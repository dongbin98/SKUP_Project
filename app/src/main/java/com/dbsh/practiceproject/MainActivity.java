package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.StrongBoxUnavailableException;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MainHomeFragment homeFragment;
    MainMenuFramgment menuFragment;
    MainSettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_form);
        Intent intent = getIntent();

        bottomNavigationView = findViewById(R.id.bottomNavi);
        homeFragment = new MainHomeFragment();
        menuFragment = new MainMenuFramgment();
        settingFragment = new MainSettingFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.main_container, homeFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_framgent:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, menuFragment).commit();
                        break;
                    case R.id.home_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
                        break;
                    case R.id.setting_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, settingFragment).commit();
                        break;
                }
                return false;
            }
        });
    }
}
