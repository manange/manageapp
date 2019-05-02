package com.example.manageapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // kita set default nya Home Fragment
        replaceFragment(new HomeFragment());
        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void replaceFragment(Fragment frg){
        FragmentTransaction fm= getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fl_container,frg);
        fm.addToBackStack(null);
        fm.commit();
        Log.d("Replace Frag","replace "+frg.getTag());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home_menu:
               replaceFragment(new HomeFragment());
                break;
            case R.id.search_menu:
                replaceFragment(new TaskFragment());
                break;
            case R.id.favorite_menu:
                replaceFragment(new StatsFragment());
                break;
            case R.id.account_menu:
                replaceFragment(new AccountFragment());
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.menu_main_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
