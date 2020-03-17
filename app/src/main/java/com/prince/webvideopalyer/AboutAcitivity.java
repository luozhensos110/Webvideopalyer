package com.prince.webvideopalyer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class AboutAcitivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_acitivity);
        textView = findViewById(R.id.textView_version);
        String versionName = APKVersionCodeUtils.getVerName(this);
        textView.setText("当前软件版本为"+versionName);

    }
}
