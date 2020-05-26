package com.example.musicclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musiccentral.MusicStorage;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Button b1;
    Button b2;
    Button b5;
    Button b6;
    Button b7;
    Button b8;
    Button b9;
    Button b11;

    TextView t1;
    TextView songName;
    TextView artistName;
    TextView listSong;

    EditText songNumber;

    ListView list1;

    boolean needBind = false;
    boolean isBounded = false;

    MusicStorage musicService;

    String[] nameSong;
    String[] nameArtist;
    String[] songs;
    String[] songURL;
    Bitmap[] imageSong;

    String nameSong1;
    String nameArtist1;
    Bitmap imageSong1;

    int songNumbers = -1;

    String[] song1 = new String[1];
    String[] artist1 = new String[1];
    Bitmap[] songPic1 = new Bitmap[1];

    MediaPlayer mp;

    private ImageAdapter imageAdapter;

    AsyncTask<Void, Void, String> runTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        b11 = findViewById(R.id.button11);

        t1 = findViewById(R.id.textView2);
        listSong = findViewById(R.id.listSong);
        songNumber = findViewById(R.id.editText1);

        list1 = findViewById(R.id.songlist);
        list1.setVisibility(View.INVISIBLE);
        list1.setOnItemClickListener(this);

        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });


        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songNumbers = -1;
                list1.setVisibility(View.VISIBLE);
                list1.setAdapter(null);
                if (runTask != null)
                    runTask.cancel(true);
                runTask = new Task();
                runTask.execute();

            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    songs = musicService.getAllSongNames();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                String songName = "";

                for(int i = 0; i < songs.length; i++) {
                    int x = i + 1;
                    songName = songName + x + ". " + songs[i] + "\n";
                }

                listSong.setText(songName);

            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = songNumber.getText().toString();
                songNumbers = Integer.parseInt(text);
                list1.setVisibility(View.VISIBLE);
                list1.setAdapter(null);
                songNumber.setText("");

                try {
                    nameSong1 = musicService.getSongName(songNumbers - 1);
                    nameArtist1 = musicService.getSongArtist(songNumbers - 1);
                    imageSong1 = musicService.getPicture(songNumbers - 1);
                    song1[0] = nameSong1;
                    artist1[0] = nameArtist1;
                    songPic1[0] = imageSong1;
                    imageAdapter = new ImageAdapter(MainActivity.this,song1,songPic1,artist1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                list1.setAdapter(imageAdapter);

            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()) {
                    mp.pause();
                }

            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()) {
                    mp.stop();
                }

            }
        });

        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mp.isPlaying()) {
                    mp.start();
                }

            }
        });

        b2.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);
        b11.setEnabled(false);
    }

    public void startService() {
        if(!needBind){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.musiccentral", "com.example.musiccentral.MainActivity"));
            startForegroundService(intent);
            b2.setEnabled(true);
            b1.setEnabled(false);
            b5.setEnabled(true);
            b6.setEnabled(true);
            b7.setEnabled(true);
            t1.setText(R.string.serviceBound);
            bind();
        }
        else {
            bind();
            b2.setEnabled(true);
            b1.setEnabled(false);
            b5.setEnabled(true);
            b6.setEnabled(true);
            b7.setEnabled(true);
            t1.setText(R.string.serviceBound);
        }
    }

    public void bind(){
        if (!isBounded) {
            boolean bind = false;
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.musiccentral", "com.example.musiccentral.MainActivity"));
            bind = getApplicationContext().bindService(intent, this.connection, Context.BIND_AUTO_CREATE);
            if(bind){
                isBounded=true;
            }
        } else {
            bind();
        }
    }

    public void stopService(){
            Toast.makeText(MainActivity.this, "Service Unbounded ,Press Start button to Re-Bind", Toast.LENGTH_SHORT).show();
            if(isBounded) {

                getApplicationContext().unbindService(this.connection);
                isBounded = false;
            }
            b1.setEnabled(true);
            b2.setEnabled(false);
            b5.setEnabled(false);
            b6.setEnabled(false);
            b7.setEnabled(false);
            t1.setText(R.string.service_unbounded);
            needBind=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBounded) {
            unbindService(connection);
        }
        if (runTask != null)
            runTask.cancel(true);

        mp.release();

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = MusicStorage.Stub.asInterface(service);
            isBounded = true;
            Toast.makeText(MainActivity.this, "Service was connected.",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBounded = false;
            Toast.makeText(MainActivity.this, "Service was disconnected.",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        b8.setEnabled(true);
        b9.setEnabled(true);
        b11.setEnabled(true);
        if(mp != null){
            if(mp.isPlaying()) {
                mp.stop();
            }
        }
        mp = new MediaPlayer();
        if(songURL == null) {
            try {
                songURL = musicService.getSongURL();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        try {
            if(songNumbers == -1) {
                mp.setDataSource(songURL[position]);
            } else {
                mp.setDataSource(songURL[songNumbers - 1]);
            }
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private final class Task extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                nameSong = musicService.getAllSongNames();
                nameArtist = musicService.getAllArtistNames();
                imageSong = musicService.getAllPictures();

                imageAdapter = new ImageAdapter(MainActivity.this,nameSong,imageSong,nameArtist);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            list1.setAdapter(imageAdapter);
        }
    }

}
