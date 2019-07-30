package com.flutter.hybrid.android;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import io.flutter.facade.Flutter;

public class MainActivity extends AppCompatActivity {

    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.frame_content);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addFlutterView();
                replaceFlutterView();
            }
        });
    }

    private void addFlutterView(){
        View flutterView = Flutter.createView(this,getLifecycle(),"route1");
        layout.addView(flutterView);
    }

    private void replaceFlutterView(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content,Flutter.createFragment("route1")).commit();
    }

}
