package com.tacklind.blake.mytest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class TestingZone extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_zone);


        final Button button=(Button)findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String TAG = "Test";
                //Log.v(TAG, "Stage 1");
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //Log.v(TAG, "Stage 2");

                DataBaseWarper.checkUserExistance("HAHA");
                //DataBaseWarper.getUsers();
                //DataBaseWarper.addUser("Joe");
            }
        });

    }


    private static class DataBaseWarper{
        final private static String url = "jdbc:postgresql://serenity.isozilla.com:5432/" +
                "parcelexchange?sslfactory=org.postgresql.ssl.NonValidatingFactory" +
                "&ssl=true";

        public static void checkUserExistance(String name){
            new get().execute(new getStrings("users", "username", "username = '" + name + "';"));
        }



        public static void getUsers(){
            new get().execute(new getStrings("users", "username"));

            return;
        }

        public static void addUser(String name){
            new add().execute(new addStrings("users", "username", name));
        }

        private static class getStrings{
            public getStrings(String t, String c){
                table = t;
                columns = c;
                extras = "";
            }

            public getStrings(String t, String c, String e){
                this(t,c);
                extras = e;
            }

            public String table;
            public String columns;
            public String extras;
        }

        private static class addStrings{
            public addStrings(String t, String c, String v){
                table = t;
                column = c;
                values = v;
            }

            public String table;
            public String column;
            public String values;
        }

        protected static class add extends AsyncTask<addStrings, Integer, Integer>{
            Connection conn;
            protected Integer doInBackground(addStrings... in){
                conn = connectToDataBase();

                for(addStrings g: in) {
                    insertINTO(conn, g.table, g.column, g.values);
                }

                return 0;
            }

            protected void onPostExecute(Integer result){
                getUsers();
            }
        }

        protected static class get extends AsyncTask<getStrings, Integer, Integer>{
            Connection conn;

            protected Integer doInBackground(getStrings... in){
                conn = connectToDataBase();
                for(getStrings g: in) {
                    for (String s : getFROM(conn, g.table, g.columns, g.extras)) {
                        Log.v("getUsers", s);
                    }
                }

                if (conn != null)
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                return 0;
            }
        }

        protected static void insertINTO (Connection conn, String table, String column, String values){
            final String TAG = "insertINTO";

            if (conn == null){
                Log.e(TAG, "Connection was null");
                return;
            }

            String sql;
            sql = "INSERT INTO " + table + " (" + column + ") VALUES (?);";
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                st.setString(1, values);
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        protected static ArrayList<String> getFROM(Connection conn, String table, String column, String extra){
            final String TAG = "getFROM";

            if (conn == null){
                Log.e(TAG, "Connection was null");
                return null;
            }


            String sql;
            sql = "SELECT " + column + " FROM " + table;
            if (extra != "")
                sql += " WHERE " + extra;

            ArrayList<String> res = new ArrayList<String>();
            Statement st = null;
            try {
                st = conn.createStatement();
                ResultSet rs = null;
                rs = st.executeQuery(sql);

                while(rs.next()){
                    String out = rs.getString(column);
                    res.add(out);
                    //Log.v(TAG, out.toString());
                }

                rs.close();
                st.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return res;
        }

        protected static Connection connectToDataBase(){
            Connection conn = null;

            try {
                conn = DriverManager. getConnection(url, "parcelexchange", "Mabc0NDkYRf1yVyIfhRd");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (conn == null)
                Log.e("connectToDataBase", "Connection was null");

            return conn;
        }

    }

    private class GetData extends AsyncTask<URL, Integer, Long>{
        protected Long doInBackground(URL... urls) {
            String  url ;
            url = "jdbc:postgresql://serenity.isozilla.com:5432/parcelexchange" +
                    "?sslfactory=org.postgresql.ssl.NonValidatingFactory" +
                    "&ssl=true";
            String TAG = "Test";
            Connection conn = null;
            //Log.v(TAG, "Stage 3");
            try {
                conn = DriverManager. getConnection(url, "parcelexchange", "Mabc0NDkYRf1yVyIfhRd");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Log.v(TAG, "Stage 4");

            if(conn!=null) {

                String sql;
                sql = "SELECT username FROM users;";

                Statement st = null;
                try {
                    st = conn.createStatement();
                    ResultSet rs = null;
                    rs = st.executeQuery(sql);

                    while(rs.next()){
                        String user = rs.getString("username");
                        Log.v(TAG, user);
                    }

                    rs.close();
                    st.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Log.v(TAG, "Stage 4.5");
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //Log.v(TAG, "Stage 5");

            return Long.valueOf(0);
        }

        protected void onPostExecute(Long result){
            //Log.v("GetData", "Done Stuff");
        }
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
