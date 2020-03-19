package com.prince.webvideopalyer;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
//注册页面
public class RegisterAcitivity extends AppCompatActivity {
    private EditText editText_pwd;                //定义控件
    private TextView textView_PhoneSn;            //定义控件
    private String PhoneSn;                       //定义变量用于存储手机识别码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);
        PhoneSn = getDeviceSN();
        //Log.d("REG",PhoneSn);
        editText_pwd=findViewById(R.id.editText_register);
        textView_PhoneSn=findViewById(R.id.textView_phonesn);
        //定义控件
        Button btn_GetPhoneSn = findViewById(R.id.button_getphonesn);
        //定义控件
        Button btn_Register = findViewById(R.id.button_register);
        //注册按键监听
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String key = Set_Key(PhoneSn);
                String pwd = editText_pwd.getText().toString().trim();
                if(pwd.equals(key)){
                    SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                    editor.putBoolean("isRegister", true);
                    editor.apply();
                    SharedPreferences preferences = getSharedPreferences("config",MODE_PRIVATE);
                    boolean str1 = preferences.getBoolean("isRegister",false);
                    Toast.makeText(RegisterAcitivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                    //Intent intent_register=new Intent(RegisterAcitivity.this,VipplayerAcitivity.class);
                    //startActivity(intent_register);

                }else{
                    Toast.makeText(RegisterAcitivity.this,"注册码错误,请重新输入",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //获取序列号按键监听
        btn_GetPhoneSn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_PhoneSn.setText(PhoneSn);
                //获取剪贴版
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                //第一个参数只是一个标记，随便传入。
                //第二个参数是要复制到剪贴版的内容
                ClipData clip = ClipData.newPlainText("PhoneSn", PhoneSn);
                //传入clipdata对象.
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(RegisterAcitivity.this,"序列号已经复制到剪贴板",Toast.LENGTH_SHORT).show();
            }
        }
        );
    }
    //加密方法Set_Key（Phone_sn）
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String Set_Key(String Phone_sn){
        String desc = Base64.getEncoder().encodeToString(Phone_sn.getBytes(StandardCharsets.UTF_8));
        System.out.println("加密后的字符串为:"+desc);
        return desc;
    }
    //生成设备唯一标识符
    public static String getDeviceSN() {
        String serialNumber = "3501" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return serialNumber;
    }
    /**获取设备序列号,用于注册(有BUG 部分设备返回为unknown)
    public static String getDeviceSN(){

        @SuppressLint("HardwareIds")
        String serialNumber = android.os.Build.SERIAL;

        return serialNumber;
    }
     **/
}
