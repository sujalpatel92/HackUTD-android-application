package com.hackutd.sujal.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class sensorActivity extends AppCompatActivity {

    public TextView textView;
    AlertDialog.Builder dialog;
    public AlertDialog.Builder popUpDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        textView = (TextView) findViewById(R.id.textView2);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(customOnClickListener);
        dialog = new AlertDialog.Builder(this);
        popUpDialog = new AlertDialog.Builder(this);
        dialog.setTitle("Button clicked");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        textView = (TextView) findViewById(R.id.textView2);
    }

    private Button.OnClickListener customOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            //dialog.show();
            new RESTapiGet().execute();
        }
    };
    public class RESTapiGet extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            StringBuffer response = new StringBuffer();

            popUpDialog.setTitle("Response Code");
            popUpDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            try {
                URL url = new URL("http://ec2-52-24-176-44.us-west-2.compute.amazonaws.com:8081/readData");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                int responseCode = httpURLConnection.getResponseCode();
                System.out.println("Response code: "+responseCode);
                /*create alert dialog*/
                /*popUpDialog.setTitle(Integer.toString(responseCode));
                popUpDialog.create();
                popUpDialog.show();*/
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inp;
                while((inp = in.readLine()) != null){
                    response.append(inp);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("String: "+response.toString());
            return response.toString();
        }
        protected void onPostExecute(String results) {
            if (results!=null) {
                textView.setText(results);
            }
        }
    }
}
  