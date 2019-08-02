import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:ui';

void main() => runApp(MyApp(routeName: window.defaultRouteName));

class MyApp extends StatelessWidget {
  final routeName;

  const MyApp({Key key, this.routeName}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(
        title: 'Flutter Demo Home Page',
        routeName: routeName,
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title, this.routeName}) : super(key: key);
  final String title;
  final String routeName;
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  BasicMessageChannel<String> _basicMessageChannel;
  MethodChannel _methodChannel;
  EventChannel _eventChannel;
  StreamSubscription _streamSubscribption;
  String _showMsg;
  String _responseMsg;

  @override
  void initState() {
    _basicMessageChannel = const BasicMessageChannel<String>(
        'key_basic_message_channel', StringCodec());
    _basicMessageChannel.setMessageHandler(_handlerBasicMessage);
    _methodChannel = MethodChannel('key_method_channel');
    _methodChannel.setMethodCallHandler(_handleMethodChannel);
    _eventChannel = EventChannel('key_event_channel');
    _streamSubscribption = _eventChannel
        .receiveBroadcastStream("123")
        .listen(_onToDart, onError: _onToDartError);
    super.initState();
  }

  @override
  void dispose() {
    _streamSubscribption.cancel();
    super.dispose();
  }

  Future<String> _handleMethodChannel(MethodCall call){
    if(call.method.compareTo('send')==0) {
      setState(() {
        _showMsg = call.arguments;
      });
    }
  }

  _onToDartError(error) {
    print(error);
  }

  void _onToDart(event) {
    setState(() {
      _showMsg = event;
    });
  }

  Future<String> _handlerBasicMessage(String message) {
    return Future<String>(() {
      setState(() {
        _showMsg = message;
      });
      return 'dart收到nativ BasicMsg信息：$message';
    });
  }

  void _handleSendMethodChannel(String msg) async {
    try {
      _responseMsg =
          await _methodChannel.invokeMethod('send', 'method agr') as String;
      setState(() {});
    } on PlatformException catch (e) {
      print(e);
    }
  }

  void _onChange(String code) async {
//    var responseStr = await _basicMessageChannel.send(code);
  var responseStr = await _methodChannel.invokeMethod('send',"dart to nav:$code");


    setState(() {
      _showMsg = responseStr;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              widget.routeName,
              style: TextStyle(color: Colors.red),
            ),
            Text(
              '收到native信息:$_showMsg',
              style: Theme.of(context).textTheme.display1,
            ),
            TextField(
              onChanged: _onChange,
              decoration: InputDecoration(
                focusedBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.white),
                ),
                enabledBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.red),
                ),
              ),
            )
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {},
        tooltip: 'Increment',
        child: Icon(
          Icons.send,
          color: Colors.blue,
        ),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
