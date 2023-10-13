package com.ead.train_management;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ead.train_management.models.disable;
import com.ead.train_management.models.user;
import com.ead.train_management.models.userRes;
import com.ead.train_management.service.LoginService;
import com.ead.train_management.util.DatabaseHelper;
import com.ead.train_management.util.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class ProfileActivity extends AppCompatActivity {

    private LoginService loginService;
    private  String nic = "";
    private String userid = "";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    EditText firstname;
    EditText lastname;
    EditText phone_number;
    Button uploadButton;
    Button deactivateButton;
    Button logoutButton;
    EditText date;
    @SuppressLint({"Range", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch(item.getItemId())
            {
                case R.id.book:
                    startActivity(new Intent(getApplicationContext(),BookingActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.home:
                    return true;
                case R.id.view:
                    startActivity(new Intent(getApplicationContext(),ViewActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });



        firstname = findViewById(R.id.fname2);
        lastname = findViewById(R.id.lname2);
        phone_number = findViewById(R.id.phone2);
        date = findViewById(R.id.date2);
        uploadButton = findViewById(R.id.upButton);
        deactivateButton = findViewById(R.id.rmButton);
        logoutButton = findViewById(R.id.lgButton);
        loginService =  RetrofitClient.getClient().create(LoginService.class);
        dbHelper = new DatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        String[] projection = {
                "nic",
                "userid"
        };

        cursor = db.query(
                "users",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            nic = cursor.getString(cursor.getColumnIndex("nic"));
            userid  = cursor.getString(cursor.getColumnIndex("userid"));

        }



        Call<userRes> data = loginService.getUserProfile(nic);

        data.enqueue(new Callback<userRes>() {
            @Override
            public void onResponse(Call<userRes> call1, Response<userRes> response1) {

                if (response1.isSuccessful() && response1.body() != null) {

                    userRes res = response1.body();

                    firstname.setText(res.getFname());
                    lastname.setText(res.getLname());
                    phone_number.setText(res.getPhone());
                    date.setText(res.getDate());
                } else {
                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<userRes> call, Throwable t) {

                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( firstname.getText().toString().equals("")&& lastname.getText().toString().equals("")&& phone_number.getText().toString().equals("")) {
                    Toast.makeText(ProfileActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                } else {

                    userRes pa = new userRes();
                    pa.setAcc(true);
                    pa.setNic(nic);
                    pa.setPhone(phone_number.getText().toString());
                    pa.setFname(firstname.getText().toString());
                    pa.setLname(lastname.getText().toString());
                    pa.setDate(date.getText().toString());
                    pa.setId(userid);
                    Call<userRes> call = loginService.Update(pa);
                    call.enqueue(new Callback<userRes>() {
                        @Override
                        public void onResponse(Call<userRes> call, Response<userRes> response) {

                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(ProfileActivity.this, "Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(intent);

                            } else {

                                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<userRes> call, Throwable t) {

                            Toast.makeText(ProfileActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }


    public void LogOut(View view) {

        int deletedRows = db.delete("users", null, null);
        cursor.close();
        dbHelper.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Disable(View view) {
        disable d = new disable();
        d.setTrv_account(false);
        Call<userRes> data = loginService.Dis(nic,d);

        data.enqueue(new Callback<userRes>() {
            @Override
            public void onResponse(Call<userRes> call1, Response<userRes> response1) {

                if (response1.isSuccessful() && response1.body() != null) {
                    LogOut(view);
                } else {
                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<userRes> call, Throwable t) {

                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}