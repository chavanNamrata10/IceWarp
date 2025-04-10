package com.example.icewarpassesttest.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.icewarpassesttest.modelResponse.ChannelList;
import com.example.icewarpassesttest.modelResponse.LoginResponse;

@Database(entities = {LoginResponse.class, ChannelList.class}, version = 1)
public abstract class IceWarpDatabase extends RoomDatabase {

    public static volatile IceWarpDatabase instance;

    public abstract IceWarpDao iceWarpDao();

    public static IceWarpDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (IceWarpDatabase.class) {
                instance = Room.databaseBuilder(context, IceWarpDatabase.class, "IceWarp_db").build();
            }
        }
        return instance;
    }
}
