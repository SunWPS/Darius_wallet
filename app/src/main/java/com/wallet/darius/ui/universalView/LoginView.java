package com.wallet.darius.ui.universalView;

import com.wallet.darius.API.WalletAPI;

public interface LoginView {
    void goToNextPage(WalletAPI wallet);
    void onFail(String message);
}
