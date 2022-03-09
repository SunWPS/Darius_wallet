package com.wallet.darius.ui.pin;

public interface LoginPinView {
    void isCorrect();
    void isNotCorrect();
    void onFail(String message);
}
