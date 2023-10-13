package com.ead.train_management;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ead.train_management.models.login;
import com.ead.train_management.models.loginRes;
import com.ead.train_management.models.userRes;
import com.ead.train_management.service.LoginService;
import com.ead.train_management.util.DatabaseHelper;
import com.ead.train_management.util.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private LoginService lgService;
    EditText username;
    EditText password;
    Button loginButton;
    @Override
    //Declaring variables required for the Login process
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        lgService =  RetrofitClient.getClient().create(LoginService.class);
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Login button onclick function
            public void onClick(View view) {
                if (username.getText().toString().equals("") && password.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    login loginRequest = new login();
                    loginRequest.setNic(username.getText().toString());
                    loginRequest.setUserPassword(password.getText().toString());

                    Call<loginRes> call = lgService.Login(loginRequest);
                    call.enqueue(new Callback<loginRes>() {
                        @Override
                        //Login button click responses
                        public void onResponse(Call<loginRes> call, Response<loginRes> response) {

                            if (response.isSuccessful() && response.body() != null) {
                                loginRes userResponse = response.body();
                                if(userResponse.getRole().equals("traveler")) {
                                    Call<userRes> data = lgService.getUserProfile(userResponse.getNic());

                                    data.enqueue(new Callback<userRes>() {
                                        @Override
                                        public void onResponse(Call<userRes> call1, Response<userRes> response1) {

                                            if (response1.isSuccessful() && response1.body() != null) {

                                                userRes res = response1.body();

                                                if (res.isAcc()) {

                                                    ContentValues values = new ContentValues();
                                                    values.put("nic", userResponse.getNic());
                                                    values.put("uid", res.getId());
                                                    long newRowId = db.insert("users", null, values);

                                                    if (newRowId != -1) {
                                                        Toast.makeText(MainActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                                    }

                                                }else{
                                                    Toast.makeText(MainActivity.this, "Your Account is disabled", Toast.LENGTH_SHORT).show();
                                                }

                                            } else {
                                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<userRes> call, Throwable t) {

                                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(MainActivity.this, "Invalid Account Type", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<loginRes> call, Throwable t) {

                            Toast.makeText(MainActivity.this, "Failed to Sign in!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }

    public void navigateToReg(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }





}