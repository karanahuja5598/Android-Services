// MusicStorage.aidl
package com.example.musiccentral;

// Declare any non-default types here with import statements

import android.graphics.Bitmap;

interface MusicStorage {

    // Retrieve all information for all songs stored in the service.
    String[] getAllSongNames();
    String[] getAllArtistNames();
    Bitmap[] getAllPictures();

    // Retrieve all information for one specific song by the song’s number
    String getSongName(int songId);
    String getSongArtist(int songId);
    Bitmap getPicture(int songId);

    // Retrieve the URL string of the site with the song’s audio file.
    String[] getSongURL();

}
