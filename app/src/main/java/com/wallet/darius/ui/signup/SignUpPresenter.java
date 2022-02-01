package com.wallet.darius.ui.signup;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.ui.BasicView;

public class SignUpPresenter {
    BasicView basicView;
    FirebaseAuth auth;

    public SignUpPresenter(BasicView basicView) {
        this.basicView = basicView;
        auth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> basicView.onSuccess())
                .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
    }

}
