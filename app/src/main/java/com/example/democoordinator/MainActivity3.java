package com.example.democoordinator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.democoordinator.adapter.AdapterMain;
import com.example.democoordinator.fragment.MyFragmentDialog;
import com.example.democoordinator.model.Place;
import com.example.democoordinator.roomdatabase.PlaceDatabase;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements MyFragmentDialog.IsendData {
    RecyclerView rvDemo;
    ArrayList<Place> mList = new ArrayList<>();
    AdapterMain adapterMain;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbarDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Log.d("TAG","onCreateMain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        callFullScreen(this);

        initView();
        setSupportActionBar(toolbarDemo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbarDemo.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbarDemo.getOverflowIcon().setTint(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        adapterMain = new AdapterMain(mList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvDemo.setAdapter(adapterMain);
        rvDemo.setLayoutManager(layoutManager);
        adapterMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity3.this, DetailActivity.class);
                intent.putExtra("obj", mList.get(position));
                startActivity(intent);

            }
        });

        new getData().start();
    }

    private void initView() {
        rvDemo = findViewById(R.id.rvDemo);
        collapsingToolbarLayout = findViewById(R.id.collapsingDemo);
        toolbarDemo = findViewById(R.id.toolBarDemo);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                MyFragmentDialog myFragmentDialog = new MyFragmentDialog();
                myFragmentDialog.show(getSupportFragmentManager(), null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendData(Place place) {
        mList.add(place);
        MainActivity3.this.runOnUiThread(new Runnable() {
                public void run() {
                    adapterMain.notifyDataSetChanged();
                }
            });
    }

    private class getData extends Thread {
        @Override
        public void run() {
            super.run();
            List<Place> listTemp = PlaceDatabase.getInstance(getApplicationContext()).placeDAO().getListPlace();
            mList.addAll(listTemp);
            MainActivity3.this.runOnUiThread(new Runnable() {
                public void run() {
                    adapterMain.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG","onStopMain");
        mList.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG","onRestartMain");
        new getData().start();
    }

    public void callFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}