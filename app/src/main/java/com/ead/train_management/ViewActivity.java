package com.ead.train_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.ead.train_management.models.userRes;
import com.ead.train_management.models.viewBooking;
import com.ead.train_management.service.BookingService;
import com.ead.train_management.service.LoginService;
import com.ead.train_management.util.DatabaseHelper;
import com.ead.train_management.util.MyAdapter;
import com.ead.train_management.util.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class ViewActivity extends AppCompatActivity {
    private BookingService bookingService;
    private  String nic = "";
    private String userid = "";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    @SuppressLint({"NonConstantResourceId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.view);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch(item.getItemId())
            {
                case R.id.book:
                    startActivity(new Intent(getApplicationContext(),BookingActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.view:
                    return true;
            }
            return false;
        });

        bookingService =  RetrofitClient.getClient().create(BookingService.class);
        dbHelper = new DatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        String[] projection = {
                "nic",
                "uid"
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
            userid  = cursor.getString(cursor.getColumnIndex("uid"));

        }


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<List<viewBooking>> data = bookingService.getBooking(nic);

        data.enqueue(new Callback<List<viewBooking>>() {
            @Override
            public void onResponse(Call<List<viewBooking>> call1, Response<List<viewBooking>> response1) {

                if (response1.isSuccessful() && response1.body() != null) {

                    List<viewBooking> dataList = response1.body();
                    dataList.removeIf(viewBooking::isCc);
                    MyAdapter adapter = new MyAdapter(dataList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<viewBooking>> call, Throwable t) {

                Toast.makeText(ViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}