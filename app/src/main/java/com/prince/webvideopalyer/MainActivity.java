package com.prince.webvideopalyer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String aqy_url="https://www.iqiyi.com/";//声明变量用于存储按钮对应的视频源
    private String txsp_url="https://v.qq.com/";    //声明变量用于存储按钮对应的视频源
    private String youku_url="https://www.youku.com/";//声明变量用于存储按钮对应的视频源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        PermissionUtils.isGrantExternalRW(this, 1);
        //实例化图片按钮对象
        ImageButton btn_aqy = findViewById(R.id.imgBtn_aqy);
        ImageButton btn_txsp= findViewById(R.id.imgBtn_txsp);
        ImageButton btn_youku= findViewById(R.id.imgBtn_youku);
        //检测当前是否有网络连接
        boolean isNetConnected = NetUtils.isNetConnected(MainActivity.this);
        //判断当前网络是否是WIFI
        boolean isWifiConnected = NetUtils.isWifiConnected(MainActivity.this);
        if(!isNetConnected){
            Toast.makeText(MainActivity.this,"当前无网络连接,请检查网络状态！",Toast.LENGTH_SHORT).show();
        }else{
         if(!isWifiConnected){
             Toast.makeText(MainActivity.this,"当前使用流量,注意流量消耗！",Toast.LENGTH_SHORT).show();
         }
        }
        //按钮1监听
        btn_aqy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VipplayerAcitivity.class);
                Log.d("Main","准备启动VipplayerAcitivity");
                intent.putExtra("Extra_url",aqy_url);
                Log.d("Main","准备传送到下一个Acitivity的URL为："+aqy_url);
                startActivity(intent);
            }
        });
        //按钮2监听
        btn_txsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VipplayerAcitivity.class);
                Log.d("Main","准备启动VipplayerAcitivity");
                intent.putExtra("Extra_url",txsp_url);
                Log.d("Main","准备传送到下一个Acitivity的URL为："+txsp_url);
                startActivity(intent);
            }
        });
        //按钮3监听
        btn_youku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VipplayerAcitivity.class);
                Log.d("Main","准备启动VipplayerAcitivity");
                intent.putExtra("Extra_url",youku_url);
                Log.d("Main","准备传送到下一个Acitivity的URL为："+youku_url);
                startActivity(intent);
            }
        });
    }
    //对获取权限处理的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                    String sdCard = Environment.getExternalStorageState();
                    //Toast.makeText(this,"获得授权",Toast.LENGTH_LONG).show();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "未获取授权，请手动授权！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
