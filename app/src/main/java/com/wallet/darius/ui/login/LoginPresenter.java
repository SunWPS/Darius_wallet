package com.wallet.darius.ui.login;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.Function.DownloadWalletFunction;
import com.wallet.darius.ui.universalView.LoginView;

public class LoginPresenter {

    private LoginView loginView;
    private FirebaseAuth auth;
    private DownloadWalletFunction downloadWalletFunction;

    public LoginPresenter(LoginView loginView, DownloadWalletFunction downloadWalletFunction) {
        this.loginView = loginView;
        auth = FirebaseAuth.getInstance();
        this.downloadWalletFunction = downloadWalletFunction;
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> downloadWalletFunction.load())
                .addOnFailureListener(e -> loginView.onFail(e.getMessage()));
    }

}
