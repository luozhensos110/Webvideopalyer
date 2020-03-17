package com.prince.webvideopalyer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String aqy_url="https://www.iqiyi.com/";  //声明变量用于存储按钮对应的视频源
    private String txsp_url="https://v.qq.com/";      //声明变量用于存储按钮对应的视频源
    private String youku_url="https://www.youku.com/";//声明变量用于存储按钮对应的视频源
    private Integer newversioncode=1 ;                //声明变量用于存储从服务器端获取的versioncode，并赋予初值
    private String newversionname ;                   //声明变量用于存储从服务器端获取的newversionname.
    private String newdownloadurl;                    //声明变量用于存储从服务器端获取的newdownloadurl
    private String newinfo;                           //声明变量用于存储从服务器端获取的newinfo
    public String Urlpath="";                         //声明变量用于存储服务端配置文件路径,正式版为固定值

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
        new MyXmlTask().execute();
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

    //开启异步任务获取服务端信息
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class MyXmlTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            //获取网络数据
            String path=Urlpath;
            Log.d("MSG","Urlpath检查："+Urlpath);
            try {
                URL url=new URL(path);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                //设置连接服务器超时时间
                connection.setConnectTimeout(10*1000);
                //设置从服务器读取数据超时时间
                connection.setReadTimeout(10*1000);
                //设置请求方式（一般提交数据使用POST，获取数据使用GET）
                connection.setRequestMethod("GET");
                //设置请求时间
                connection.setConnectTimeout(5000);
                //获取结果码
                int code=connection.getResponseCode();
                Log.d("MSG","获取到的请求码："+code);
                if(200==code){
                    InputStream is=connection.getInputStream();//获取读流
                    Log.d("test","获取数据流");
                    //解析XML  （我们使用Android特有的pull解析）
                    //实例化解析器
                    XmlPullParser pullParser= Xml.newPullParser();
                    Log.d("test","实例化解析器");
                    //进行解析  (参数一：数据源（网络流）；参数二：编码方式)
                    pullParser.setInput(is,"UTF-8");
                    Log.d("test","进行解析");
                    //解析标签类型
                    int type=pullParser.getEventType();
                    Log.d("test","设置变量存储类型");
                    //判断不是结束标签
                    while(type!=XmlPullParser.END_DOCUMENT){
                        //设置变量用于存储标签名称
                        String nodeName = pullParser.getName();
                        switch (type){
                            case XmlPullParser.START_TAG:{
                                if("versionname".equals(nodeName)){
                                    newversionname=pullParser.nextText();
                                    Log.d("Server_info","newversionname="+newversionname);
                                }else if("versioncode".equals(nodeName)){
                                    newversioncode =Integer.parseInt(pullParser.nextText());
                                    Log.d("Server_info","newversioncode="+ newversioncode.toString());
                                }
                                else if("downloadurl".equals(nodeName)){
                                    newdownloadurl = pullParser.nextText();
                                    Log.d("Server_info","newdownloadurl="+newdownloadurl);
                                }
                                else if("newinfo".equals(nodeName)){
                                    newinfo=pullParser.nextText();
                                    Log.d("Server_info","newinfo="+newinfo);
                                }
                                break;
                            }
                            case XmlPullParser.END_TAG:{
                                break;
                            }
                            default:
                                break;
                        }
                        type=pullParser.next();
                    }
                }
            }
            //捕捉URL异常
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //捕捉IO异常
            catch (IOException e) {
                e.printStackTrace();
            }
            //捕捉解析异常
            catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    //重载Menu菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //menu菜单选择事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //选择更新按钮后事件
            case R.id.Check_update:
                Log.d("登录页面_检查更新","选择更新，当前检查的页面是："+Urlpath);
                //获取是否连接网络
                boolean isNetConnected = NetUtils.isNetConnected(MainActivity.this);
                if(!isNetConnected){
                    Toast toast = Toast.makeText(this,null,Toast.LENGTH_SHORT);
                    toast.setText("网络未连接，请检查连接并稍后再试！");
                    toast.show();
                   // Log.d("登录页面_检查更新","选择更新，网络未连接，请检查连接并稍后再试！");
                    break;
                }else{
                    //设置更新文件地址
                    if(Urlpath==null){
                        //setCheckUpdate();
                        Urlpath="http://texxa7.natappfree.cc/update.xml";
                        break;
                    }else{
                    //获取服务器端信息
                    new MyXmlTask().execute();
                    //获取服务器端信息测试
                    //Log.d("登录页面_检查更新","准备更新，准备从服务器获取数据");
                    //Log.d("登录页面_检查更新","准备更新，获取的Code是："+newversioncode);
                    //Log.d("登录页面_检查更新","准备更新，获取的Name是："+newversionname);
                    //Log.d("登录页面_检查更新","准备更新，获取的Url是："+newdownloadurl);
                    //Log.d("登录页面_检查更新","准备更新，获取的Info是："+newinfo);
                    //获取本地信息
                    int versionCode = APKVersionCodeUtils.getVersionCode(this);
                    //Log.d("登录页面_检查更新","获取到的本地版本号是："+versionCode);
                    //判断是否需要更新
                    if (versionCode < newversioncode) {
                        Toast toast = Toast.makeText( MainActivity.this, null, Toast.LENGTH_SHORT);
                        toast.setText("有新的版本可以更新，确认是否更新");
                        toast.show();
                        AlertDialog dialog = new AlertDialog.Builder(this).create();//创建对话框
                        final AlertDialog isNotwifi=new AlertDialog.Builder(this).create();//创建确认流量下载对话框
                        //isNotwifi.setIcon(R.mipmap.alter_logo);
                        isNotwifi.setTitle("当前非WIFI情况下，确认下载！");
                        isNotwifi.setButton(DialogInterface.BUTTON_POSITIVE, "现在更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isNotwifi.dismiss();
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(newdownloadurl);
                                intent.setData(content_url);
                                startActivity(intent);
                            }
                        });
                        isNotwifi.setButton(DialogInterface.BUTTON_NEGATIVE, "稍后更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isNotwifi.dismiss();
                            }
                        });
                        //dialog.setIcon(R.mipmap.alter_logo);//设置对话框icon
                        dialog.setTitle("有新的版本，确认是否更新");//设置对话框标题
                        dialog.setMessage(newinfo);//设置文字显示内容
                        dialog.setCancelable(false);//不能通过BACK取消
                        //分别设置三个button
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"现在更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("登录页面_检查更新","点击的是现在更新按钮");
                                //关闭对话框
                                dialog.dismiss();
                                //获取当前网络状态类型
                                String operatorName = NetUtils.getOperatorName(MainActivity.this);
                                //判断是否WIFI连接
                                boolean isWifiConnected = NetUtils.isWifiConnected(MainActivity.this);
                                if(isWifiConnected){
                                    //Log.d("登录页面_检查更新","当前为WIFI连接，开始下载程序...");
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(newdownloadurl);
                                    intent.setData(content_url);
                                    startActivity(intent);
                                }else{
                                    //Log.d("登录页面_检查更新","当前为运营商连接，运营商为"+ operatorName);
                                    //Log.d("登录页面_检查更新","下载会消耗流量，是否下载！");
                                    //显示非WIFI提示框
                                    isNotwifi.show();
                                }
                            }
                        });
                        //按钮事件处理
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "暂不更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Log.d("登录页面_检查更新","点击的是暂不更新按钮");
                                //关闭对话框
                                dialog.dismiss();
                            }
                        });
                        //显示对话框
                        dialog.show();

                    }else{
                        //否则吐司，说现在是最新的版本
                        Log.d("登录页面_检查更新","当前的版本无需更新:");
                        //通用吐司小米显示应用名称问题
                        Toast toast = Toast.makeText(this,null,Toast.LENGTH_SHORT);
                        toast.setText("当前已经是最新的版本");
                        toast.show();
                    }
                    break;
                }}
                //选择关于按钮后事件
            case R.id.About:
                //Log.d("登录页面_关于","打开关于页面");
                Intent intent = new Intent(MainActivity.this,AboutAcitivity.class);
                startActivity(intent);
                break;
            case R.id.Contact_us:
                //Log.d("MENU","点击的是联系我们菜单");
                // 必须明确使用mailto前缀来修饰邮件地址,如果使用
                // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
                Uri uri = Uri.parse("mailto:297006042@qq.com");
                String[] email = {"297006042@qq.com"};
                Intent intent_Contact = new Intent(Intent.ACTION_SENDTO, uri);
                // 抄送人
                intent_Contact.putExtra(Intent.EXTRA_CC, email);
                // 主题
                intent_Contact.putExtra(Intent.EXTRA_SUBJECT, "关于WebVideoPlayer");
                // 正文
                intent_Contact.putExtra(Intent.EXTRA_TEXT, "");
                //选择邮件类应用界面,用户可能有多个邮箱应用
                startActivity(Intent.createChooser(intent_Contact, "请选择邮件类应用"));
                break;
                //选择更新服务器地址按钮
            case R.id.Server_url:
                //手动添加更新地址的方法调用
                setCheckUpdate();
                break;
            default:
        }
        return true;
    }

    //手动添加更新地址的方法,(测试用,正式版此地址固定，不用手动输入)
    public  void setCheckUpdate(){
    final EditText inputServer = new EditText(this);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入地址").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            inputServer.getText().toString();
            Log.d("MENU","输入的检查更新的地址为："+inputServer.getText().toString());
            Urlpath=inputServer.getText().toString();
        }
    });
        builder.show();
    }

    //对获取权限处理的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                //String sdCard = Environment.getExternalStorageState();
                //Toast.makeText(this,"获得授权",Toast.LENGTH_LONG).show();
            } else {
                //开启线并提示
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
