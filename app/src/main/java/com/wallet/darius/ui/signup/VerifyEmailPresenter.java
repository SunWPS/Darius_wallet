package com.wallet.darius.ui.signup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wallet.darius.ui.BasicView;

public class VerifyEmailPresenter {

    BasicView basicView;
    FirebaseUser user;

    public VerifyEmailPresenter(BasicView basicView) {
        this.basicView = basicView;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void sendVerifyEmail() {
        user.sendEmailVerification()
                .addOnSuccessListener(unused -> basicView.onSuccess())
                .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
    }
}
