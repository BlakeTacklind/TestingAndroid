package com.tacklind.blake.mytest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestingZone extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_zone);


        final Button button=(Button)findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String TAG = "Test";
                Log.v(TAG, "Stage 1");
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Log.v(TAG, "Stage 2");
                String  url ;
                url = "jdbc:postgresql://sernity.isozill.com:5432/parcelexchange" +
                        "?sslfactory=org.postgresql.ssl.NonValidatingFactory" +
                        "&ssl=true";
                Connection conn = null;
                Log.v(TAG, "Stage 3");
                try {
                    conn = DriverManager. getConnection(url, "ParcelExchange", "Mabc0NDkYRf1yVyIfhRd");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Log.v(TAG, "Stage 4");

                if(conn!=null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                Log.v(TAG, "Stage 5");
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_testing_zone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
