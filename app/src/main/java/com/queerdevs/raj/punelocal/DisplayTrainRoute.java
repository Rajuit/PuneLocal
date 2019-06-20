package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RAJ on 1/24/2018.
 */

public class DisplayTrainRoute extends AppCompatActivity {
    boolean flag = false;
    RecyclerView recyclerView1;
    TextView textView;
    ArrayList<TrainRoute> trainsRoutes=new ArrayList<TrainRoute>();
    Toolbar toolbar;
    String source="";
    String destination="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_train_route);
       // textView = (TextView) findViewById(R.id.ShowTrainName);
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
        trainsRoutes = (ArrayList<TrainRoute>) getIntent().getSerializableExtra("trains");
        this.flag = getIntent().getBooleanExtra("flag", false);
        Intent intent1=getIntent();
        source=intent1.getStringExtra("srcName");
        destination=intent1.getStringExtra("dest");

        Log.d("SIZEVALUEEEE", "" + this.trainsRoutes.size());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Train Route");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerViewAdapterTrainRoute mAdapter = new RecyclerViewAdapterTrainRoute(DisplayTrainRoute.this, this.trainsRoutes, this.flag);
        this.recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView1.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView1.setAdapter(mAdapter);
    }

    private class RecyclerViewAdapterTrainRoute extends RecyclerView.Adapter<DisplayTrainRoute.RecyclerViewAdapterTrainRoute.MyViewHolder> {
        Context context;
        boolean flag;
        ArrayList<TrainRoute> trainsRoutes;

         class MyViewHolder extends RecyclerView.ViewHolder {
            TextView Station_Name;
            TextView Station_code;
            TextView Time;
            ImageView imageView;

            public MyViewHolder(View V) {
                super(V);
                this.Station_Name = (TextView) V.findViewById(R.id.StationName);
                this.Time = (TextView) V.findViewById(R.id.Time);
              //  this.Station_code = (TextView) V.findViewById(R.id.StationCode);
                this.imageView = (ImageView) V.findViewById(R.id.imageView);
            }
        }

        private RecyclerViewAdapterTrainRoute(Context context, ArrayList<TrainRoute> trainsRoutes, boolean flag) {
            this.context = context;
            this.trainsRoutes = trainsRoutes;
            this.flag = flag;
        }

        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.train_route, parent, false));
        }

        public void onBindViewHolder(MyViewHolder holder, int position) {
            TrainRoute train = (TrainRoute) this.trainsRoutes.get(position);
            TextView textView = holder.Station_Name;
            Share share = new Share();

            String station = train.getName();
            String code=train.getCode();
            textView.setText(station+" - "+code);
            holder.Time.setText(train.getTime());
            if(position %2 == 1)
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
            }
            //holder.Station_code.setText(train.getCode());
            if (this.flag && source.equals(station)) {
                holder.itemView.setBackgroundResource(R.drawable.back);
                holder.imageView.setBackgroundResource(R.drawable.mapgreen);
            } else if (this.flag && destination.equals(station)) {
                holder.itemView.setBackgroundResource(R.drawable.back);
                holder.imageView.setBackgroundResource(R.drawable.mapred);
            } else {
                holder.imageView.setBackgroundResource(R.drawable.mapblack);
                holder.itemView.setBackgroundResource(R.drawable.back);
            }



        }

        public int getItemCount() {
            return this.trainsRoutes.size();
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
