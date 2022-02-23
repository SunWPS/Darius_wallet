package com.wallet.darius.model.walletDataModel;

public class WalletData {
    private String password;
    private String walletName;

    public WalletData(String password, String walletName) {
        this.password = password;
        this.walletName = walletName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
