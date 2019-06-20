package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.kaopiz.kprogresshud.KProgressHUD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by RAJ on 1/23/2018.
 */

public class DownloadManager extends AsyncTask<Void, String, Boolean> {
    private static String DATABASE_NAME = "database";
    private static String DATABASE_PATH = "";
    Context context;
    String downloadUrl = "http://queerdevs.in/pune_local_database/punelocal.zip";
    private KProgressHUD hud;

    public DownloadManager(Context context) {
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).toString();
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.hud = KProgressHUD.create(this.context).setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE).setLabel("DataBase is Downloading...").setMaxProgress(100);
        this.hud.show();
    }

    protected Boolean doInBackground(Void... params) {
        MalformedURLException e;
        ZipInputStream zipInputStream;
        FileNotFoundException e2;
        IOException e3;
        Exception e4;
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(DATABASE_PATH));
            FileOutputStream fileOutputStream;
            try {
                HttpURLConnection conection = (HttpURLConnection) new URL(this.downloadUrl).openConnection();
                conection.connect();
                ZipInputStream zis = new ZipInputStream(conection.getInputStream());
                try {
                    byte[] Buffer = new byte[1024];
                    long total = 0;
                    while (true) {
                        ZipEntry zipEntry = zis.getNextEntry();
                        if (zipEntry == null) {
                            break;
                        }
                        do {
                            int count = zis.read(Buffer);
                            if (count == -1) {
                                break;
                            }
                            total += (long) count;
                            long totalSize = zipEntry.getSize();
                            publishProgress(new String[]{"" + ((int) ((100 * total) / totalSize))});
                            outputStream.write(Buffer, 0, count);
                        } while (!isCancelled());
                    }
                    outputStream.flush();
                    outputStream.close();
                    zis.close();
                    fileOutputStream = outputStream;
                } catch (MalformedURLException e5) {
                    e = e5;
                    fileOutputStream = outputStream;
                    zipInputStream = zis;
                    e.printStackTrace();
                    return null;
                } catch (FileNotFoundException e6) {
                    e2 = e6;
                    fileOutputStream = outputStream;
                    zipInputStream = zis;
                    e2.printStackTrace();
                    return null;
                } catch (IOException e7) {
                    e3 = e7;
                    fileOutputStream = outputStream;
                    zipInputStream = zis;
                    e3.printStackTrace();
                    return null;
                } catch (Exception e8) {
                    e4 = e8;
                    fileOutputStream = outputStream;
                    zipInputStream = zis;
                    e4.printStackTrace();
                    return null;
                }
            } catch (MalformedURLException e9) {
                e = e9;
                fileOutputStream = outputStream;
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e10) {
                e2 = e10;
                fileOutputStream = outputStream;
                e2.printStackTrace();
                return null;
            } catch (IOException e11) {
                e3 = e11;
                fileOutputStream = outputStream;
                e3.printStackTrace();
                return null;
            } catch (Exception e12) {
                e4 = e12;
                fileOutputStream = outputStream;
                e4.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException e14) {
            e2 = e14;
            e2.printStackTrace();
            return null;
        } catch (IOException e15) {
            e3 = e15;
            e3.printStackTrace();
            return null;
        } catch (Exception e16) {
            e4 = e16;
            e4.printStackTrace();
            return null;
        }
        return null;
    }


    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        this.hud.setProgress(Integer.parseInt(values[0]));
        if (Integer.parseInt(values[0]) == 100) {
            Toast toast = new Toast(this.context);
            toast.setGravity(17, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            View v = ((LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast, null);
            ((TextView) v.findViewById(R.id.toast_message)).setText("Download Successful!!");
            toast.setView(v);
            this.hud.dismiss();
            toast.show();
        }
    }

    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
