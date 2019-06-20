package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.net.URL;
import java.util.Scanner;

/**
 * Created by RAJ on 1/25/2018.
 */

public class UpdateCheck {
    public static long new_ver;
    public static long old_ver;
    Context context;
    String updateUrl = "http://queerdevs.in/pune_local_database/version.txt";

    class UpdateThread extends Thread {
        int data_new_ver;

        UpdateThread() {
        }

        public void run() {
            super.run();
            synchronized (this) {
                try {
                    this.data_new_ver = new Scanner(new URL(UpdateCheck.this.updateUrl).openStream()).nextInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notify();
            }
        }
    }

    UpdateCheck(Context context) {
        this.context = context;
        checkUpdate();
    }

    public void checkUpdate() {
        try {
            Cursor cursor = new MySQLiteOpenHelper(this.context).getReadableDatabase().rawQuery("select dbver from db_information;", null);
            if (cursor != null && cursor.moveToFirst()) {
                old_ver = (long) cursor.getInt(0);
                cursor.close();
                Log.d("OldUp", "" + old_ver);
            }
        } catch (Exception e) {
        }
    }

    public void getUpdateAvail() {
        final UpdateThread updateThread = new UpdateThread();
        new Thread(new Runnable() {
            public void run() {
                updateThread.start();
                synchronized (updateThread) {
                    try {
                        updateThread.wait();
                    } catch (InterruptedException e) {
                    }
                }
                final UpdateActivity downloadActivity = (UpdateActivity) UpdateCheck.this.context;
                if (UpdateCheck.old_ver < ((long) updateThread.data_new_ver)) {
                    Log.d("RAJ", "True" + updateThread.data_new_ver);
                    downloadActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            downloadActivity.ShowDownloadDialog();
                        }
                    });
                    return;
                }
                Log.d("RAJ", "False");
                downloadActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        downloadActivity.ShowUpdatedDialog();
                    }
                });
            }
        }).start();
    }
}
