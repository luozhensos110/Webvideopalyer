package com.prince.webvideopalyer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
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
import static com.prince.webvideopalyer.AppManager.getAppManager;
import static java.lang.System.exit;
//首页
public class MainActivity extends AppCompatActivity {
    private String aqy_url="https://www.iqiyi.com/";  //声明变量用于存储按钮对应的视频源
    private String txsp_url="http://m.v.qq.com";      //声明变量用于存储按钮对应的视频源
    private String youku_url="https://www.youku.com/";//声明变量用于存储按钮对应的视频源
    private String mgtv_url="https://www.mgtv.com/";  //声明变量用于存储按钮对应的视频源
    private Integer newversioncode=1 ;                //声明变量用于存储从服务器端获取的versioncode，并赋予初值
    private String newdownloadurl;                    //声明变量用于存储从服务器端获取的newdownloadurl
    private String newinfo;                           //声明变量用于存储从服务器端获取的newinfo
    public  String Urlpath="";                        //声明变量用于存储服务端配置文件路径,正式版为固定值
    private static final int TIME_EXIT=2000;          //声明变量用于两次退出判断
    private long mBackPressed;                        //声明变量用于两次退出判断，记录按键时间
    public Boolean iSback=true;                       //声明变量用于两次退出判断，是否退出
    public  Boolean codeisContains=false;             //声明变量用于判断是否存在启动密码，默认无
    public  String startcode="";                      //声明变量用于存储启动密码，默认无

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //隐藏导航栏
        //hideBottomUIMenu();
        setContentView(R.layout.activity_main);
        getAppManager().addActivity(this);
        PermissionUtils.isGrantExternalRW(this, 1);
        //实例化图片按钮对象
        ImageButton btn_aqy = findViewById(R.id.imgBtn_aqy);
        ImageButton btn_txsp= findViewById(R.id.imgBtn_txsp);
        ImageButton btn_youku= findViewById(R.id.imgBtn_youku);
        ImageButton btn_mgtv=findViewById(R.id.imgBtn_mgtv);
        Switch switch_code_start=findViewById(R.id.switch_codestart);
        //实例化获取SharedPreferences
        SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
        //检查暗码启动状态键是否存在
        boolean isContains=sharedPre.contains("isCode_start");
        //检查启动密码键是否存在
        codeisContains=sharedPre.contains("StartCode");
        //设置启动状态默认值
        boolean iScode_start=false;
        if(codeisContains){
            //获取启动密码
            startcode=sharedPre.getString("StartCode","");
        }
        //根据设定保存值改变状态
        if(isContains){
        //获取暗码启动状态
        iScode_start=sharedPre.getBoolean("isCode_start",false);
        }else{
            Log.d("暗码启动","当前键值不存在!");
        }
        //根据获取值和当前系统权限调整switch
        if(MiuiUtils.isMIUI()&&!MiuiUtils.canBackgroundStart(MainActivity.this)){
            switch_code_start.setChecked(false);
            SavesInfo.remIscode_start(MainActivity.this,false);
        }else{
        switch_code_start.setChecked(iScode_start);
        }
        //获取服务类实例化
        final ComponentName name = new ComponentName(this, SecretCodeReceiver.class);
        final PackageManager pm = getPackageManager();
        //获取传递值，用于判断是否暗码启动
        Intent intent = getIntent();
        boolean is_CODE_start=intent.getBooleanExtra("iScode_start",false);
        if(iScode_start){
            if(is_CODE_start){
            Log.d("暗码启动","暗码启动打开并且暗码启动");
            }else{
              /*  final EditText inputServer = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请输入启动密码").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        inputServer.getText().toString();
                    }
                });
                builder.show();*/
               isNotCodeStart();
                Log.d("暗码启动","暗码启动打开桌面启动");
            }
        }else{
            Log.d("暗码启动","暗码启动未打开");
        }

        new MyXmlTask().execute();
        //初始化设置服务自启动
        set_init();
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
                intent.putExtra("Extra_url",aqy_url);
                //Log.d("Main","准备传送到下一个Acitivity的URL为："+aqy_url);
                startActivity(intent);
            }
        });
        //按钮2监听
        btn_txsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VipplayerAcitivity.class);
                intent.putExtra("Extra_url",txsp_url);
                //Log.d("Main","准备传送到下一个Acitivity的URL为："+txsp_url);
                startActivity(intent);
            }
        });
        //按钮3监听
        btn_youku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VipplayerAcitivity.class);
                intent.putExtra("Extra_url",youku_url);
                //Log.d("Main","准备传送到下一个Acitivity的URL为："+youku_url);
                startActivity(intent);
            }
        });
        //按钮4监听
        btn_mgtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VipplayerAcitivity.class);
                intent.putExtra("Extra_url",mgtv_url);
                //Log.d("Main","准备传送到下一个Acitivity的URL为："+mgtv_url);
                startActivity(intent);
            }
        });
              //switch事件监听
        switch_code_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SavesInfo.remIscode_start(MainActivity.this,true);
                    boolean is=MiuiUtils.canBackgroundStart(MainActivity.this);
                    //判断是否小米
                    if(MiuiUtils.isMIUI()){
                        //判断是否授权
                        if(MiuiUtils.canBackgroundStart(MainActivity.this)){
                            //保存switch状态
                            SavesInfo.remIscode_start(MainActivity.this,true);
                            //启用用一个广播（暗码启动）
                            pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                    PackageManager.DONT_KILL_APP);
                            //隐藏桌面图标
                        }else{
                            permissions_dialog();
                        }
                    }
                }else{
                    //保存switch状态
                    SavesInfo.remIscode_start(MainActivity.this,false);
                    // 禁用一个广播（暗码启动）
                    pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(MainActivity.this,"暗码启动关闭",Toast.LENGTH_SHORT).show();
                    //显示桌面图标

                }
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
            SharedPreferences preferences = getSharedPreferences("config",MODE_PRIVATE);
            String path =preferences.getString("Checkurl","");
            //Log.d("MSG","Urlpath检查："+path);
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
                //Log.d("MSG","获取到的请求码："+code);
                if(200==code){
                    InputStream is=connection.getInputStream();//获取读流
                    //Log.d("test","获取数据流");
                    //解析XML  （我们使用Android特有的pull解析）
                    //实例化解析器
                    XmlPullParser pullParser= Xml.newPullParser();
                    //Log.d("test","实例化解析器");
                    //进行解析  (参数一：数据源（网络流）；参数二：编码方式)
                    pullParser.setInput(is,"UTF-8");
                    //Log.d("test","进行解析");
                    //解析标签类型
                    int type=pullParser.getEventType();
                    //Log.d("test","设置变量存储类型");
                    //判断不是结束标签
                    while(type!=XmlPullParser.END_DOCUMENT){
                        //设置变量用于存储标签名称
                        String nodeName = pullParser.getName();
                        switch (type){
                            case XmlPullParser.START_TAG:{
                                if("versionname".equals(nodeName)){
                                    //声明变量用于存储从服务器端获取的newversionname.
                                    String newversionname = pullParser.nextText();
                                    //Log.d("Server_info","newversionname="+ newversionname);
                                }else if("versioncode".equals(nodeName)){
                                    newversioncode =Integer.parseInt(pullParser.nextText());
                                    //Log.d("Server_info","newversioncode="+ newversioncode.toString());
                                }
                                else if("downloadurl".equals(nodeName)){
                                    newdownloadurl = pullParser.nextText();
                                    //Log.d("Server_info","newdownloadurl="+newdownloadurl);
                                }
                                else if("newinfo".equals(nodeName)){
                                    newinfo=pullParser.nextText();
                                    //Log.d("Server_info","newinfo="+newinfo);
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
                //Log.d("登录页面_检查更新","选择更新，当前检查的页面是："+Urlpath);
                //获取是否连接网络
                boolean isNetConnected = NetUtils.isNetConnected(MainActivity.this);
                if(!isNetConnected){
                    Toast toast = Toast.makeText(this,null,Toast.LENGTH_SHORT);
                    toast.setText("网络未连接，请检查连接并稍后再试！");
                    toast.show();
                   // Log.d("登录页面_检查更新","选择更新，网络未连接，请检查连接并稍后再试！");
                    break;
                }else{
                    SharedPreferences preferences = getSharedPreferences("config",MODE_PRIVATE);
                    //String str1 = preferences.getString("str1","");
                    Urlpath=preferences.getString("Checkurl","");
                    //设置更新文件地址
                    if(Urlpath.isEmpty()){
                        Toast.makeText(MainActivity.this,"更新地址为空,请输入更新地址！",Toast.LENGTH_SHORT).show();
                        setCheckUpdate();
                        //Urlpath="http://texxa7.natappfree.cc/update.xml";
                        break;
                    }else{
                    //获取服务器端信息
                        AsyncTask execute = new MyXmlTask().execute();
                        //获取本地信息
                    int versionCode = APKVersionCodeUtils.getVersionCode(this);
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
                        //Log.d("登录页面_检查更新","当前的版本无需更新:");
                        Toast.makeText(MainActivity.this,"当前已经是最新的版本",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }}
                //选择关于按钮后事件
            case R.id.About:
                //Log.d("登录页面_关于","打开关于页面");;
                Intent intent = new Intent(MainActivity.this,AboutAcitivity.class);
                startActivity(intent);
                break;
            case R.id.Contact_us:
                //Log.d("MENU","点击的是联系我们菜单");
                Contact_us();
                break;
                //选择更新服务器地址按钮
            case R.id.Server_url:
                //手动添加更新地址的方法调用
                setCheckUpdate();
                break;
            case R.id.Start_code:
                //修改启动密码的方法调用
                setStartcode();
                break;
            default:
        }
        return true;
    }
    //联系我们的方法
    private void Contact_us(){
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用,intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
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
    }

    //手动添加更新地址的方法,(测试用,正式版此地址固定，不用手动输入)
    private void setCheckUpdate(){
    final EditText inputServer = new EditText(this);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入地址").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            inputServer.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("Checkurl", inputServer.getText().toString());
            //Log.d("MENU","输入的检查更新的地址为："+inputServer.getText().toString());
            editor.apply();
        }
    });
        builder.show();
    }

    //手动设置启动密码的方法
    private void setStartcode(){
        if(!codeisContains){
            Toast.makeText(MainActivity.this,"初次使用，请设置启动密码",Toast.LENGTH_SHORT).show();
            final EditText inputServer = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("启动密码设置").setMessage("初次设置，设置桌面启动密码!").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                        SavesInfo.SaveCode(MainActivity.this,inputServer.getText().toString());
                        Toast.makeText(MainActivity.this,"设置密码为："+ inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }else{
            final EditText old_startcode = new EditText(this);
            final EditText new_startcode = new EditText(this);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("启动密码修改").setMessage("请输入旧密码").setIcon(android.R.drawable.ic_dialog_info).setView(old_startcode);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //判断输入旧密码是否正确
                    if(old_startcode.getText().toString().equals(startcode)){
                    builder.setMessage("请输入新密码").setIcon(android.R.drawable.ic_dialog_info).setView(new_startcode);
                    builder.setPositiveButton("修改密码", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SavesInfo.SaveCode(MainActivity.this,new_startcode.getText().toString());
                            Toast.makeText(MainActivity.this,"设置密码为："+ new_startcode.getText().toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                    }else{
                        Toast.makeText(MainActivity.this,"密码输入错误,请重新输入!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        }
    }

    //暗码启动桌面启动提示框
    private void isNotCodeStart(){
        if(!codeisContains){
            Toast.makeText(MainActivity.this,"初次使用，请设置启动密码",Toast.LENGTH_SHORT).show();
        }
        final EditText inputServer = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("暗码启动").setMessage("设置为暗码启动，桌面启动需输入启动密码!").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("暗码启动","暗码启动密码验证");
                if(codeisContains){
                    Log.d("暗码启动","暗码启动密码验证,密码存在");
                    if(inputServer.getText().toString().equals(startcode)){
                        //Log.d("暗码启动","启动密码密码输入正确!");
                        Toast.makeText(MainActivity.this,"密码正确!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"密码错误，软件自动关闭!",Toast.LENGTH_LONG).show();
                       AppManager.getAppManager().finishAllActivity();
                       AppManager.getAppManager().AppExit(MainActivity.this);
                    }
                }else{
                    //inputServer.getText().toString();
                    SavesInfo.SaveCode(MainActivity.this,inputServer.getText().toString());
                    Toast.makeText(MainActivity.this,"设置密码为："+ inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                    //Log.d("暗码启动","启动密码不存在，设置并保存");
                }

            }
        });
        builder.show();
    }

    //根据OEM厂商调整实现后台运行暗码
    private void set_init(){
        /*
          针对特殊厂商设置自启动项，否则应用关闭广播接收器关闭导致后台不会接收到暗码
         */
        final Intent[] POWERMANAGER_INTENTS = {
                new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
                new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
                new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
                new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
                new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
                new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))};
        final SharedPreferences.Editor pref = getSharedPreferences("allow_notify", MODE_PRIVATE).edit();
        pref.apply();
        final SharedPreferences sp = getSharedPreferences("allow_notify", MODE_PRIVATE);

        if (!sp.getBoolean("protected", false)) {
            for (final Intent intent : POWERMANAGER_INTENTS)
                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("暗码启动权限").setMessage("请开启自启动权限")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(intent);
                                    sp.edit().putBoolean("protected", true).apply();
                                }
                            })
                            .setCancelable(false)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    iSback=false;
                                }
                            })
                            .create().show();
                    break;
                }
        }

    }

    //提示用户授权允许后台弹出界面
    private void  permissions_dialog(){
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();//创建对话框
        final AlertDialog isNot_permissions=new AlertDialog.Builder(MainActivity.this).create();//创建提示手动授权
        isNot_permissions.setIcon(R.mipmap.permissions);
        isNot_permissions.setTitle("权限设置");
        isNot_permissions.setMessage("小米手机，请手动授权允许后台弹出界面!\n暗码为：*#*#2305#*#*");
        isNot_permissions.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isNot_permissions.dismiss();
                try{
                    //MIUI8
                    Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    localIntent.putExtra("extra_pkgname", MainActivity.this.getPackageName());
                    startActivity(localIntent);
                }catch (Exception e){
                    try{
                        // MIUI 5/6/7
                        Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                        localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                        localIntent.putExtra("extra_pkgname", MainActivity.this.getPackageName());
                        startActivity(localIntent);

                    }catch(Exception e1){
                        // 否则跳转到应用详情
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            }
        });
        //窗口显示
        isNot_permissions.show();
    }

    //两次返回退出应用
    @Override
    public void onBackPressed(){
        if(mBackPressed+TIME_EXIT>System.currentTimeMillis()){
            //super.onBackPressed();
            getAppManager().finishAllActivity();
            getAppManager().AppExit(this);
            exit(0);
        }else{
            Toast.makeText(this,"再点击一次返回退出程序",Toast.LENGTH_SHORT).show();
            mBackPressed=System.currentTimeMillis();
        }
    }

    //对获取权限处理的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                //String sdCard = Environment.getExternalStorageState();
                Toast.makeText(this,"获得授权",Toast.LENGTH_LONG).show();
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
