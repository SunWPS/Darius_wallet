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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.Provider;
import java.security.Security;

public class WalletAPI implements Parcelable {

    private HttpService link;
    private Web3j web3;
    private File file;
    private Credentials credential;

    private String filePath;
    private String password;
    private String walletName;
    private String uri;

    public WalletAPI(String filePath, String password, String uri) {
        this.filePath = filePath;
        this.password = password;
        this.walletName = "";
        this.uri = uri;
        setUp();
        setupBouncyCastle();
        checkDIR();
        connectToEthNetwork();
    }

    public WalletAPI(String filePath, String uri) {
        this.filePath = filePath;
        this.password = "";
        this.walletName = "";
        this.uri = uri;
        setUp();
        setupBouncyCastle();
        checkDIR();
        connectToEthNetwork();
    }

    protected WalletAPI(Parcel in) {
        filePath = in.readString();
        password = in.readString();
        walletName = in.readString();
        uri = in.readString();
        setUp();
        connectToEthNetwork();
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
            Log.i("xx", "passwor " + password + "  address " + credential.getAddress() );
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
            Log.i("xx", "loadwallet " + password);
            this.credential = WalletUtils.loadCredentials(password, file + "/" + walletName);
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
        }
    }

    public BigDecimal retrieveBalance() {
        try {
            EthGetBalance balanceWei = web3.ethGetBalance(credential.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger unscaledBalance = balanceWei.getBalance();

            BigDecimal balance = Convert.fromWei(unscaledBalance.toString(), Convert.Unit.ETHER)
                    .setScale(4, BigDecimal.ROUND_UP);

            return  balance;
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
        }
        return new BigDecimal(0);
    }

    public boolean makeTransaction(Double amount, String toAddress) {
        try {
            TransactionReceipt receipt = Transfer.sendFunds(web3, credential, toAddress, BigDecimal.valueOf(amount), Convert.Unit.ETHER).send();
            return true;
        } catch (Exception e) {
            Log.i("xx", e.getMessage());
            return false;
        }
    }

    // TODO: change network by change link of api and pass network to the parameter of function
    public void connectToEthNetwork() {
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

    public Credentials getCredential() {
        return credential;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void setUp() {
        this.link = new HttpService(uri);
        this.web3 = Web3j.build(link);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filePath);
        parcel.writeString(password);
        parcel.writeString(walletName);
        parcel.writeString(uri);
    }

    public String toString() {
        return credential.getAddress() + " Balance:" + retrieveBalance();
    }
}
