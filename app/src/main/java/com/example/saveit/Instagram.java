package com.example.saveit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.saveit.FacebookAPI.*;

import java.nio.charset.Charset;
import java.util.Random;


public class Instagram extends AppCompatActivity {
    EditText url;
    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_instagram);
        loadBottomNavigation();


        url=findViewById(R.id.link2);
        download = findViewById(R.id.download2);

        download.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                Toast.makeText(Instagram.this, "Processing Request Please Wait...", Toast.LENGTH_SHORT).show();
                String videoURL = url.getText().toString();
                Boolean bool = false;

                new FacebookExtractor(Instagram.this,videoURL,true)
                {
                    @Override
                    protected void onExtractionComplete(FacebookFile facebookFile) {


                        String downURL = facebookFile.getHdUrl();
                        downURL = downURL.replace("&amp;","&");
                        downURL = downURL.replace("/instagram.fccu19-1.fna.fbcdn.net/","/scontent.cdninstagram.com/");

                        Log.e("DownloadURL","URL:"+downURL);

                        String fileName=genName(20);
                        downloadInstagramVideo(downURL,fileName);
                    }

                    @Override
                    protected void onExtractionFail(Exception error) {
                        Log.e("Error","Error :: "+error.getMessage());
                        Toast.makeText(Instagram.this, "Extraction Failed:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }

                    @Override
                    protected void onExtractionFail(String Error) {
                    }
                };

            }
        });
    }

    private void loadBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.Instagram);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.YouTube:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Facebook:
                        startActivity(new Intent(getApplicationContext(),Facebook.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Instagram:
                        return true;

                    case R.id.Downloads:
                        startActivity(new Intent(getApplicationContext(),Downloads.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;

            }
        });
    }

    private void downloadInstagramVideo(String url, String name){
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(downloadUri);
        req.setTitle(name);
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS , "SaveIT Downloads/Video/" + name + ".mp4");
        req.setMimeType("*/*");
        DownloadManager downloadManager = (DownloadManager)getSystemService(Facebook.DOWNLOAD_SERVICE);
        downloadManager.enqueue(req);
        Toast.makeText(Instagram.this, "Download Started", Toast.LENGTH_SHORT).show();
    }

    private String genName(int n){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

}