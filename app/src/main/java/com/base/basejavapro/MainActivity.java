package com.base.basejavapro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.base.annotation.Route;


@Route(path = "Main",group = "Activity")
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}