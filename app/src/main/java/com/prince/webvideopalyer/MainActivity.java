package com.prince.webvideopalyer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        //隐藏导航栏
        //hideBottomUIMenu();
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

    //重载Menu菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //menu点击事件处理
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.About:
               Intent intent1=new Intent(MainActivity.this,AboutAcitivity.class);
               startActivity(intent1);
               break;
           case R.id.Contact_us:
               Log.d("MENU","点击的是联系我们菜单");
               // 必须明确使用mailto前缀来修饰邮件地址,如果使用
               // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
               Uri uri = Uri.parse("mailto:297006042@qq.com");
               String[] email = {"297006042@qq.com"};
               Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
               intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
               intent.putExtra(Intent.EXTRA_SUBJECT, "关于WebVideoPlayer"); // 主题
               intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
               startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
               break;
           case R.id.Check_update:
               Log.d("MENU","点击的是检查更新菜单");
               int versionCode = APKVersionCodeUtils.getVersionCode(this);
               Log.d("MENU","获取到的本地版本号是："+versionCode);
               break;
       }
       return true;
    }

    //对获取权限处理的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
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
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
