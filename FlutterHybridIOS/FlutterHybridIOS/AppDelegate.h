//
//  AppDelegate.h
//  FlutterHybridiOS
//
//  Created by mac on 2019/7/31.
//  Copyright © 2019 feng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong) NSPersistentContainer *persistentContainer;

- (void)saveContext;


@end

