package com.flutter.hybrid.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StringCodec;
import io.flutter.view.FlutterView;

public class MainActivity extends AppCompatActivity implements BasicMessageChannel.MessageHandler<String>,BasicMessageChannel.Reply<String>, MethodChannel.MethodCallHandler, EventChannel.StreamHandler {

    FrameLayout layout;
    FlutterView flutterView;
    EditText editText;
    TextView text;

    private BasicMessageChannel basicMessageChannel;
    private MethodChannel methodChannel;
    private EventChannel eventChannel;
    private EventChannel.EventSink eventSink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.frame_content);
        editText = findViewById(R.id.edt_content);
        text = findViewById(R.id.tv_rec);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                basicMessageChannel.send(s.toString(),MainActivity.this);
//                methodChannel.invokeMethod("send",s.toString());
                if (eventSink != null) {
                    eventSink.success(s.toString());
                } else {
                    eventChannel = new EventChannel(flutterView,"key_event_channel");
                    eventChannel.setStreamHandler(MainActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFlutterView();
            }
        });

    }

    /**
     * 初始化，设置消息处理器
     */
    private void initChannel() {
        basicMessageChannel = new BasicMessageChannel(flutterView, "key_basic_message_channel2", StringCodec.INSTANCE);
        basicMessageChannel.setMessageHandler(this);
        methodChannel = new MethodChannel(flutterView,"key_method_channel");
        methodChannel.setMethodCallHandler(this);
        eventChannel = new EventChannel(flutterView,"key_event_channel");
        eventChannel.setStreamHandler(this);
    }

    private void addFlutterView(){
        flutterView = Flutter.createView(this,getLifecycle(),"route1");
        layout.addView(flutterView);
        initChannel();
    }

    private void replaceFlutterView(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content,Flutter.createFragment("route1")).commit();
    }

    /**
     * BasicMessageChannel
     * 接受dart发送过来的信息，并回复给dart信息
     * @param s
     * @param reply
     */
    @Override
    public void onMessage(@Nullable String s, @NonNull BasicMessageChannel.Reply<String> reply) {
        Log.i(TAG, "onMessage:" + s);
        text.setText("m"+s);
        reply.reply("nativ收到dart消息："+s);
    }

    /**
     * BasicMessageChannel
     * dart回复给java信息（java发给dart）
     * @param s
     */
    @Override
    public void reply(@Nullable String s) {
        Log.i(TAG, "reply:" + s);
        text.setText(s);
    }

    private final static String TAG = "FKY_CHANNEL";

    /**
     * method channel 回调
     * @param methodCall
     * @param result
     */
    @Override
    public void onMethodCall(@NonNull MethodCall methodCall, @NonNull MethodChannel.Result result) {
        if (methodCall.method.equals("send")) {
            String content = (String) methodCall.arguments;
            text.setText(content);
            result.success("nati回复："+content);
        }else{
            result.error("方法找不到1","方法找不到1","方法找不到1");
        }
    }

    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        Log.i(TAG, "onListen "+o.toString());
        text.setText(o.toString());
        this.eventSink = eventSink;
    }

    @Override
    public void onCancel(Object o) {
        Log.i(TAG, "onCancel "+o.toString());
    }
}
