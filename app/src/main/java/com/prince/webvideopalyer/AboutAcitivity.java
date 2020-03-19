package com.prince.webvideopalyer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//关于页面
public class AboutAcitivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_acitivity);
        AppManager.getAppManager().addActivity(this);
        TextView textView = findViewById(R.id.textView_version);
        String versionName = APKVersionCodeUtils.getVerName(this);
        textView.setText("当前软件版本为"+versionName);
    }
}
