package com.wallet.darius.ui.password;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wallet.darius.ui.BasicView;

public class ResetPasswordPresenter {

    BasicView basicView;
    FirebaseUser user;

    public ResetPasswordPresenter(BasicView basicView) {
        this.basicView = basicView;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void resetPass(String currentPass, String newPass){
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);

        user.reauthenticate(credential)
                .addOnSuccessListener(unused -> {
                    user.updatePassword(newPass)
                            .addOnSuccessListener(aVoid -> basicView.onSuccess())
                            .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
                }).addOnFailureListener(e -> basicView.onFail(e.getMessage()));

    }

}
