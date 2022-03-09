package com.wallet.darius.ui.changeEmail;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wallet.darius.ui.universalView.BasicView;

public class ChangeEmailPresenter {

    BasicView basicView;
    FirebaseUser user;

    public ChangeEmailPresenter(BasicView basicView) {
        this.basicView = basicView;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void changeEmail(String email) {
        user.updateEmail(email).addOnSuccessListener(unused -> {
            basicView.onSuccess();
        }).addOnFailureListener(e -> {
            basicView.onFail(e.getMessage());
        });

        user.reload();
    }

    public String getCurrentEmail() {
        return user.getEmail();
    }

}
