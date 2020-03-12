package com.prince.webvideopalyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

public class VipplayerAcitivity extends AppCompatActivity {
    public String url_start_id;
    public String url_end;
    private TextView receiveTitle;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] select_source;
    private Button Btn_VIP;
    /**
     * 用来承载AgentWebView
     */
    private LinearLayout agentWebLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_vipplayer_acitivity);
        //Log.d("Vipplayer","VipplayerAcitivity已经打开准备接收数据：");
        Intent intent = getIntent();
        String Extra_url = intent.getStringExtra("Extra_url");
        //Log.d("Vipplayer","选择打开的视频源是："+ Extra_url);
        initView();//初始化视窗
        //加载播放器选项
        spinner = (Spinner) findViewById(R.id.spinner);
        select_source = getResources().getStringArray(R.array.source_array);
        /* ArrayAdapter构造参数说明：
         * context:上下文对象
         * resource:表示列表item的布局资源id
         * objects:表示加载的数据源
         */
        adapter = new ArrayAdapter<String>(VipplayerAcitivity.this, android.R.layout.simple_spinner_dropdown_item, select_source);
        spinner.setAdapter(adapter);
        //设置当spinner控件中的item被选中时触发的监听事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*表示当spinner控件中的item被选中时的回调方法
             * AdapterView<?> parent：表示当前触发事件的适配器控件对象spinner
             * View view:表示当前被选中的item的对象
             * int position：表示被选中的item的下标
             * long id：表示当前被选中的item的id
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //String s1 = select_source[position];//在数据源中获取
                //String s2 = adapter.getItem(position);//根据下标在适配器中获取
                String s3 = spinner.getItemAtPosition(position).toString();//根据下标在spinner中获取
                url_start_id =String.valueOf(position);
                //Toast.makeText(VipplayerAcitivity.this, "当前选择的播放器是："+s3, Toast.LENGTH_SHORT).show();
            }
            //当没有任何选中时回调的方法
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
        //.setWebLayout(new WebLayout(this))
        //打开其他应用时，弹窗咨询用户是否前往其他应用
        //拦截找不到相关页面的Scheme
        AgentWeb mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(agentWebLL, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                //.setAgentWebWebSettings(getSettings())//设置
                .setWebChromeClient(webChromeClient)
                .setWebViewClient(webViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                //.setWebLayout(new WebLayout(this))
                //.setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(Extra_url);
       // initAgentWeb();//初始化Agentweb
        Btn_VIP= findViewById(R.id.btn_vip);
        Btn_VIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VipplayerAcitivity.this,VIP_Acitivity.class);
                //Log.d("Test","准备启动VIP_Acitivity_2");
                //Log.d("Test","需要VIP打开的页面是："+url_end);
                //url_start_id="http://cdn.yangju.vip/k/?url=";
                intent.putExtra("Extra_Vip_start_id",url_start_id);
                intent.putExtra("Extra_Vip_end",url_end);
                //Log.d("Test","准备传送到下一个Acitivity的URL为："+url_vip);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        agentWebLL = findViewById(R.id.agentWeb);
        Log.d("Vipplayer","页面初始化");
    }
    /***初始化Agentweb函数
    private void initAgentWeb() {
        mAgentWeb = AgentWeb.with(this)
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
                .go(TARGET_URL);

    }
     **/

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
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d("Test","获取到的新地址为："+request.getUrl());
            //url_end = request.getUrl().toString();
            return super.shouldOverrideUrlLoading(view, request);

        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("Test","当前打开的URL地址为："+url);
            url_end=url;
        }

    };
    }

