package com.wallet.darius.ui.verifyEmail;

import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.ui.universalView.BasicView;

import java.nio.charset.StandardCharsets;

public class VerifyEmailPresenter {

    private BasicView basicView;
    private FirebaseUser user;

    public VerifyEmailPresenter(BasicView basicView) {
        this.basicView = basicView;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void sendVerifyEmail() {
        user.sendEmailVerification()
                .addOnSuccessListener(unused -> basicView.onSuccess())
                .addOnFailureListener(e -> basicView.onFail(e.getMessage()));
    }

    public WalletAPI getWallet(String password, String filePath) {
        String encryptPass = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8).toString();

        WalletAPI wallet = new WalletAPI(filePath, encryptPass);
        wallet.createWallet();
        return wallet;
    }

    public boolean checkVerify() {
        user.reload();
        return user.isEmailVerified();
    }

}
