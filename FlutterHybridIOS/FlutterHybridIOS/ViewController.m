//
//  ViewController.m
//  FlutterHybridiOS
//
//  Created by mac on 2019/7/31.
//  Copyright © 2019 feng. All rights reserved.
//

#import "ViewController.h"
#import <Flutter/Flutter.h>
#import <FlutterPluginRegistrant/GeneratedPluginRegistrant.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button addTarget:self
               action:@selector(handleButtonAction)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:@"Test" forState:UIControlStateNormal];
    [button setBackgroundColor:[UIColor redColor]];
    button.frame = CGRectMake(80, 210, 160, 40);
    
    [self.view addSubview:button];
}
    
- (void)handleButtonAction{
    FlutterViewController *flutterController =
    [FlutterViewController new];
    [flutterController setInitialRoute:@"route1_IOS"];
    [self presentViewController:flutterController animated:true completion:nil];
}


@end
