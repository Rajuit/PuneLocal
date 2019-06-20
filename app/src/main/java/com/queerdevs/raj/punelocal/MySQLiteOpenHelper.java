package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Created by RAJ on 1/22/2018.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database";
    public static String DATABASE_PATH = "";
    public static final int DATABASE_VERSION = 1;
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.myContext = context;
        DATABASE_PATH = this.myContext.getDatabasePath(DATABASE_NAME).toString();
    }

    public boolean checkDataBase() {
        boolean z;
        int db_ver;
        SQLiteDatabase sQLiteDatabase = null;
        try {
            sQLiteDatabase = getReadableDatabase();
           sQLiteDatabase.rawQuery("select dbver from db_information", null);
           /* db_ver= sQLiteDatabase.getVersion();
            Log.d("DBVER",String.valueOf(db_ver));*/
            z = true;

        } catch (Exception e) {
            z = false;
        } finally {
            sQLiteDatabase.close();
        }
        return z;
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = this.myContext.getAssets().open(DATABASE_NAME);
        OutputStream mOutput = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);
        byte[] mBuffer = new byte[2024];
        while (true) {
            int mLength = mInput.read(mBuffer);
            if (mLength > 0) {
                mOutput.write(mBuffer, 0, mLength);
            } else {
                mOutput.flush();
                mOutput.close();
                mInput.close();
                return;
            }
        }
    }

    public void db_delete() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    public void openDatabase() throws SQLException {
        this.myDataBase = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, 0);
    }

    public synchronized void closeDataBase() throws SQLException {
        if (this.myDataBase != null) {
            this.myDataBase.close();
        }
        super.close();
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }

    public List<String> getData(String s) {
        SQLiteDatabase database = getReadableDatabase();
        List<String> strings = new ArrayList();
        Cursor cursor = database.rawQuery("select * from crstations where name like '" + s + "%' or code like '" + s + "%' limit 15;", null);
        if (cursor.moveToFirst()) {
            do {
                String s1 = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("SUCCESS", s1);
                strings.add(s1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return strings;
    }

    public boolean checkUpDown(String sourceName, String destName) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from(select _id from crstations where name='" + sourceName + "')a inner join (select _id from crstations where name='')b on a._id>b._id", null);
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        database.close();
        return false;
    }

    public ArrayList<Train> getTrains(String sourceName, String destName, boolean checkClick) {
        boolean isup;
        int count=0;
        String qry;
        String sourceCode = null;
        String destCode = null;
        ArrayList<Train> trains = new ArrayList<Train>();
        SQLiteDatabase database = getReadableDatabase();
        Calendar currentTime = Calendar.getInstance();
        int time = (currentTime.get(Calendar.HOUR_OF_DAY) * 60) + currentTime.get(Calendar.MINUTE);
        Log.d("VALUE", "TIME" + time);
        if (database.rawQuery("select * from(select _id from crstations where name='" + sourceName + "')a inner join (select _id from crstations where name='" + destName + "')b on cast(a._id as integer) > cast (b._id as integer)", null).getCount() > 0) {
            isup = true;
        } else {
            isup = false;
        }
        Cursor cursor1 = database.rawQuery("select * from (select code from crstations where name='" + sourceName + "') inner join (select code from crstations where name='" + destName + "') on 1=1", null);
        while (cursor1.moveToNext()) {
            sourceCode = cursor1.getString(0);
            destCode = cursor1.getString(1);
            Log.d("VALUE", sourceCode);
            Log.d("VALUE", destCode);
            Log.d("VALUE", String.valueOf(isup));
        }
        if (isup && checkClick) {
            qry = "select * from (select * from(select * from (select * from cruptimetable where cruptimetable.stkey='" + sourceCode + "')a inner join (select * from cruptimetable where cruptimetable.stkey='" + destCode + "')b on a.trainkey=b.trainkey)c inner join (select * from cruptrains)d on c.trainkey=d.trainkey) k where cast(k.timemin as integer)>" + time + " order by time;";
        } else if (!isup && checkClick) {
            qry = "select * from (select * from(select * from (select * from crdowntimetable where crdowntimetable.stkey='" + sourceCode + "')a inner join (select * from crdowntimetable where crdowntimetable.stkey='" + destCode + "')b on a.trainkey=b.trainkey)c inner join (select * from crdowntrains)d on c.trainkey=d.trainkey) k where cast(k.timemin as integer)>" + time + " order by time;";
        } else if (!isup || checkClick) {
            qry = "select * from(select * from (select * from crdowntimetable where crdowntimetable.stkey='" + sourceCode + "')a inner join (select * from crdowntimetable where crdowntimetable.stkey='" + destCode + "')b on a.trainkey=b.trainkey)c inner join (select * from crdowntrains)d on c.trainkey=d.trainkey order by time";
        } else {
            qry = "select * from(select * from (select * from cruptimetable where cruptimetable.stkey='" + sourceCode + "')a inner join (select * from cruptimetable where cruptimetable.stkey='" + destCode + "')b on a.trainkey=b.trainkey)c inner join (select * from cruptrains)d on c.trainkey=d.trainkey order by time";
        }
        SQLiteDatabase sqdb=getReadableDatabase();
        Cursor c = sqdb.rawQuery(qry, null);
        while (c.moveToNext()) {
           try {
                Train train = new Train();
                train.setCars(c.getString(14));
                String name = c.getString(1);
                train.setTrainKey(name);
                train.setStartst(c.getString(12));
                train.setDestst(c.getString(13));
                train.setSpeed(c.getString(16));
                train.setDepartTime(c.getString(c.getColumnIndex("time")));
                train.setArrivalTime(c.getString(c.getColumnIndex("time:1")));

                trains.add(train);
                Log.d("VALUEING", name);
            }catch (Exception e){
               e.printStackTrace();
           }

            /*finally {
                c.close();
            }*/
        }

        c.close();
        return trains;
    }

    public ArrayList<TrainRoute> getRoute(String trainNumber) {
        ArrayList<TrainRoute> trainRoutes = new ArrayList();
        SQLiteDatabase database = getReadableDatabase();
        int flag = 0;
        String query = "select trainkey from cruptimetable where trainkey='" + trainNumber + "';";
        String query1 = "select trainkey from crdowntimetable where trainkey='" + trainNumber + "';";
        Cursor cur = database.rawQuery(query, null);
        Cursor cur1 = database.rawQuery(query1, null);
        Cursor c1 = null;
        if (cur.getCount() > 0) {
            flag = 1;
        } else if (cur1.getCount() > 0) {
            flag = 2;
        } else if (cur.getCount() <= 0 && cur1.getCount() <= 0) {
            flag = 0;
        }
        Log.d("VALUE", "FLAG" + flag);
        if (flag == 1) {
            c1 = database.rawQuery("select name,time,stkey from cruptimetable inner join crstations on cruptimetable.stkey=crstations.code where trainkey='" + trainNumber + "';", null);
        } else if (flag == 2) {
            c1 = database.rawQuery("select name,time,stkey from crdowntimetable inner join crstations on crdowntimetable.stkey=crstations.code where trainkey='" + trainNumber + "'", null);
        }
        while (c1.moveToNext()) {
            try {
                TrainRoute trainRoute = new TrainRoute();
                trainRoute.setCode(c1.getString(2));
                trainRoute.setTime(c1.getString(1));
                trainRoute.setName(c1.getString(0));
                trainRoutes.add(trainRoute);
            } catch (Exception e) {
                if (c1 != null) {
                    c1.close();
                }
            } catch (Throwable th) {
                if (c1 != null) {
                    c1.close();
                }
            }
        }
        if (c1 != null) {
            c1.close();
        }
        return trainRoutes;
    }
}
