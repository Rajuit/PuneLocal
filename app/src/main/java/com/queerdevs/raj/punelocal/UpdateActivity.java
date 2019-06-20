package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

public class UpdateActivity extends AppCompatActivity {
    AnimatedCircleLoadingView animatedCircleLoadingView;
    ImageButton imageButton;
    ImageView imageView;
    TextView textView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        this.animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        this.imageButton = (ImageButton) findViewById(R.id.download1);
        this.imageView = (ImageView) findViewById(R.id.imgview);
        this.textView = (TextView) findViewById(R.id.check);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Database Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.imageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new UpdateCheck(UpdateActivity.this).getUpdateAvail();
                UpdateActivity.this.imageView.getDisplay();
            }
        });
        this.textView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UpdateActivity.this.imageButton.performClick();
            }
        });
    }

    public void ShowDownloadDialog() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage("Database Update Available!").setCancelable(false);
        alertDialogBuilder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new DownloadDatabase(UpdateActivity.this).execute(new Void[0]);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    public void ShowUpdatedDialog() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage("You already have latest Database.").setCancelable(false);
        alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UpdateActivity.this.finish();
            }
        });
        alertDialogBuilder.create().show();
    }


    private class DownloadDatabase extends DownloadManager {
        private DownloadDatabase(Context context) {
            super(context);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            UpdateActivity.this.animatedCircleLoadingView.startDeterminate();
            // UpdateActivity.this.imageView.setImageResource(R.drawable.download_database);
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            UpdateActivity.this.animatedCircleLoadingView.setPercent(Integer.parseInt(values[0]));
            UpdateActivity.this.animatedCircleLoadingView.stopOk();
        }

        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
