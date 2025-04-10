package com.example.icewarpassesttest.channelScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.icewarpassesttest.R;
import com.example.icewarpassesttest.adapter.ChannelListAdapter;
import com.example.icewarpassesttest.apiService.ApiClient;
import com.example.icewarpassesttest.apiService.ApiService;
import com.example.icewarpassesttest.database.IceWarpDao;
import com.example.icewarpassesttest.database.IceWarpDatabase;
import com.example.icewarpassesttest.databinding.ActivityChannelScreenBinding;
import com.example.icewarpassesttest.login.LoginActivity;
import com.example.icewarpassesttest.modelResponse.ChannelList;
import com.example.icewarpassesttest.modelResponse.ChannelResponse;
import com.example.icewarpassesttest.modelResponse.LoginResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChannelScreenActivity extends AppCompatActivity {
    IceWarpDao dao;
    ApiService apiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ChannelListAdapter adapter;


    ActivityChannelScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChannelScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setting the toolbar
        setToolBar();

        //initializing all claasses
        initData();

        //setting the adapter after creating the class
        setAdapter();

        // loading data from server
        loadChannelList();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadChannelList() {

        /*
        *10-07-2025
        *for avoid memory leak using compositeDisposable while fetching the userDetails from database
        * flatmap used for getting observable data from observable
        * */

        compositeDisposable.add(dao.getUserDetails().subscribeOn(Schedulers.io())
                .flatMap(loginResponse -> apiService.getChannelListResponse(loginResponse.getToken(), true,
                        true, false))
                .observeOn(AndroidSchedulers.mainThread())
                .map(channelResponse -> {
                    Log.d("Log10", "Display Channel Response: " + channelResponse.getChannelLists());
                    List<ChannelList> channelLists = new ArrayList<>();
                    for (int i = 0; i < channelResponse.getChannelLists().size(); i++) {
                        ChannelList model = new ChannelList();

                        model.setId(channelResponse.getChannelLists().get(i).getId());
                        model.setName(channelResponse.getChannelLists().get(i).getName());
                        model.setGroupHolderName(channelResponse.getChannelLists().get(i).getGroupHolderName());

                        channelLists.add(model);
                    }

                    return channelLists;

                })

                .flatMapCompletable(channelResponseList ->
                        dao.insertChannelDetails(channelResponseList).
                                subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()))
                .andThen(dao.getAllChannelList().subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(channelResponseList -> {
                    Log.d("Log10", "Display Channel Response: Adapter1" + channelResponseList.get(0).getName());
                    adapter.updateChannelList(channelResponseList);
                    binding.pbChannel.setVisibility(View.GONE);

                }, throwable -> {
                    binding.pbChannel.setVisibility(View.GONE);
                    Log.d("Log10", "Error loading listData: " + throwable.getMessage());
                    // This is error block
//                    Toast.makeText(this, "Error loading while displaying list:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

                })
        );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }

    private void initData() {
        dao = IceWarpDatabase.getInstance(this).iceWarpDao();
        apiService = ApiClient.getClient();
    }

    private void setAdapter() {
        binding.pbChannel.setVisibility(View.VISIBLE);
        adapter = new ChannelListAdapter(new ArrayList<>());
        binding.rvChannelList.setAdapter(adapter);

    }

    private void setToolBar() {
        setSupportActionBar(binding.tbChannel);
        binding.tbChannel.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow_black));
        binding.tbChannel.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChannelScreenActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            logout();

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }


    private void logout() {

        compositeDisposable.add(dao.deleteUserDetails()
                .andThen(dao.deleteChannelList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear(); // or just editor.remove("is_logged_in");
                    editor.apply();
                    startActivity(new Intent(ChannelScreenActivity.this, LoginActivity.class));
                    finish();
                }, throwable -> {
                    Toast.makeText(this, "Error during logout:"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));





    }



}