package com.prince.webvideopalyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

public class VIP_Acitivity extends AppCompatActivity {
    /**
     * 用来承载AgentWebView
     */
    private LinearLayout agentWebLL;
    private TextView receiveTitle;
    private String Vip_start_id;
    private String Vip_end;
    private String Vip_url;
    private String Vip_start;
    private long firstTime;// 记录点击返回时第一次的时间毫秒值


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_vip_acitivity);
        Intent intent = getIntent();
        Vip_start_id = intent.getStringExtra("Extra_Vip_start_id");
        Vip_start = select_url_start(Vip_start_id);
        //Log.d("VIP","选择打开是："+ Vip_start);
        //Vip_start ="http://cdn.yangju.vip/k/?url=";
        Vip_end = intent.getStringExtra("Extra_Vip_end");
        Vip_url=Vip_start+Vip_end;
        Log.d("Test","需要解析的地址为："+Vip_end);
        Log.d("Test","选择打开的视频源是："+ Vip_url);
        initView();//初始化视窗
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
                .go(Vip_url);
        //mAgentWeb.getWebCreator().getWebView();//获取 WebView
        mAgentWeb.getAgentWebSettings().getWebSettings().setUserAgentString("agentweb");

    }
    @Override
    public void onBackPressed() {
        VIP_Acitivity.this.finish();
        Intent intent=new Intent(VIP_Acitivity.this,VipplayerAcitivity.class);
        Log.d("VIP_A","准备返回VipplayerAcitivity");
        //intent.putExtra("Extra_url",youku_url);
        //Log.d("Main","准备传送到下一个Acitivity的URL为："+youku_url);
        startActivity(intent);
    }


    private void initView() {
        agentWebLL = findViewById(R.id.agentWeb_VIP);
        Log.d("VIP","页面初始化");
    }


    // 看导包 这些是第三方的 不是自带的
    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // 设置接收到的 title
            if(receiveTitle != null){
                receiveTitle.setText(title);
            }

        }
    };
    //看导包 这些是第三方的 不是自带的
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

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    /**
     *
     * @param select 用于播放器选择界面，传入选择的下标
     * @return 返回选择的播放器(解析接口)地址
     */
    public String select_url_start(String select){
        int select_id = Integer.valueOf(select).intValue();
        switch (select_id){
            case 0:
                //
                select="https://cdn.yangju.vip/k/?url=";
;                break;
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
