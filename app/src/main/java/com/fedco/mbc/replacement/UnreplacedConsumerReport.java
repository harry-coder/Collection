package com.fedco.mbc.replacement;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fedco.mbc.R;
import com.fedco.mbc.activity.CameraActivity;
import com.fedco.mbc.logging.Logger;
import com.fedco.mbc.sqlite.DB;
import com.fedco.mbc.utils.UtilAppCommon;

import java.sql.SQLException;
import java.util.ArrayList;

public class UnreplacedConsumerReport extends Activity {

    String cnumber, name, meter, division, address, cycle, route, subdivision,section,OLD_MTR_CAP,NEW_MTR_CAP,OLD_MTR_FR,NEW_MTR_IR;
    String consumer_name_next;
    TextView c_count, t_count;
    Logger Log;
    private GridView gridView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    //DatabaseHandler handler;
    SQLiteDatabase SD;
    DB dbHelper;
    UtilAppCommon uac;

    static final String[] letters = new String[]{

            "Cons No","IVRS No","OLD Meter No","NEW Meter No","OLD Meter Make","NEW Meter Make","OLD Meter Type","NEW Meter Type","OLD Meter Capacity","NEW Meter Capacity","OLD Meter FR","NEW Meter IR"};

    GridView grid;
    Button btn_bck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace_consumer_report);

        //GridView
        gridView = (GridView) findViewById(R.id.gridView1);
        c_count = (TextView) findViewById(R.id.text5);
        t_count = (TextView) findViewById(R.id.text4);
        btn_bck =(Button)findViewById(R.id.Buttonback);
        btn_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // finish activity
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right);
            }
        });
        dbHelper = new DB(getApplicationContext());
        SD = dbHelper.getWritableDatabase();

        // for showing header value
        grid = (GridView) findViewById(R.id.gridView);
        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.grid_view_header, letters);
        grid.setAdapter(adapter1);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        //ArrayList
        list = new ArrayList<String>();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                Toast.makeText(getApplicationContext(), list.get(position).toString(), Toast.LENGTH_SHORT).show();


                consumer_name_next = list.get(position).toString();
                String ret1 = "select * from TBL_CONSMAST where Name = '" + consumer_name_next + "'";
                //String ret1="SELECT * FROM TBL_CONSMAST WHERE Name ="+consumer_name_next;
                //String ret = "SELECT Consumer_Number FROM TBL_CONSMAST WHERE Name='SAPHALA PARIDA'";
                Cursor curconsmasData1 = SD.rawQuery(ret1, null);

                /****GETTING DATA FROM CURSOR WHEN CURSOR IS NOT NULL AND ON THE FORST INDEX****/
                if (curconsmasData1 != null && curconsmasData1.moveToFirst()) {

                    uac = new UtilAppCommon();
                    try {
                        uac.copyResultsetToConmasClass(curconsmasData1);
                        uac.mpConsumerPrint();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    /****INITIALISATION OF INTENT AND ADDING SENDING DATA TO NEXT SCREEN , STARING INTENT****/

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        intent.putExtra("datalist", allist);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                    //String temp_address = c.getString(3);
                    //System.out.println(temp_address+" result of select Query");
                }


            }
        });


        try {

            String total_c = "select * from TBL_CONSMAST";
            Cursor cursor_total = SD.rawQuery(total_c, null);
            Toast.makeText(getApplicationContext(), "Total Consumer:" + cursor_total.getCount(), Toast.LENGTH_LONG).show();
            t_count.setText("Total Consumer is: " + cursor_total.getCount());

            String ret = "SELECT *  FROM TBL_METER_REPLACEMENT";
            // System.out.println("query---" + ret);
            Cursor curSearchdata = SD.rawQuery(ret, null);
            c_count.setText("Replaced Consumer : " + curSearchdata.getCount());

            if (curSearchdata != null && curSearchdata.moveToFirst()) {

                do {

                    cnumber     = curSearchdata.getString(0);
                    section     = curSearchdata.getString(1);//IVRS
                    name        = curSearchdata.getString(5);
                    meter       = curSearchdata.getString(17);
                    address     = curSearchdata.getString(6);
                    cycle       = curSearchdata.getString(18);//group
                    route       = curSearchdata.getString(7);//RDG
                    division    = curSearchdata.getString(19);
                    OLD_MTR_CAP    = curSearchdata.getString(8);
                    NEW_MTR_CAP    = curSearchdata.getString(20);
                    OLD_MTR_FR = curSearchdata.getString(10);
                    NEW_MTR_IR  = curSearchdata.getString(22);
//                    subdivision = curSearchdata.getString(71);
////                    section     = cursor_total.getString(10);

                    //add in to array list
                    list.add(cnumber);
                    list.add(section);
                    list.add(name);
                    list.add(meter);
                    list.add(address);
                    list.add(cycle);
                    list.add(route);
                    list.add(division);
                    list.add(OLD_MTR_CAP);
                    list.add(NEW_MTR_CAP);
                    list.add(OLD_MTR_FR);
                    list.add(NEW_MTR_IR);
//                    list.add(subdivision);
//                    list.add("dummy");
                } while (curSearchdata.moveToNext());
            }
            //for holding retrieve data from query and store in the form of rows
//            String ret = "SELECT * FROM TBL_CONSMAST WHERE Consumer_Number NOT IN(SELECT Cons_Number  FROM TBL_BILLING)";
//            System.out.println("query---" + ret);
//            Cursor curSearchdata = SD.rawQuery(ret, null);
//            Toast.makeText(getApplicationContext(), "Total data Found:" + curSearchdata.getCount(), Toast.LENGTH_LONG).show();
//            c_count.setText("Unbilled Consumer is: " + curSearchdata.getCount());
            // Cursor c=handler.DisplayData();
            //Move the cursor to the first row.
//            if (cursor_total.moveToFirst()) {
//        /*  list.add("Consumer No.");
//          list.add("Name");
//          list.add("Meter No.");
//          list.add("Address");
//          list.add("Cycle");
//          list.add("Route");
//          list.add("Division");
//          list.add("Subdivision");*/
//
//                do {
//
//                    //gridView.setAdapter(adapter);
//                } while (cursor_total.moveToNext());//Move the cursor to the next row.
//            } else {
//                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
//            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No data found" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.grid_view_billing, list);
        gridView.setAdapter(adapter);
        // handler.close();
        //Toast.makeText(getBaseContext(),"Name: "+name+"Roll No: "+roll+"Course: "+course , Toast.LENGTH_LONG).show();
    }

    public void onBackPressed() {
//        if (exit) {
        finish(); // finish activity
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
//        overridePendingTransition(R.anim.anim_slide_in_right,
//                R.anim.anim_slide_out_right);
//
//        Debug.stopMethodTracing();
//        super.onDestroy();
//        } else {
//            Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
//            exit = true;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    exit = false;
//                }
//            }, 3 * 1000);
//        }
    }
}  