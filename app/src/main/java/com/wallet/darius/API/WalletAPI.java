package com.wallet.darius.API;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wallet.darius.model.itemCard.HistoryCard;
import com.wallet.darius.model.walletDataModel.WalletData;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.Provider;
import java.security.Security;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class WalletAPI implements Parcelable {

    private Web3j web3;
    private File file;
    private Credentials credential;

    private String filePath;
    private String password;
    private String walletName;

    // for create new Wallet
    public WalletAPI(String filePath, String password) {
        this.filePath = filePath;
        this.password = password;
        setUp();
        setupBouncyCastle();
        checkDIR();
    }

    // for DownloadWalletFunction (Main and Login)
    public WalletAPI(String filePath) {
        this.filePath = filePath;
        this.password = "";
        setUp();
        setupBouncyCastle();
        checkDIR();
    }

    // between view
    protected WalletAPI(Parcel in) {
        filePath = in.readString();
        password = in.readString();
        walletName = in.readString();
        setUp();
        loadWallet();
    }

    public static final Creator<WalletAPI> CREATOR = new Creator<WalletAPI>() {
        @Override
        public WalletAPI createFromParcel(Parcel in) {
            return new WalletAPI(in);
        }

        @Override
        public WalletAPI[] newArray(int size) {
            return new WalletAPI[size];
        }
    };

    public void createWallet() {
        try {
            this.walletName = WalletUtils.generateLightNewWalletFile(password, file);
            this.credential = WalletUtils.loadCredentials(password, file + "/" + walletName);
            // load to storage
            putDataAndFile();
        } catch (Exception e) {
            Log.i("xx", "Create wallet Failed");
        }
    }

    private void putDataAndFile() {
        // put data to database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("data");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        WalletData wallet = new WalletData(password, walletName);
        myRef.child(user.getUid()).setValue(wallet);

        // put file to storage
        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("credentials/" + user.getUid() + "/" + walletName);

        storageRef.putFile(Uri.fromFile(new File((file.toString() + "/" + walletName))))
                .addOnSuccessListener(taskSnapshot -> Log.i("xx", "upload file success"))
                .addOnFailureListener(e -> Log.i("xx", e.getMessage()));
    }


    public void loadWallet() {
        try {
            this.credential = WalletUtils.loadCredentials(password, file + "/" + walletName);
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
        }
    }

    public void loadWallet(String walletName) {
        try {
            this.walletName = walletName;
            this.credential = WalletUtils.loadCredentials(password, file + "/" + walletName);
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
        }
    }

    public BigDecimal retrieveBalance() {
        try {
            EthGetBalance balanceWei = web3.ethGetBalance(credential.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger unscaledBalance = balanceWei.getBalance();

            BigDecimal balance = new BigDecimal(unscaledBalance)
                    .divide(new BigDecimal(1000000000000000000L), 8, RoundingMode.HALF_UP);

            return balance;
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
        }
        return new BigDecimal(0);
    }

    public boolean makeTransaction(String toAddress, BigDecimal amount, String network) {
        try {
            long timestamp = System.currentTimeMillis();
            TransactionReceipt receipt = Transfer.sendFunds(web3, credential, toAddress, amount, Convert.Unit.ETHER).send();

            HistoryCard historyCard = new HistoryCard(toAddress, amount.toPlainString(), getFee().toPlainString(),
                    receipt.getTransactionHash(), timestamp);

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("history");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            myRef.child(user.getUid() + "/" + network.toLowerCase() + "/" + UUID.randomUUID().toString().replace("-", ""))
                    .setValue(historyCard);

            return true;
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
            return false;
        }
    }

    public void connectToEthNetwork(String uri) {
        HttpService link = new HttpService(uri);
        web3 = Web3j.build(link);

        try {
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if(!clientVersion.hasError()) {
                Log.i("xx", "ETH network connected");
            } else {
                Log.i("xx", clientVersion.getError().getMessage());
            }
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
        }
    }

    public BigDecimal getFee() {
        try {
            BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();

            BigDecimal fee = new BigDecimal(String.valueOf(gasPrice.multiply(new BigInteger(Transfer.GAS_LIMIT.toString()))))
                    .divide(new BigDecimal("1000000000000000000"))
                    .setScale(9, RoundingMode.CEILING);

            return fee;
        } catch (IOException e) {
            Log.i("xx", e.getMessage());
        }
        return new BigDecimal("0");
    }

    private void setUp() {
        this.file = new File(filePath);
        this.credential = null;
    }
    
    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);

        if (provider == null) {
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            return;
        }

        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    private void checkDIR() {
        if (file.mkdir()) {
            return;
        }
        file.mkdir();
    }

    public Credentials getCredential() {
        return credential;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filePath);
        parcel.writeString(password);
        parcel.writeString(walletName);
//        parcel.writeString(uri);
    }

    public Web3j getWeb3() {
        return web3;
    }
}
