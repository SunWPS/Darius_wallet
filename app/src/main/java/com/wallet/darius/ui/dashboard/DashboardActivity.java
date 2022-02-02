package com.wallet.darius.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.wallet.darius.R;
import com.wallet.darius.ui.login.LoginActivity;
import com.wallet.darius.ui.password.ResetPasswordActivity;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DashboardView{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolBar;
    TextView menuEmail, usdPrice, percentChange1h, percentChange24h, percentChange7d;
    ConstraintLayout trackingView;
    ImageView refreshTracking;

    DashboardPresenter dashboardPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardPresenter = new DashboardPresenter(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolBar = findViewById(R.id.tool_bar);

        menuSetUpTracking();
        menuSetUp();
        dashboardPresenter.getEthTracking();
    }

    private void menuSetUpTracking() {
        trackingView = findViewById(R.id.trackingView);
        usdPrice = trackingView.findViewById(R.id.usdPrice);
        percentChange1h = trackingView.findViewById(R.id.percent_change_1H);
        percentChange24h = trackingView.findViewById(R.id.percent_change_24H);
        percentChange7d = trackingView.findViewById(R.id.percent_change_7D);
        refreshTracking = trackingView.findViewById(R.id.refresh_tracking);

        refreshTracking.setOnClickListener(view -> {
            dashboardPresenter.getEthTracking();
            Log.i("xx", "click");
        });
    }

    public void menuSetUp() {

        View header = navigationView.getHeaderView(0);
        menuEmail = header.findViewById(R.id.menu_email);
        menuEmail.setText(dashboardPresenter.getUserEmail());

        // toolbar
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(null);
        navigationView.setItemIconTintList(null);

        // Navigation Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_change_pass);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_change_pass:
                startActivity(new Intent(DashboardActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.nav_logout:
                dashboardPresenter.userSignOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTextToTracking(Double price, Double changeIn1H, Double changeIn24H, Double changeIn7D) {

        int red = getResources().getColor(R.color.red);
        int green = getResources().getColor(R.color.green);

        usdPrice.setText(String.format("%.4f", price) + "$");

        percentChange1h.setText((changeIn1H > 0 ? "+": "") + String.format("%.2f", changeIn1H) + "%" );
        percentChange1h.setTextColor(changeIn1H >= 0 ? green: red);

        percentChange24h.setText((changeIn24H > 0 ? "+": "") + String.format("%.2f", changeIn24H) + "%");
        percentChange24h.setTextColor(changeIn24H >= 0 ? green: red);

        percentChange7d.setText((changeIn7D > 0 ? "+": "") + String.format("%.2f", changeIn7D) + "%");
        percentChange7d.setTextColor(changeIn7D >= 0 ? green: red);
    }
}