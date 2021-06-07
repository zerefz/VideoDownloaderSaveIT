package com.example.saveit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.File;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity {
    Button download;
    EditText editText;
    String newLink;
    RadioGroup radioGroup;
    RadioButton rad_720, rad_480, rad_360, rad_audio;
    int tag;
    String values;
    String ext = null;

    private File storage;
    private String[] storagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        loadBottomNavigation();



        editText = findViewById(R.id.link);
        radioGroup = findViewById(R.id.radioGroup);
        rad_720 = findViewById(R.id.rad_720);
        rad_480 = findViewById(R.id.rad_480);
        rad_360 = findViewById(R.id.rad_360);
        rad_audio = findViewById(R.id.rad_audio);
        download = findViewById(R.id.download);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rad_720.isChecked()) {
                    tag = 22;
                    ext = ".mp4";
                } else if (rad_480.isChecked()) {
                    tag = 135;
                    ext = ".mp4";
                } else if (rad_360.isChecked()) {
                    tag = 18;
                    ext = ".mp4";
                } else if (rad_audio.isChecked()) {
                    tag = 251;
                    ext = ".mp3";
                }
                values = editText.getText().toString();

                DownloadMyVideo(values, tag, ext);
            }
        });


    }

    public void DownloadMyVideo(String values, int t, String extens) {

        @SuppressLint("StaticFieldLeak") YouTubeExtractor youTubeExtractor = new YouTubeExtractor(MainActivity.this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles != null) {

                    newLink = ytFiles.get(t).getUrl();
                    String title = videoMeta.getTitle();
                    String title1 = title.replace(".", ""); //to be able to download videos which have "." in title name but gives an error
                    //but video thumbnail will not be visible
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(newLink));      //Constructor call
                    title1 = title1.replace("#","");
                    request.setTitle(title1);        //set name for video file
                    if (extens == ".mp4") {
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SaveIT Downloads/Video/" + title1 + extens);//Destination of video file
                    } else if (extens == ".mp3") {
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SaveIT Downloads/Audio/" + title1 + extens);//Destination of audio file
                    }
                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE); //Return the handle to a system-level service by name

                    request.allowScanningByMediaScanner();                 //the file to be downloaded is to be scanned by MediaScanner


                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);       //Restrict the types of networks over which this download may proceed.


                    downloadManager.enqueue(request);   //Enqueues Download in device

                    Toast.makeText(MainActivity.this, "Downloading Started", Toast.LENGTH_SHORT).show(); //show download started popup
                } else {
                    Toast.makeText(MainActivity.this, ytFiles + "yTfiles : Null , API error!!", Toast.LENGTH_SHORT).show();
                }
            }

        };
        youTubeExtractor.execute(values);
    }

    private void loadBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.YouTube);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.YouTube:
                        return true;

                    case R.id.Facebook:
                        startActivity(new Intent(getApplicationContext(), Facebook.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.Instagram:
                        startActivity(new Intent(getApplicationContext(), Instagram.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.Downloads:
                        startActivity(new Intent(getApplicationContext(), Downloads.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;

            }
        });
    }
}
