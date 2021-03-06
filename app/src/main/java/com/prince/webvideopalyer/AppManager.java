package com.prince.webvideopalyer;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import java.util.Stack;
//App管理类
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;
    private AppManager(){}

    //单一实例
    static AppManager getAppManager(){
        if(instance==null){
            instance=new AppManager();
        }
        return instance;
    }

    //添加Activity到堆栈
    void addActivity(Activity activity){
        if(activityStack==null){
            activityStack= new Stack<>();
        }
        activityStack.add(activity);
    }

    //获取当前Activity（堆栈中最后一个压入的）
    public Activity currentActivity(){
        if(activityStack.isEmpty()){
            return null;
        }else{
            return activityStack.lastElement();
        }
    }

    //结束当前Activity（堆栈中最后一个压入的）
    public void finishActivity(){
        Activity activity=activityStack.lastElement();
        if(activity!=null){
            activity.finish();
            //activity=null;
        }
    }

    //结束指定的Activity
    private void finishActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            //activity=null;
        }
    }

    //结束指定类名的Activity
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                finishActivity(activity);
            }
        }
    }

    //结束所有Activity
    void finishAllActivity(){
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    //退出应用程序
    void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert activityMgr != null;
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception ignored) { }
    }

}
