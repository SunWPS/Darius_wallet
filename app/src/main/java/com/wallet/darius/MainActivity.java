package com.wallet.darius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.internal.InternalTokenProvider;
import com.kenai.jffi.Main;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.Function.DownloadWalletFunction;
import com.wallet.darius.ui.pin.LoginPinActivity;
import com.wallet.darius.ui.universalView.LoginView;
import com.wallet.darius.ui.dashboard.DashboardActivity;
import com.wallet.darius.ui.login.LoginActivity;

import jnr.ffi.annotations.In;

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
    public void goToNextPage(WalletAPI wallet) {
        Intent intent = new Intent(MainActivity.this, LoginPinActivity.class);
        intent.putExtra("wallet", wallet);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFail(String message) {

    }

}