package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.v7.app.AlertDialog.Builder;
/**
 * Created by RAJ on 1/23/2018.
 */

public class NetConnectivity {
    Context context;

    NetConnectivity(Context context) {
        this.context = context;
    }

    public Boolean checkNet() {
        Boolean iconnection = Boolean.valueOf(false);
        NetworkInfo[] info = ((ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo();
        int i = 0;
        while (i < info.length) {
            if (info[i].getState() == NetworkInfo.State.DISCONNECTED || info[i].getState() == NetworkInfo.State.SUSPENDED) {
                iconnection = Boolean.valueOf(false);
            }
            i++;
        }
        for (NetworkInfo state : info) {
            if (state.getState() == State.CONNECTED) {
                iconnection = Boolean.valueOf(true);
            }
        }
        if (!iconnection.booleanValue()) {
            ShowInternetDialog();
        }
        return iconnection;
    }

    public void ShowInternetDialog() {
        Builder alertDialogBuilder = new Builder(this.context);
        alertDialogBuilder.setMessage((CharSequence) "Oops...Check Your Internet Connection!").setCancelable(false);
        alertDialogBuilder.setNegativeButton((CharSequence) "OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }
}

