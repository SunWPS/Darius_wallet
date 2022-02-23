package com.wallet.darius.ui.signup;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wallet.darius.API.CoinInfoAPI;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.ui.UniversalView.BasicView;

import java.nio.charset.StandardCharsets;

public class VerifyEmailPresenter {

    private BasicView basicView;
    private FirebaseUser user;
    private ApplicationInfo ai;

    public VerifyEmailPresenter(BasicView basicView, ApplicationInfo ai) {
        this.basicView = basicView;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void sendVerifyEmail() {
        user.sendEmailVerification()
                .addOnSuccessListener(unused -> basicView.onSuccess())
                .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
    }

    public WalletAPI getWallet(String password, String filePath) {
        String encryptPass = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8).toString();


        Bundle bundle = ai.metaData;

        WalletAPI wallet = new WalletAPI(filePath, encryptPass, bundle.getString("infuraURI"));
        wallet.createWallet();
        return wallet;
    }

}
