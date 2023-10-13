package com.ead.train_management;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.ead.train_management.models.booking;
import com.ead.train_management.models.disable;
import com.ead.train_management.models.train;
import com.ead.train_management.models.userRes;
import com.ead.train_management.service.BookingService;
import com.ead.train_management.service.LoginService;
import com.ead.train_management.util.DatabaseHelper;
import com.ead.train_management.util.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    // Declare class variables
    private BookingService bookingService;
    private  String nic = "";
    private String userid = "";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    EditText traveler_name;
    EditText email;
    EditText passengerCount;
    EditText res_date;
    EditText contactNumber;
    Button addButton;
    Spinner spinner;
    private int year, month, day, hour, minute;

    // This method is called when the activity is created
    @SuppressLint({"Range", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //Initializing variables to the DatePicker
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.book);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch(item.getItemId())
            {
                case R.id.book:
                    return true;
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.view:
                    startActivity(new Intent(getApplicationContext(),ViewActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        //Initializing other variables
        traveler_name = findViewById(R.id.name3);
        spinner = findViewById(R.id.spinner);
        contactNumber = findViewById(R.id.phone3);
        res_date = findViewById(R.id.date3);
        email = findViewById(R.id.email3);
        passengerCount = findViewById(R.id.num3);
        addButton = findViewById(R.id.addButton);
        bookingService =  RetrofitClient.getClient().create(BookingService.class);
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

        Call<List<train>> data = bookingService.getTrain();

        data.enqueue(new Callback<List<train>> () {
            @Override
            public void onResponse(Call<List<train>>  call1, Response<List<train>>  response1) {
                if (response1.isSuccessful() && response1.body() != null) {
                    List<train> responseData = response1.body();
                    List<String> dt = new ArrayList<>();

                    for(train d:responseData)
                    {
                        dt.add(d.getTidc());
                    }
                    populateSpinner(dt);
                } else {
                    Toast.makeText(BookingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<train>>  call, Throwable t) {

                Toast.makeText(BookingActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Add Booking Button Click
            public void onClick(View view) {
                if ( traveler_name.getText().toString().equals("")&& email.getText().toString().equals("")&& contactNumber.getText().toString().equals("")) {
                    Toast.makeText(BookingActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                } else {
                    String selectedValue ="";
                    if(spinner.getSelectedItem()!=null){
                        selectedValue = spinner.getSelectedItem().toString();
                    }
                    booking bg = new booking ();

                    bg.setReferenceId(nic);
                    bg.setTravelerId(userid);
                    bg.setStatus(false);
                    bg.setTrainName(selectedValue);
                    bg.setReservationDate(res_date.getText().toString());
                    bg.setPhoneNumber(contactNumber.getText().toString());
                    bg.setEmail(email.getText().toString());
                    bg.setTravelerName(traveler_name.getText().toString());
                    bg.setPassengerCount(Integer.parseInt(passengerCount.getText().toString()));
                    Call<String> call = bookingService.createBooking(bg);
                    call.enqueue(new Callback<String>() {
                        @Override
                        //Add Booking Button click response function
                        public void onResponse(Call<String> call, Response<String> response1) {

                            if (response1.isSuccessful() && response1.body() != null) {
                                Toast.makeText(BookingActivity.this, "Reserved Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
                                startActivity(intent);

                            } else {

                                Toast.makeText(BookingActivity.this, "Process Canceled.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(BookingActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }

    //Data loading part in the Spinner
    private void populateSpinner(List<String> trainClassList) {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trainClassList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



}