package com.example.icewarpassesttest.apiService;

import com.example.icewarpassesttest.modelResponse.ChannelResponse;
import com.example.icewarpassesttest.modelResponse.LoginResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

// this interface used for calling the webmethods or webservices from app level
public interface ApiService {

    @FormUrlEncoded
    @Headers(value = "Content-Type: application/x-www-form-urlencoded")
    @POST("iwauthentication.login.plain")
    Single<LoginResponse> login(
            @Field("username") String userName,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("channels.list")
    @Headers(value = "Content-Type: application/x-www-form-urlencoded")
    Single<ChannelResponse> getChannelListResponse(
            @Field("token") String token,
            @Field("include_unread_count") boolean includeUnreadCount,
            @Field("exclude_members") boolean excludeMembers,
            @Field("include_permissions") boolean includePermission
            );

}
