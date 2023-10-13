package com.ead.train_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ead.train_management.models.login;
import com.ead.train_management.models.user;
import com.ead.train_management.models.userRes;
import com.ead.train_management.service.LoginService;
import com.ead.train_management.util.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private LoginService loginService;
    EditText nic;
    EditText password;
    EditText firstname;
    EditText lastname;
    EditText contactNumber;
    Button registratioinButton;
    @Override
    //Declaring variables required for Signup
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nic = findViewById(R.id.nic1);
        password = findViewById(R.id.password1);
        firstname = findViewById(R.id.fname1);
        lastname = findViewById(R.id.lname1);
        contactNumber = findViewById(R.id.phone1);
        registratioinButton = findViewById(R.id.regButton);
        loginService =  RetrofitClient.getClient().create(LoginService.class);
        registratioinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Signup Button Click Function
            public void onClick(View view) {
                if (nic.getText().toString().equals("") && password.getText().toString().equals("")&& firstname.getText().toString().equals("")&& lastname.getText().toString().equals("")&& contactNumber.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {

                    user us = new user();
                    user.UserInfo usi = us.new UserInfo();
                    us.setAcc(true);
                    us.setNic(nic.getText().toString());
                    us.setPhone(contactNumber.getText().toString());
                    us.setFname(firstname.getText().toString());
                    us.setLname(lastname.getText().toString());
                    usi.setPassword(password.getText().toString());
                    usi.setRole("traveler");
                    us.setData(usi);
                    Call<userRes> call = loginService.Reg(us);
                    call.enqueue(new Callback<userRes>() {
                        @Override
                        //Signup Button click responses
                        public void onResponse(Call<userRes> call, Response<userRes> response) {

                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(RegisterActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            } else {

                                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<userRes> call, Throwable t) {

                            Toast.makeText(RegisterActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }

    public void navigateToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}