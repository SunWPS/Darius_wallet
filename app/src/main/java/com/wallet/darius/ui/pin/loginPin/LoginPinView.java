package com.wallet.darius.ui.pin.loginPin;

public interface LoginPinView {
    void isCorrect();
    void isNotCorrect();
    void onFail(String message);
}
