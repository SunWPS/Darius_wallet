package com.wallet.darius.ui.password;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.ui.universalView.BasicView;

public class ForgotPasswordPresenter {

    BasicView basicView;
    FirebaseAuth auth;

    public ForgotPasswordPresenter(BasicView basicView) {
        this.basicView = basicView;
        auth = FirebaseAuth.getInstance();
    }

    public void sendResetPassLink(String email) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener(aVoid -> basicView.onSuccess())
                .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
    }

}
