package com.example.saveit;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saveit.FacebookAPI.FacebookExtractor;
import com.example.saveit.FacebookAPI.FacebookFile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Facebook extends AppCompatActivity {

    EditText url;
    Button download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_facebook);
        loadBottomNavigation();

        url= findViewById(R.id.link1);
        download=findViewById(R.id.download1);

        download.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                Toast.makeText(Facebook.this, "Processing Request Please Wait...", Toast.LENGTH_SHORT).show();
                String videoURL = url.getText().toString();
                Boolean bool = false;

                new FacebookExtractor(Facebook.this,videoURL,true)
                {
                    @Override
                    protected void onExtractionComplete(FacebookFile facebookFile) {

                        String downURL = facebookFile.getHdUrl();
                        downURL = downURL.replace(".fccu19-1.fna.","-lhr8-2.xx.");
                        downURL = downURL.replace("&amp;","&");
                        String fileName = "Facebook_"+genNameFB(20);
                        if(fileName.contains("Facebook Watch"))
                        {
                            fileName=fileName.replace("Facebook Watch-&x98f; ","Facebook_Shorts_");
                        }
                        Log.e("FileName","F:"+fileName);
                        downloadFacebookVideo(downURL,fileName);
                    }

                    @Override
                    protected void onExtractionFail(Exception error) {
                        Log.e("Error","Error :: "+error.getMessage());
                        Toast.makeText(Facebook.this, "Extraction Failed:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }

                    @Override
                    protected void onExtractionFail(String Error) {
                        //Should Remain Empty
                    }
                };

            }
        });
    }

    private void loadBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Facebook);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.YouTube:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Facebook:
                        return true;

                    case R.id.Instagram:
                        startActivity(new Intent(getApplicationContext(),Instagram.class));
                        overridePendingTransition(0,0);
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

    private String genNameFB(int n){
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

    private void downloadFacebookVideo(String url, String name){
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(downloadUri);
        req.setTitle(name);
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS , "SaveIT Downloads/Video/" + name + ".mp4");
        req.setMimeType("*/*");
        DownloadManager downloadManager = (DownloadManager)getSystemService(Facebook.DOWNLOAD_SERVICE);
        downloadManager.enqueue(req);
        Toast.makeText(this, "Download Started", Toast.LENGTH_SHORT).show();
    }
}

