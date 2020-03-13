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

import androidx.appcompat.app.AppCompatActivity;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

import java.util.Objects;

public class VipplayerAcitivity extends AppCompatActivity {
    public String url_start_id;    //声明变量用于存储选取的解析id
    public String url_end;         //声明变量用于存储需要解析的地址
    private LinearLayout agentWebLL; //用来承载AgentWebView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_vipplayer_acitivity);
       //接收Intent传递过来的数据
        Intent intent = getIntent();
        String Extra_url = intent.getStringExtra("Extra_url");
        //初始化视窗
        initView();
        //初始化Agentweb
        initAgentWeb(Extra_url);
        //加载播放器选项
        //用于承载下拉选择播放器选项
        Spinner spinner = findViewById(R.id.spinner);
        //声明变量用于存储选择的播放器菜单项
        String[] select_source = getResources().getStringArray(R.array.source_array);
        /* ArrayAdapter构造参数说明：
         * context:上下文对象
         * resource:表示列表item的布局资源id
         * objects:表示加载的数据源
         */
        //声明变量用于存播放器选单
        ArrayAdapter<String> adapter = new ArrayAdapter<>(VipplayerAcitivity.this, android.R.layout.simple_spinner_dropdown_item, select_source);
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
                //String s3 = spinner.getItemAtPosition(position).toString();//根据下标在spinner中获取
                url_start_id =String.valueOf(position);
                //Toast.makeText(VipplayerAcitivity.this, "当前选择的播放器是："+s3, Toast.LENGTH_SHORT).show();
            }
            //当没有任何选中时回调的方法
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //声明VIP播放按钮,处理点击事件
        Button btn_VIP = findViewById(R.id.btn_vip);
        //VIP按钮监听事件
        btn_VIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VipplayerAcitivity.this,VIP_Acitivity.class);
                intent.putExtra("Extra_Vip_start_id",url_start_id);
                intent.putExtra("Extra_Vip_end",url_end);
                //Log.d("Test","准备传送到下一个Acitivity的URL为："+url_vip);
                startActivity(intent); }
        });
    }

    //初始化视窗函数initView();
    private void initView() {
        agentWebLL = findViewById(R.id.agentWeb);
        Log.d("Vipplayer","页面初始化");
    }

    //初始化Agentweb函数initAgentWeb(String url);url为要加载地址;
    private void initAgentWeb(String url) {
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
                .go(url);
    }
    // 重新防止跳转其它浏览器
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //获取标题,设置提醒
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // 设置接收到的 title
            //if(receiveTitle != null){
            //    receiveTitle.setText(title);
            // }
        }
    };
    //重写方法
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

