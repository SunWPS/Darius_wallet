package com.wallet.darius.ui.pin.createAndConfirm;

import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wallet.darius.model.user.UserPin;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConfirmPinPresenter {

    private DatabaseReference myRef;
    private FirebaseUser user;

    public ConfirmPinPresenter() {
        this.myRef = FirebaseDatabase.getInstance().getReference("userPin");
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void putPinToDataBase(String pin) {
        UserPin userPin = new UserPin(encryptPin(pin));
        myRef.child(user.getUid()).setValue(userPin);
    }

    public void updatePin(String pin) {
        Map<String, Object> update = new HashMap<>();
        update.put("pin", encryptPin(pin));
        myRef.child(user.getUid()).updateChildren(update);
    }

    private String encryptPin(String pin) {
        return Hashing.sha256()
                .hashString(pin, StandardCharsets.UTF_8).toString();
    }

}
