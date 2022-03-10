package com.wallet.darius.ui.pin.loginPin;

import androidx.annotation.NonNull;

import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wallet.darius.model.user.UserPin;

import java.nio.charset.StandardCharsets;


public class LoginPinPresenter {
    private DatabaseReference myRef;
    private FirebaseUser user;
    private LoginPinView loginPinView;

    public  LoginPinPresenter(LoginPinView loginPinView) {
        this.myRef = FirebaseDatabase.getInstance().getReference("userPin");
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.loginPinView = loginPinView;
    }


    public void checkPin(String inputPin) {
        myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Gson gson = new Gson();
                UserPin userPin = gson.fromJson(snapshot.getValue().toString(), UserPin.class);

                String encryptPin = Hashing.sha256()
                        .hashString(inputPin, StandardCharsets.UTF_8).toString();

                if (userPin.getPin().equals(encryptPin)) {
                    loginPinView.isCorrect();
                }
                else {
                    loginPinView.isNotCorrect();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loginPinView.onFail(error.getMessage());
            }
        });
    }
}
