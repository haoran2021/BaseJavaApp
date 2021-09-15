package com.base.basejavapro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.base.annotation.Route;
import com.base.basejavapro.annotation.InjectUtils;
import com.base.basejavapro.annotation.InjectView;


@Route(path = "Main",group = "Activity")
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.tv_test)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtils.injectView(this);

        textView.setText("注解反射测试");
    }

}