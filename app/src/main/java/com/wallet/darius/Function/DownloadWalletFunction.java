package com.wallet.darius.Function;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.model.walletDataModel.WalletData;
import com.wallet.darius.ui.UniversalView.LoginView;

import java.io.File;

public class DownloadWalletFunction {

    private LoginView loginView;
    private FirebaseUser user;
    private String filePath;
    private WalletAPI wallet;
    private Gson gson;
    private String password;
    private String walletName;

    public DownloadWalletFunction(LoginView loginView, String filePath, String uri) {
        this.loginView = loginView;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.filePath = filePath;
        this.wallet = new WalletAPI(filePath, uri);
        this.gson = new Gson();
        this.password = "";
        walletName = "";
    }


    public void load() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("data/" + user.getUid());


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WalletData data = gson.fromJson(snapshot.getValue().toString(),
                        WalletData.class);


                password = data.getPassword();
                walletName = data.getWalletName();

                if (!(new File(filePath +  "/" + walletName).exists())) {
                    downloadCredential();
                } else {
                    walletSetup();
                    loginView.goToDashBoard(wallet);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("xx", error.getMessage());
            }
        });
    }

    public void downloadCredential() {
        StorageReference stRf = FirebaseStorage.getInstance()
                .getReference("credentials/" + user.getUid() + "/" + walletName);
        File localFile = new File(filePath + "/" + walletName);

        stRf.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    walletSetup();
                    loginView.goToDashBoard(wallet);
                })
                .addOnFailureListener(e -> Log.i("xx", e.getMessage()));
    }

    public void walletSetup() {
        if (!password.isEmpty() && !walletName.isEmpty())
        wallet.setPassword(password);
        wallet.loadWallet(walletName);
    }

}
