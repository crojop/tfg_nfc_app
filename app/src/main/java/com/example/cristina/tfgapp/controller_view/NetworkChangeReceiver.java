package com.example.cristina.tfgapp.controller_view;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import java.util.List;

/**
 * Created by Cristina on 13/03/18.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        //Sólo si la aplicación está en primer plano
        if (isAppForground(context)) {
            if (!Utils.isConnected(context)) {
                MyToastSingleton.getInstance(context).setError(context.getString(R.string.network_lost_connexion));
                Intent int_go_main = new Intent(context.getApplicationContext(), LoginActivity.class);
                int_go_main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(int_go_main);
            }
            else {
                MyToastSingleton.getInstance(context).setSuccess(context.getString(R.string.network_recovered_connexion));
            }
        }
    }

    public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }
}
