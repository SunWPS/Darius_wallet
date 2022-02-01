package com.wallet.darius.ui.Password;

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

    public void resetPass(String newPass){
        user.updatePassword(newPass).addOnSuccessListener(aVoid -> basicView.onSuccess())
                .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
    }

}
