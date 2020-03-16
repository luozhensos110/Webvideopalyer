package com.prince.webvideopalyer;
/*
*  VIP_PlayerUI;
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

import java.util.Objects;

public class VIP_Acitivity extends AppCompatActivity {
    private LinearLayout agentWebLL;//用来承载AgentWebView
    private String Vip_start;       //定义变量来存储选择解析接口地址
    private  int width;             //用于存储屏幕宽度
    private  int height;            //用于存储屏幕高度
    private float startY = 0;       //手指按下时的Y坐标用于检测屏幕上手指滑动方向
    private float startX = 0;       //手指按下时的Y坐标用于判断是不是在有效范围滑动

   //加载
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        Objects.requireNonNull(getSupportActionBar()).hide();
        //隐藏导航栏
        hideBottomUIMenu();
        //加载Layout
        setContentView(R.layout.activity_vip_acitivity);
        //获取intent传递的数据
        Intent intent = getIntent();
        //private TextView receiveTitle;//用于标题提示
        //定义变量来存储选择解析接口ID
        String vip_start_id = intent.getStringExtra("Extra_Vip_start_id");
        Vip_start = select_url_start(vip_start_id);
        //Log.d("VIP","选择打开是："+ Vip_start);
        //定义变量来存储需要解析的地址
        String vip_end = intent.getStringExtra("Extra_Vip_end");
        //完整地址拼接
        //定义变量来存储拼接好的地址
        String vip_url = Vip_start + vip_end;
        Log.d("Test","需要解析的地址为："+ vip_end);
        Log.d("Test","选择打开的视频源是："+ vip_url);
        //获取屏幕宽度
        width=getWidth();
        //获取屏幕高度
        height=getHeight();
        Log.d("Test","高度"+height);
        Log.d("Test","宽度"+width);
        //初始化视窗
        initView();
        AgentWeb mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(agentWebLL, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(webChromeClient)
                .setWebViewClient(webViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                //.setWebLayout(new WebLayout(this))
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(vip_url);
        //设置请求头,可以屏蔽部分根据请求头判断类型的JS广告
        mAgentWeb.getAgentWebSettings().getWebSettings().setUserAgentString("agentweb");
        //定义Webview
        WebView mAgentweb_vip = mAgentWeb.getWebCreator().getWebView();
        //Touch事件监听
        mAgentweb_vip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Touch","TOUCH事件触发！");
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        Log.d("Touch","初始坐标X="+startX+",  Y="+startY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float endX = event.getX();
                        float endY = event.getY();
                        float distancey = startY - endY;//用于计算y轴移动距离
                        float distancex = startX - endX;//用于计算x轴移动距离，防止误触发
                        boolean isNot_set = Math.abs(distancex)> 5 || startY>height*0.9;
                        Log.d("Test","isNot_set="+isNot_set);
                        if(isNot_set){
                            break;
                        }else{
                        if(startX>width/2){
                            //音量调节
                            Log.d("Touch","触摸右边部分");
                            final double FLING_MIN_DISTANCE = 0.5;
                            final double FLING_MIN_VELOCITY = 0.5;
                            //AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                            //int maxVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                            //Log.d("VOL","最大音量的值为："+maxVolume);
                            if(distancey>FLING_MIN_DISTANCE && Math.abs(distancey)>FLING_MIN_VELOCITY){
                                Log.d("VOL","音量增大");
                                //am.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                                setvolness(1f);
                            }
                            if(distancey<FLING_MIN_DISTANCE && Math.abs(distancey)>FLING_MIN_VELOCITY){
                                //am.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                                Log.d("VOL","音量减小");
                                setvolness(-1f);
                            }
                        }else{
                            //亮度调节
                            Log.d("Touch","触摸左边部分");
                            final double fling_min_distance = 0.5;
                            final double fling_min_velocity = 0.5;
                            if (distancey > fling_min_distance && Math.abs(distancey) > fling_min_velocity) {
                                setbrightness(10);
                            }
                            if (distancey < fling_min_distance && Math.abs(distancey) > fling_min_velocity) {
                                setbrightness(-10);
                            }
                        }
                        break;
                        }
                }
                return false;
            }
        });
    }

   //   隐藏虚拟按键，并且全屏函数hideBottomUIMenu()
    @SuppressLint("ObsoleteSdkInt")
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    //获取屏幕高度getHeight();返回屏幕高度
    public int getHeight()//典型的需要返回值的
    {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return  outMetrics.heightPixels;
    }

    //获取屏幕宽度getWidth();返回屏幕宽度
    public int getWidth()//典型的需要返回值的
    {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return  outMetrics.widthPixels;
    }

    //重载返回按钮过程，返回上一个acitivity
    @Override
    public void onBackPressed() {
        VIP_Acitivity.this.finish();
        Intent intent=new Intent(VIP_Acitivity.this,VipplayerAcitivity.class);
        Log.d("VIP_A","准备返回VipplayerAcitivity");
        //intent.putExtra("Extra_url",youku_url);
        //Log.d("Main","准备传送到下一个Acitivity的URL为："+youku_url);
        startActivity(intent);
    }

   //页面初始化
    private void initView() {
        agentWebLL = findViewById(R.id.agentWeb_VIP);
        Log.d("VIP","页面初始化");
    }

  //设置屏幕亮度子函数 setbrightness();,0为最暗，1为最亮
    public void setbrightness(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;

        } else if (lp.screenBrightness < 0.1) {
            lp.screenBrightness = (float) 0.1;
        }
        getWindow().setAttributes(lp);
        float sb = lp.screenBrightness;
        //Log.d("Touch","屏幕亮度调节为："+(int) Math.ceil(sb * 100) + "%");
        Log.d("Touch","sb的值为："+sb);
        if(sb==1){
            Toast.makeText(VIP_Acitivity.this,"已经是最大亮度",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(VIP_Acitivity.this,"屏幕亮度调整为："+(int) Math.ceil(sb * 100) + "%",Toast.LENGTH_SHORT).show();
        }
    }

  //设置音量值子函数 setvolness();
    public void setvolness(float volness) {
        //音量控制,初始化定义
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //取得最大音量
        assert mAudioManager != null;
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d("VOL_Test","最大音量值为："+maxVolume);
        //获得当前音量
        int mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
       // Log.d("VOL","当前音量值为："+mCurrentVolume);
        //设置新的音量值
        float setVolume = mCurrentVolume+volness;
        int volume = Math.round(setVolume);
        if (volume>=maxVolume){
            volume=maxVolume;}
        else if(volume<=0){
            volume=0;
        }
        Log.d("VOL_Test","将要设置为："+volume);
        //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volness,AudioManager.FLAG_VIBRATE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,0);
       //提示当前音量值
        //Log.d("VOL_Test","音量百分比"+(volume*100)/maxVolume+"%");
        Toast.makeText(VIP_Acitivity.this,"音量调整为："+(volume*100)/maxVolume + "%",Toast.LENGTH_SHORT).show();
    }

    //  webChromeClient.onReceivedTitle()方法，用于获取标题设置提示
    private WebChromeClient webChromeClient;
    {
        webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        };
    }

    //WebViewClient()事件，防止跳转到系统浏览器
    private WebViewClient webViewClient=new WebViewClient(){
        // 可以去看上一级已经写了
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request.getUrl().toString().startsWith(Vip_start)) {
                return super.shouldOverrideUrlLoading(view, request);
            }
            VIP_Acitivity.this.finish();
            return false;
        }
//重载页面加载事件
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    //播放器选择select_url_start()函数;传入选择的下标，输出对应解析器接口
    public String select_url_start(String select){
        int select_id = Integer.valueOf(select);
        switch (select_id){
            case 0:
                //
                select="https://cdn.yangju.vip/k/?url=";
                break;
            case 1:
                select="https://jsap.attakids.com/?url=";
                break;
            case 2:
                select="https://jx.618g.com/?url=";
                break;
            case 3:
                select="https://api.2020jx.com/?url=";
                break;
            case 4:
                select="https://jx.wslmf.com/?url=";
                break;
            case 5:
                select="https://vip.jlsprh.com/?url=";
                break;
            case 6:
                select="预留";
                break;
            case 7:
                select="预留";
                break;
                default:
                    select="Hello,world";
                    break;
        }
        return select;
    }
}
