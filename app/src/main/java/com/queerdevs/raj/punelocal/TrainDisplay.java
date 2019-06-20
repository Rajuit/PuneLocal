package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RAJ on 1/23/2018.
 */

public class TrainDisplay extends AppCompatActivity {
    String destination = "";
    TextView header;
    // MyRecyclerViewAdapter mAdapter=new MyRecyclerViewAdapter();
    RecyclerView recyclerView;
    String source = "";
    TextView textView;
    ArrayList trains = new ArrayList<Train>();
    Toolbar toolbar;
    Share share = new Share();
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_details);
        context = this;
        // this.textView = (TextView) findViewById(R.id.header);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //this.header = (TextView) findViewById(R.id.header);

        trains = (ArrayList) getIntent().getSerializableExtra("trains");


        /*source = share.getSourceStation();
        destination = share.getDestStation();*/
        Log.d("VALUE", "AGAINSIZE" + this.trains.size());
        Intent intent1 = getIntent();
        source = intent1.getStringExtra("Source");
        destination = intent1.getStringExtra("Destination");


        Log.d("SOURCE", source);
        Log.d("DEST", destination);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(source + " To " + destination);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter(TrainDisplay.this, this.trains, this.recyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(mAdapter);


        // this.header.setText("Trains From " + this.source + " to " + this.destination + "");
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<TrainDisplay.MyRecyclerViewAdapter.MyViewHolder> {
        private  final Context context;
       // private  ArrayList<Train> trains=null;
        private  ArrayList<Train> trains = new ArrayList<Train>();
         MyClickListener myClickListener = new MyClickListener();
        RecyclerView recyclerView;

        class MyClickListener implements View.OnClickListener {
            MyClickListener() {
            }

            public void onClick(View v) {
                Train train =  trains.get(MyRecyclerViewAdapter.this.recyclerView.getChildLayoutPosition(v));
                new findRoute().execute(train.getTrainKey());
            }
        }


        MyRecyclerViewAdapter(Context context, ArrayList<Train> trains, RecyclerView recyclerView) {
            this.trains = trains;
            this.context = context;
            this.recyclerView = recyclerView;
        }


        @Override

        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_train_detail, parent, false);
              itemView.setOnClickListener(this.myClickListener);
            return new MyViewHolder(itemView);
        }

        @Override

        public void onBindViewHolder(MyViewHolder holder, int position) {
            Train train = (Train) trains.get(position);
            holder.trainKey.setText(train.getTrainKey());
            holder.start.setText(train.getStartst());
            holder.dest.setText(train.getDestst());
            holder.speed.setText(train.getSpeed());
            holder.cars.setText(train.getCars());
            holder.arrival.setText("Arr : " + train.getArrivalTime());
            holder.departure.setText("Dep : " + train.getDepartTime());
        }

        @Override
        public int getItemCount() {
            return trains.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView arrival;
            TextView cars;
            TextView departure;
            TextView dest;
            TextView speed;
            TextView start;
            TextView trainKey;

            public MyViewHolder(View v) {
                super(v);
                this.trainKey = (TextView) v.findViewById(R.id.trainKey);
                this.start = (TextView) v.findViewById(R.id.start);
                this.dest = (TextView) v.findViewById(R.id.dest);
                this.cars = (TextView) v.findViewById(R.id.cars);
                this.speed = (TextView) v.findViewById(R.id.speed);
                this.arrival = (TextView) v.findViewById(R.id.arrival);
                this.departure = (TextView) v.findViewById(R.id.departure);
            }
        }


        public  class findRoute extends AsyncTask<String, Void, Void> {
            public ArrayList<TrainRoute> trainsRoutes;

            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected Void doInBackground(String... stations) {
                String trainNumber = stations[0];
                Log.d("VALUE", trainNumber);
                trainsRoutes = new MySQLiteOpenHelper(TrainDisplay.this).getRoute(trainNumber);
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent i = new Intent(TrainDisplay.this, DisplayTrainRoute.class);
                i.putExtra("trains", trainsRoutes);
                i.putExtra("srcName",source);
                i.putExtra("dest",destination);
                i.putExtra("flag", true);
                startActivity(i);
            }
        }
    }
}
