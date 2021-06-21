package com.example.democoordinator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.democoordinator.model.Place;
import com.example.democoordinator.roomdatabase.PlaceDatabase;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class DetailActivity extends AppCompatActivity {
    Button btnMore, btnBookTour;
    CheckBox checkBoxDetail;
    TextView tvDetail;
    CollapsingToolbarLayout collapsingToolbarDetail;
    Toolbar toolbarDetail;
    Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        callFullScreen(this);

        initView();

        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbarDetail.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        mPlace = (Place) getIntent().getSerializableExtra("obj");
        collapsingToolbarDetail.setTitle(mPlace.getName());
        collapsingToolbarDetail.setCollapsedTitleTextColor(Color.WHITE);
        tvDetail.setText(mPlace.getDetail());

        checkBoxDetail.setChecked(mPlace.isFavourite());

        checkBoxDetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlace.setFavourite(isChecked);
                new updateSttFavourite().start();

            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "MORE", Toast.LENGTH_SHORT).show();
            }
        });
        btnBookTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "Book Tour", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        btnMore = findViewById(R.id.btnMore);
        btnBookTour = findViewById(R.id.btnBookTour);
        collapsingToolbarDetail = findViewById(R.id.collapsingDetail);
        toolbarDetail = findViewById(R.id.toolBarDetail);
        tvDetail = findViewById(R.id.tvDetail);
        checkBoxDetail = findViewById(R.id.checkBoxDetail);
    }

    public void callFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private class updateSttFavourite extends Thread {
        @Override
        public void run() {
            super.run();
            PlaceDatabase.getInstance(DetailActivity.this).placeDAO().updateSttFavorite(mPlace.isFavourite(), mPlace.getId());
        }
    }

}

