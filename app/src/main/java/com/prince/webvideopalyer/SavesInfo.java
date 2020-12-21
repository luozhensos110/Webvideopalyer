package com.prince.webvideopalyer;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;
//保存信息类
class SavesInfo {

    //保存更新地址
    static void  SaveUrl(Context context, String checkurl){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=context.getSharedPreferences("config", MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("Checkurl", checkurl);
        //提交
        editor.apply();
    }
    //保存注册状态
    static void remAccount(Context context, Boolean iSrem){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=context.getSharedPreferences("config", MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putBoolean("isRegister", iSrem);
        //提交
        editor.apply();
    }
    //保存暗码启动开关状态
    static void remIscode_start(Context context, Boolean iScode_start){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=context.getSharedPreferences("config", MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putBoolean("isCode_start", iScode_start);
        //提交
        editor.apply();
    }
    //保存启动密码
    static void  SaveCode(Context context, String startCode){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=context.getSharedPreferences("config", MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("startCode", startCode);
        //提交
        editor.apply();
    }
}
