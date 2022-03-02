package com.wallet.darius.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.R;
import com.wallet.darius.ui.depositAndTransfer.DepositActivity;
import com.wallet.darius.ui.favorite.FavoriteActivity;
import com.wallet.darius.ui.history.HistoryActivity;
import com.wallet.darius.ui.login.LoginActivity;
import com.wallet.darius.ui.password.ResetPasswordActivity;
import com.wallet.darius.ui.depositAndTransfer.TransferActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jnr.ffi.annotations.In;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DashboardView{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolBar;
    private TextView menuEmail, usdPrice, percentChange1h, percentChange24h,
            percentChange7d, balance;
    private ConstraintLayout trackingView;
    private ImageView refreshTracking, refreshBalance;
    private Button depositBtn, transferBtn, favoriteBtn, historyBtn;
    private Spinner networkSpinner;

    private DashboardPresenter dashboardPresenter;
    private WalletAPI myWallet;
    private ApplicationInfo ai;
    private Bundle metaData;
    private String selectedNetwork, networkLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        metaData = ai.metaData;
        dashboardPresenter = new DashboardPresenter(this, metaData);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolBar = findViewById(R.id.tool_bar);
        depositBtn = findViewById(R.id.db_deposit_btn);
        transferBtn = findViewById(R.id.db_transfer_btn);
        favoriteBtn = findViewById(R.id.db_favorite_btn);
        historyBtn = findViewById(R.id.db_history_btn);
        balance = findViewById(R.id.db_balance_text);
        refreshBalance = findViewById(R.id.db_refresh_balance);
        trackingView = findViewById(R.id.db_tracking_view);
        networkSpinner = findViewById(R.id.db_network_dropdown);
        usdPrice = trackingView.findViewById(R.id.db_usd_price);
        percentChange1h = trackingView.findViewById(R.id.db_percent_change_1H);
        percentChange24h = trackingView.findViewById(R.id.db_percent_change_24H);
        percentChange7d = trackingView.findViewById(R.id.db_percent_change_7D);
        refreshTracking = trackingView.findViewById(R.id.db_refresh_tracking);

        Bundle extras = getIntent().getExtras();
        myWallet = extras.getParcelable("wallet");

        setUpPolicy();
        setUpButton();
        menuSetUp();
        setUpNetworkSpinner();

        dashboardPresenter.getEthTracking();

        setUpTracking();
    }

    private void setUpNetworkSpinner() {
        List<String> netWorkList = new ArrayList<>();
        netWorkList.add("Rinkeby");
        netWorkList.add("Mainnet");
        netWorkList.add("Kovan");
        netWorkList.add("Gorli");

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_selected, netWorkList);
        adapter.setDropDownViewResource(R.layout.dropdown_items);

        networkSpinner.setAdapter(adapter);

        networkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNetwork = networkSpinner.getSelectedItem().toString().toLowerCase();
                networkLink = metaData.getString(selectedNetwork);

                myWallet.connectToEthNetwork(networkLink);
                balance.setText(myWallet.retrieveBalance().setScale(4, BigDecimal.ROUND_UP).toPlainString() + " ETH");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // pass
            }
        });
    }

    private void setUpPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setUpButton() {
        refreshBalance.setOnClickListener(view -> {
            balance.setText("" + myWallet.retrieveBalance().setScale(4, BigDecimal.ROUND_UP) + " ETH");
        });

        depositBtn.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, DepositActivity.class);
            intent.putExtra("balance", myWallet.retrieveBalance().toPlainString());
            intent.putExtra("address", myWallet.getCredential().getAddress());
            intent.putExtra("network", selectedNetwork);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        transferBtn.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, TransferActivity.class);
            intent.putExtra("wallet", myWallet);
            intent.putExtra("network", selectedNetwork);
            intent.putExtra("networkLink", networkLink);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        favoriteBtn.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, FavoriteActivity.class);
            intent.putExtra("wallet", myWallet);
            intent.putExtra("network", selectedNetwork);
            intent.putExtra("networkLink", networkLink);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        historyBtn.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
            intent.putExtra("myAddress", myWallet.getCredential().getAddress());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void setUpTracking() {
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
                dashboardPresenter.userSignOut(getFilesDir() + "/" + myWallet.getWalletName());
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

        usdPrice.setText(String.format("%.4f", price) + " $");

        percentChange1h.setText((changeIn1H > 0 ? "+": "") + String.format("%.2f", changeIn1H) + "%" );
        percentChange1h.setTextColor(changeIn1H >= 0 ? green: red);

        percentChange24h.setText((changeIn24H > 0 ? "+": "") + String.format("%.2f", changeIn24H) + "%");
        percentChange24h.setTextColor(changeIn24H >= 0 ? green: red);

        percentChange7d.setText((changeIn7D > 0 ? "+": "") + String.format("%.2f", changeIn7D) + "%");
        percentChange7d.setTextColor(changeIn7D >= 0 ? green: red);

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("xx", "start");
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        if (myWallet.getWeb3() != null) {
            balance.setText("" + myWallet.retrieveBalance().setScale(4, BigDecimal.ROUND_UP) + " ETH");
        }
    }
}