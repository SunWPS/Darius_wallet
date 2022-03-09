package com.wallet.darius.ui.pin;

import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wallet.darius.model.user.UserPin;

import java.nio.charset.StandardCharsets;

public class ConfirmPinPresenter {

    private DatabaseReference myRef;
    private FirebaseUser user;

    public ConfirmPinPresenter() {
        this.myRef = FirebaseDatabase.getInstance().getReference("userPin");
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void putPinToDataBase(String pin) {
        String encryptPin = Hashing.sha256()
                .hashString(pin, StandardCharsets.UTF_8).toString();

        UserPin userPin = new UserPin(encryptPin);

        myRef.child(user.getUid()).setValue(userPin);
    }

}
