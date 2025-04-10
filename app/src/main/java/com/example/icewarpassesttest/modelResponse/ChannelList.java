package com.example.icewarpassesttest.modelResponse;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName ="Channel")
public class ChannelList {
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey
    @NonNull
    String id;


    @ColumnInfo(name = "name")
    @SerializedName("name")
    String name;

    @ColumnInfo(name = "group_folder_name")
    @SerializedName("group_folder_name")
    String groupHolderName;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupHolderName() {
        return groupHolderName;
    }

    public void setGroupHolderName(String groupHolderName) {
        this.groupHolderName = groupHolderName;
    }

    @Override
    public String toString() {
        return "ChannelList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", groupHolderName='" + groupHolderName + '\'' +
                '}';
    }
}
