package com.example.icewarpassesttest.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.icewarpassesttest.modelResponse.ChannelList;
import com.example.icewarpassesttest.modelResponse.LoginResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface IceWarpDao {

    // insert User data to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(LoginResponse loginResponse); // while inserting data into database and notifies the operation

    @Query("Select * from UserResponse")
    Single<LoginResponse> getUserDetails(); // only single item will be fetch from this method


    @Query("DELETE from UserResponse")
    Completable deleteUserDetails();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertChannelDetails(List<ChannelList> channelResponseList);

    @Query("Select * from Channel Order By group_folder_name, name")
    Flowable<List<ChannelList>> getAllChannelList(); // heavy or large data getting from database

    @Query("Delete from Channel")
    Completable deleteChannelList();
}
