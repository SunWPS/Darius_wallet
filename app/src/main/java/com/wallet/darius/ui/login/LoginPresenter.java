package com.wallet.darius.ui.login;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.ui.BasicView;

public class LoginPresenter {

    BasicView basicView;
    FirebaseAuth auth;

    public LoginPresenter(BasicView basicView) {
        this.basicView = basicView;
        auth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> basicView.onSuccess())
                .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
    }

}
