package com.example.dipon.updatedinstallation.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dipon.updatedinstallation.R;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnUpdate;
    private String url = "http://162.222.186.235/billing/downloadDialer.jsp" ;
    private String TAG = "MAIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        Log.e(TAG, "onCreate: ");

        installAPK ();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this , ObserverService.class);
        startService(intent);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void installAPK() {
        Intent newIntent = getIntent();
        if(newIntent != null) {

            String temp = newIntent.getStringExtra("path");

            if (temp != null && temp.length() > 0) {
                Log.e(TAG, "onClick: " + temp);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
                File file = new File(temp);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(intent);
            }
            else {
                Log.e(TAG, "onCreate: got 0");
            }
        }
    }
}
