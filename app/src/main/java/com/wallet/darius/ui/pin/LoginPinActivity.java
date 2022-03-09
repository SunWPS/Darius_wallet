package com.wallet.darius.ui.pin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.firebase.database.DatabaseReference;
import com.wallet.darius.R;
import com.wallet.darius.ui.dashboard.DashboardActivity;

public class LoginPinActivity extends AppCompatActivity implements LoginPinView{

    private PinView loginPin;
    private TextView pinError;

    private Bundle extras;
    private LoginPinPresenter loginPinPresenter;
    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin);

        loginPin = findViewById(R.id.login_pin);
        pinError = findViewById(R.id.login_pin_error);

        extras = getIntent().getExtras();
        loginPinPresenter = new LoginPinPresenter(this);

        setUpPinView();
    }

    private void setUpPinView() {
        loginPin.requestFocus();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        loginPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 4) {
                    loginPinPresenter.checkPin(charSequence.toString());
                }
                else {
                    pinError.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void isCorrect() {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        Log.i("xx", "correct");
        Intent intent = new Intent(LoginPinActivity.this, DashboardActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
        finish();

    }

    @Override
    public void isNotCorrect() {
        pinError.setText("Pin is Not Match");
    }

    @Override
    public void onFail(String message) {
        Log.i("xx", message);
    }
}