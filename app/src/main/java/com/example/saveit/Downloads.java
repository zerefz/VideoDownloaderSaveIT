package com.example.saveit;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Downloads extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;
    MediaPlayer mediaPlayer; //for media player integration
    private List<File> files = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_downloads);

        loadBottomNavigation();
        loadListView();
    }

    private void loadBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.Downloads);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.YouTube:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
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
                        return true;
                }
                return false;

            }
        });
    }

    private void loadListView(){
        if(ContextCompat.checkSelfPermission(Downloads.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Downloads.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(Downloads.this,new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
            else{
                ActivityCompat.requestPermissions(Downloads.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
        }
        else
        {
            mainWork();
        }

    }

    public void mainWork(){

        listView = (ListView)findViewById(R.id.data_view);
        arrayList = new ArrayList<>();


        getData();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Share Video from ListView
                String fileName = (String) listView.getItemAtPosition(position);
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("video/*");
                intentShare.putExtra(Intent.EXTRA_STREAM,Uri.parse("/storage/emulated/0/"+Environment.DIRECTORY_DOWNLOADS+"/SaveIT Downloads/Video/"+fileName+".mp4"));  //"file://"+
                startActivity(Intent.createChooser(intentShare,"Sharing file"));
            }
        });

    }

    public void getData(){

        Cursor videoCursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), null, MediaStore.Video.Media.DATA + " like ? ", new String[] {"%SaveIT Downloads/Video%"}, null);

        if(videoCursor!=null && videoCursor.moveToFirst()){
            int videoTitle = videoCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            do{
                String currentTitle = videoCursor.getString(videoTitle);
                arrayList.add(currentTitle);
            }while(videoCursor.moveToNext());

        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:
                if(grantResults.length>0 && grantResults [0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(Downloads.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        mainWork();
                    }
                }
                else{
                    Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }
}



