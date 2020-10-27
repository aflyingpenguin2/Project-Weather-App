package com.example.waver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SearchScreen extends AppCompatActivity {

    FloatingActionButton btnSearch;
    EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        btnSearch = findViewById(R.id.floating_search);
        editTextSearch = findViewById(R.id.edittext_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchScreen.this, MainActivity.class);
                String city = editTextSearch.getText().toString();
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });
    }


}