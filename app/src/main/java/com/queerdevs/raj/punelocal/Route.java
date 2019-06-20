package com.queerdevs.raj.punelocal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by RAJ on 1/22/2018.
 */

public class Route extends AppCompatActivity {
    Button FindRoute;
    MySQLiteOpenHelper helper;
    Intent i;
    EditText train_number;
    public ArrayList<TrainRoute> trainsRoutes;
    Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Route Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.train_number = (EditText) findViewById(R.id.train_number);
        this.FindRoute = (Button) findViewById(R.id.FindRoute);
        this.FindRoute.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Route.this.train_number.getText().toString().equals("")) {
                    Route.this.train_number.startAnimation(AnimationUtils.loadAnimation(Route.this, R.anim.shake));
                    Toast.makeText(Route.this, "Please Enter Train Number", Toast.LENGTH_LONG).show();
                    return;
                }
                new findRoute().execute(new String[]{Route.this.train_number.getText().toString()});
            }
        });
    }

    public class findRoute extends AsyncTask<String, Void, ArrayList<TrainRoute>> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected ArrayList<TrainRoute> doInBackground(String... stations) {
            String trainNumber = stations[0];
            Log.d("VALUE", trainNumber);
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(Route.this);
            Route.this.trainsRoutes = helper.getRoute(trainNumber);
            return Route.this.trainsRoutes;
        }

        protected void onPostExecute(ArrayList<TrainRoute> routes) {
            super.onPostExecute(routes);
            Log.d("SIZE", "" + routes.size());
            if (routes.size() > 0) {
                Route.this.i = new Intent(Route.this, DisplayTrainRoute.class);
                Route.this.i.putExtra("trains", Route.this.trainsRoutes);
                Route.this.startActivity(Route.this.i);
                return;
            }
            Toast.makeText(Route.this, "Train Not Available", Toast.LENGTH_LONG).show();
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
