package com.queerdevs.raj.punelocal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String destName;
    public static String sourceName;
    CustomAutoCompleteView Dest;
    Button Find;
    CheckBox Next;
    CustomAutoCompleteView Source;
    boolean checkClick;
    EditText date;
    DatePickerDialog datepicker;
    TextView delete;
    TextView delete1;
    TextView destText;
    Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawerLayout;
    ImageButton imageButton;
    public String[] item = new String[]{"Please Search...."};
    public ArrayAdapter myAdapter;
    MySQLiteOpenHelper mySQLiteOpenHelper;
    NavigationView navigationView;
    TextView sourceText;
    public ArrayList<Train> trains;
    Share share=new Share();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date = (EditText) findViewById(R.id.Date);
        destText=(TextView)findViewById(R.id.destText);
        sourceText=(TextView)findViewById(R.id.sourceText);

        Next = (CheckBox) findViewById(R.id.Next);
        Source = (CustomAutoCompleteView) findViewById(R.id.Source);
        Source.addTextChangedListener(new MyTextWatcher(this,1));
        this.mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        this.delete = (TextView) findViewById(R.id.delete);
        this.delete1 = (TextView) findViewById(R.id.delete1);
        if (!this.mySQLiteOpenHelper.checkDataBase()) {
            notifyDBDownload();
        }
        this.Dest = (CustomAutoCompleteView) findViewById(R.id.dest);
        this.Dest.addTextChangedListener(new MyTextWatcher(this, 2));

        this.Find = (Button) findViewById(R.id.Find);
        this.imageButton = (ImageButton) findViewById(R.id.imageButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        setDefaultDate();
        this.date.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                MainActivity.this.datepicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        MainActivity.this.date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                MainActivity.this.datepicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                MainActivity.this.datepicker.show();
            }
        });
        this.Find.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    MainActivity.sourceName = MainActivity.this.Source.getText().toString().trim();
                    MainActivity.destName = MainActivity.this.Dest.getText().toString().trim();
                    share.setSourceStation(MainActivity.sourceName);
                    share.setDestStation(MainActivity.destName);
                    if (MainActivity.sourceName.equals("") && MainActivity.destName.equals("")) {
                        Toast.makeText(MainActivity.this,"Both source & Destination is empty",Toast.LENGTH_LONG).show();

                        MainActivity.this.sourceText.setText("");
                        MainActivity.this.destText.setText("");
                        MainActivity.this.shakeSource();
                        MainActivity.this.shakeDestination();
                    } else if (MainActivity.sourceName.equals("")) {
                        Toast.makeText(MainActivity.this,"Source Station Incorrect!",Toast.LENGTH_LONG).show();
                        MainActivity.this.destText.setText("");

                        Animation   shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake);
                        MainActivity.this.sourceText.setText("Source Station Incorrect!");
                        MainActivity.this.Source.startAnimation(shake);
                    } else if (MainActivity.destName.equals("")) {
                        Toast.makeText(MainActivity.this,"Destination Station Incorrect !",Toast.LENGTH_LONG).show();


                        MainActivity.this.sourceText.setText("");
                       Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                        MainActivity.this.destText.setText("Destination Station Incorrect!");
                        MainActivity.this.Dest.startAnimation(shake);
                    } else if (!MainActivity.sourceName.equals(MainActivity.destName) || MainActivity.destName.equals("")) {
                       // MainActivity.this.sourceText.setText("");
                       // MainActivity.this.destText.setText("");
                        new SearchTrain().execute(MainActivity.sourceName, MainActivity.destName);
                    } else {
                      Animation  shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);

                        MainActivity.this.sourceText.setText("source and Destination are same!");
                        MainActivity.this.destText.setText("Source and Destination are same!");
                        MainActivity.this.Source.startAnimation(shake);
                        MainActivity.this.Dest.startAnimation(shake);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.Next.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.checkClick = MainActivity.this.Next.isChecked();
            }
        });
        this.imageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String s1 = MainActivity.this.Source.getText().toString();
                String s2 = MainActivity.this.Dest.getText().toString();
                MainActivity.this.Dest.setText(s1);
                MainActivity.this.Source.setText(s2);
            }
        });
        this.delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.Source.setText("");
            }
        });
        this.delete1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.Dest.setText("");
            }
        });

        View header = navigationView.getHeaderView(0);



        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.inbox:

                        Intent contactus = new Intent(MainActivity.this, ContactUs.class);
                        startActivity(contactus);


                        return true;
                    case R.id.Home:

                        Intent home = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(home);


                        return true;
                    case R.id.MoreApps:
                        String urls = "https://play.google.com/store/apps/developer?id=Queer%20Developers&hl=en";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls));
                        startActivity(intent);
                        // Toast.makeText(getApplicationContext(), "Rate Us Selected", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.AboutUs:

                        Intent aboutus = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(aboutus);
                        return true;
                    case R.id.rate:
                        String url = "http://play.google.com/store/apps/details?id=com.queerdevs.raj.punelocal";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        // Toast.makeText(getApplicationContext(), "Rate Us Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.Map:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, Map.class));
                        MainActivity.this.drawerLayout.closeDrawers();
                        break;
                    case R.id.Route:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, Route.class));
                        MainActivity.this.drawerLayout.closeDrawers();
                        break;
                    case R.id.Update:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, UpdateActivity.class));
                        MainActivity.this.drawerLayout.closeDrawers();
                        break;
                    case R.id.share:
                        String share = "Download Pune Local  app for your all  search for  Pune Local Train , for Next train from your Station , Route Map of Local Train running in Pune - to - Lonavala.  This app is  Offline App .http://play.google.com/store/apps/details?id=com.queerdevs.raj.punelocal";
                        Intent sharing = new Intent(Intent.ACTION_SEND);
                        sharing.setType("text/plain");
                        sharing.putExtra(Intent.EXTRA_SUBJECT, "Pune Local").putExtra(Intent.EXTRA_TEXT, share);
                        startActivity(Intent.createChooser(sharing, "Share With"));
                        // Toast.makeText(getApplicationContext(), "Share Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }

        });

        // Initializing Drawer Layout and ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();




    }

        private class SearchTrain extends AsyncTask<String, Void, Void> {
            SearchTrain() {
            }

            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected Void doInBackground(String... stations) {
                String sourceName = stations[0];
                String destName = stations[1];
                MySQLiteOpenHelper helper = new MySQLiteOpenHelper(MainActivity.this);
                MainActivity.this.trains = helper.getTrains(sourceName, destName, MainActivity.this.checkClick);
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (MainActivity.this.trains.size() > 0) {
                    Intent intent = new Intent(MainActivity.this, TrainDisplay.class);
                    intent.putExtra("trains", MainActivity.this.trains);
                    intent.putExtra("Source",sourceName);
                    intent.putExtra("Destination",destName);
                    MainActivity.this.startActivity(intent);

                    return;
                }
                Toast.makeText(MainActivity.this, "Please Enter valid Station Name!", Toast.LENGTH_LONG).show();
            }
        }


    private void setDefaultDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        this.date.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (mMonth + 1) + "/" + mYear);
    }

   /* public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.toggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.download:
                startActivity(new Intent(this, UpdateActivity.class));
                return true;
            case R.id.share:
                Intent sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra("android.intent.extra.SUBJECT", "Pune Local Trains");
                sharingIntent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=bhabua.manish.mumbairail");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public static void deleteCache(Context context) {
        try {
            deleteDir(context.getCacheDir());
        } catch (Exception e) {
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir == null || !dir.isFile()) {
            return false;
        } else {
            return dir.delete();
        }
    }

    public String[] getDataFromDB(String s) {
        String[] item;
        int x;
        this.mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        List<String> output = this.mySQLiteOpenHelper.getData(s);
        int rowCount = output.size();
        if (rowCount == 0) {
            item = new String[rowCount];
            x = 0;
        } else {
            item = new String[rowCount];
            x = 0;
        }
        for (String record : output) {
            item[x] = record;
            x++;
        }
        return item;
    }

    public void notifyDBDownload() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage( "Download Database for First Time .\n It is less than 1 MB!\nClick on \"Download\"").setCancelable(false);
        alertDialogBuilder.setPositiveButton( "Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (new NetConnectivity(MainActivity.this).checkNet().booleanValue()) {
                    new DownloadManager(MainActivity.this).execute(new Void[0]);
                }
            }
        }).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.finish();
                System.exit(0);
            }
        });
        alertDialogBuilder.create().show();
    }

    public void shakeSource() {
        this.Source.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        this.sourceText.setText("Source Station Incorrect!");

    }

    public void shakeDestination() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        this.destText.setText("Destination Station Incorrect!");
        this.Dest.startAnimation(shake);
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast toast = Toast.makeText(this, " Please click BACK again to exit ", Toast.LENGTH_SHORT);
        View vieew = toast.getView();
        //  vieew.setBackgroundColor(Color.parseColor("#BD8BDC"));
        vieew.setBackgroundResource(R.drawable.toast);
        toast.setView(vieew);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}