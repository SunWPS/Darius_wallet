package com.wallet.darius.ui.UniversalView;

import com.wallet.darius.API.WalletAPI;

public interface LoginView {
    void goToDashBoard(WalletAPI wallet);
    void onFail(String message);
}