package com.example.musiccentral;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.musiccentral.MusicStorage.*;

public class MainActivity extends Service {

    String[] songNames;
    Bitmap[] imagesSong = new Bitmap[5];
    String[] artistNames;
    String[] songURL;

    private static final String CHANNEL_ID = "Music Central";
    private static final int NOTIFICATION_ID = 1;


    @Override
    public void onCreate() {
        super.onCreate();
        songNames = getResources().getStringArray(R.array.songNames);
        artistNames = getResources().getStringArray(R.array.artistNames);
        songURL = getResources().getStringArray(R.array.songURL);

       this.createNotificationChannel();


        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.musicclient", "com.example.musicclient.MainActivity"));
        final PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setFullScreenIntent(pi, false)
                .build();

        startForeground(NOTIFICATION_ID, notification);


    }

    private void createNotificationChannel() {
        CharSequence name = "Music player notification";
        String description = "The channel is for music player notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService(intent);
        return START_STICKY;
    }


    private final MusicStorage.Stub binder = new MusicStorage.Stub() {
        @Override
        public String[] getAllSongNames() {
            return songNames;
        }

        @Override
        public String[] getAllArtistNames() {
            return artistNames;
        }

        @Override
        public Bitmap[] getAllPictures() {
            imagesSong[0] = BitmapFactory.decodeResource(getResources(), R.drawable.love);
            imagesSong[1] = BitmapFactory.decodeResource(getResources(), R.drawable.notafraid);
            imagesSong[2] = BitmapFactory.decodeResource(getResources(), R.drawable.collapse);
            imagesSong[3] = BitmapFactory.decodeResource(getResources(), R.drawable.round);
            imagesSong[4] = BitmapFactory.decodeResource(getResources(), R.drawable.feeling);

            return imagesSong;

        }

        @Override
        public String getSongName(int songId) {
            return songNames[songId];
        }

        @Override
        public String getSongArtist(int songId) {
            return artistNames[songId];
        }

        @Override
        public Bitmap getPicture(int songId) {
            imagesSong[0] = BitmapFactory.decodeResource(getResources(), R.drawable.love);
            imagesSong[1] = BitmapFactory.decodeResource(getResources(), R.drawable.notafraid);
            imagesSong[2] = BitmapFactory.decodeResource(getResources(), R.drawable.collapse);
            imagesSong[3] = BitmapFactory.decodeResource(getResources(), R.drawable.round);
            imagesSong[4] = BitmapFactory.decodeResource(getResources(), R.drawable.feeling);

            return imagesSong[songId];
        }

        @Override
        public String[] getSongURL() {
            return songURL;
        }
    };
}

