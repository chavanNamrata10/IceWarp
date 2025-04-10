package com.example.icewarpassesttest.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.icewarpassesttest.R;
import com.example.icewarpassesttest.apiService.ApiClient;
import com.example.icewarpassesttest.apiService.ApiService;
import com.example.icewarpassesttest.channelScreen.ChannelScreenActivity;
import com.example.icewarpassesttest.database.IceWarpDao;
import com.example.icewarpassesttest.database.IceWarpDatabase;
import com.example.icewarpassesttest.databinding.ActivityLoginBinding;
import com.example.icewarpassesttest.modelResponse.LoginResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    private IceWarpDao iceWarpDao;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initializing the retrofit and dao class in main Activity
        apiService = ApiClient.getClient();
        iceWarpDao = IceWarpDatabase.getInstance(this).iceWarpDao();


        //btn onClick method
        setOnClickListener();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }


    private void setOnClickListener() {
        binding.btnLogin.setOnClickListener(v -> {
         // validate Login on cloick of the Login button
            validateToLogin();

        });


    }

    private void validateToLogin() {

        // if user click on btn then below code will execute

        if (binding.etLoginEmail.getText().toString().trim().isEmpty() || binding.etLoginPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please insert Email and Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etLoginEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Please insert valid Email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.etLoginPassword.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "Your password is too short. Please insert valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        loginUser();
    }

    @SuppressLint("CheckResult")
    private void loginUser() {
        binding.progressBarLogin.setVisibility(View.VISIBLE);

        apiService.login(binding.etLoginEmail.getText().toString().trim(), binding.etLoginPassword.getText().toString().trim())
                .subscribeOn(Schedulers.io()) // run this on a background thread or for fetching data
                .observeOn(AndroidSchedulers.mainThread()) // handles results on UI thread
                .subscribe((loginResponse) -> {
                            if (loginResponse != null) {
                                if (loginResponse.getToken() != null) {
                                    LoginResponse response = new LoginResponse();
                                    response.setEmail(loginResponse.getEmail());
                                    response.setToken(loginResponse.getToken());
                                    response.setAuthorized(loginResponse.getAuthorized());
                                    response.setHost(loginResponse.getHost());
                                    response.setOk(loginResponse.getOk());

                                    iceWarpDao.insertUser(response)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {

                                                // create shared preference for user log in purpose to hold the login with using of shared preference
                                                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("is_logged_in", true);
                                                editor.putString("userId", "testing");
                                                editor.apply();

                                                Intent intent = new Intent(this, ChannelScreenActivity.class);
                                                startActivity(intent);
                                                finish();


                                            }, throwable -> {
                                                Toast.makeText(this, "Database Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                            });

                                } else {
                                    Toast.makeText(this, "Login not successfully", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(this, "Something went wrong! Please try again later", Toast.LENGTH_LONG).show();
                            }
                            binding.progressBarLogin.setVisibility(View.GONE);

                        }, throwable -> {
                            binding.progressBarLogin.setVisibility(View.GONE);
                    Log.d("Log10", "Error: " + throwable.getMessage());
                    if (throwable.getMessage().equals("HTTP 401 Unauthorized")) {

                        Toast.makeText(this, "Please  insert valid credentials", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    /*
    * below code use for hide keyboard on click of/ touch of anywhere on screen*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null) {
                if (getCurrentFocus() instanceof EditText) {
                    EditText editText = (EditText) getCurrentFocus();
                    int[] coordinates = new int[2];
                    editText.getLocationOnScreen(coordinates);
                    float x = ev.getRawX() + editText.getLeft() - coordinates[0];
                    float y = ev.getRawY() + editText.getTop() - coordinates[1];

                    if (x < editText.getLeft() || x > editText.getRight()
                            || y < editText.getTop() || y > editText.getBottom()) {
                        editText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (this.getCurrentFocus() != null) {
                            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}