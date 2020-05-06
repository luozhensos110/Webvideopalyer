package com.prince.webvideopalyer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.provider.Telephony.Sms.Intents.SECRET_CODE_ACTION;

    public class SecretCodeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && SECRET_CODE_ACTION.equals(intent.getAction())){

            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("iScode_start",true);
            Log.d("暗码启动","接收到暗码");
            context.startActivity(i);
        }
    }

}
