package com.example.icewarpassesttest.modelResponse;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

@Entity(tableName = "UserResponse")
public class LoginResponse {

    @SerializedName("authorized")
    @ColumnInfo(name = "authorized")
    String authorized;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "token")
    @SerializedName("token")
    String token;

    @SerializedName("host")
    @ColumnInfo(name = "host")
    String host;

    @SerializedName("email")
    @ColumnInfo(name = "email")

    String email;

    @SerializedName("ok")
    @ColumnInfo(name = "ok")
    String ok;

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getToken() {
        return token;
    }

    public String getHost() {
        return host;
    }

    public String getEmail() {
        return email;
    }

    public String getOk() {
        return ok;
    }
}
