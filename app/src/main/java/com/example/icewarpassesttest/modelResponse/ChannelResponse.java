package com.example.icewarpassesttest.modelResponse;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ChannelResponse {

    @SerializedName("channels")
    List<ChannelList> channelLists;

    @SerializedName("ok")
    boolean ok;

    public List<ChannelList> getChannelLists() {
        return channelLists;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setChannelLists(List<ChannelList> channelLists) {
        this.channelLists = channelLists;
    }

    @Override
    public String toString() {
        return "ChannelResponse{" +
                "channelLists=" + channelLists +
                ", ok=" + ok +
                '}';
    }
}
