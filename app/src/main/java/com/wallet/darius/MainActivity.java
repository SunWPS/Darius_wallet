package com.wallet.darius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.Function.DownloadWalletFunction;
import com.wallet.darius.ui.universalView.LoginView;
import com.wallet.darius.ui.dashboard.DashboardActivity;
import com.wallet.darius.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements LoginView {

    private DownloadWalletFunction downloadWalletFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadWalletFunction = new DownloadWalletFunction(this,
                getFilesDir().toString());


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    downloadWalletFunction.load();
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    public void goToDashBoard(WalletAPI wallet) {

        Log.i("xx", "MainActivity " + wallet.getCredential().getAddress());
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putExtra("wallet", wallet);

        startActivity(intent);
        finish();
    }

    @Override
    public void onFail(String message) {

    }

}