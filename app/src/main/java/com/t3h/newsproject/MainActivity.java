package com.t3h.newsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.t3h.newsproject.fragment.FavoriteFragment;
import com.t3h.newsproject.fragment.NewsFragment;
import com.t3h.newsproject.fragment.SaveFragment;
import com.t3h.newsproject.model.PagerAdapterNews;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerAdapter adapter;
    private FavoriteFragment fmFavorite = new FavoriteFragment();
    private SaveFragment fmSave = new SaveFragment();
    private NewsFragment fmNews = new NewsFragment();
    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()) {
            initView();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            initView();
        } else {
            finish();
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : PERMISSIONS) {
                int check = checkSelfPermission(p);
                if (check != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        adapter = new PagerAdapterNews(getSupportFragmentManager(), fmNews, fmSave, fmFavorite);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.lg_newspaper_padding);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    public FavoriteFragment getFmFavorite() {
        return fmFavorite;
    }

    public SaveFragment getFmSave() {
        return fmSave;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
