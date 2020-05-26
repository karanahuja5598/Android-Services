package com.example.musicclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private String[] songName;
    private Bitmap[] songImage;
    private String[] artistName;

    ImageAdapter(@NonNull Context context, @NonNull String[] songNames, Bitmap[] songImages, String[] artistNames) {
        this.context=context;
        this.songName = songNames;
        this.songImage = songImages;
        this.artistName = artistNames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.song_list,parent,false);
            ImageView songPicture = (ImageView) convertView.findViewById(R.id.songImage);
            TextView songName1 = (TextView) convertView.findViewById(R.id.songName);
            TextView artistName1 = (TextView) convertView.findViewById(R.id.artistName);
            songName1.setText(songName[position]);
            songPicture.setImageBitmap(songImage[position]);
            artistName1.setText(artistName[position]);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return songName.length;
    }

    @Override
    public Object getItem(int i) {
        return songName[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
