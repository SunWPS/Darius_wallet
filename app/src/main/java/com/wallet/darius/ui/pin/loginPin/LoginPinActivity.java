package com.wallet.darius.ui.pin.loginPin;

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
import com.wallet.darius.R;
import com.wallet.darius.ui.dashboard.DashboardActivity;

public class LoginPinActivity extends AppCompatActivity implements LoginPinView{

    private PinView loginPin;
    private TextView pinError, oneBtn, twoBtn, threeBtn, fourBtn,
            fiveBtn, sixBtn, sevenBtn, eightBtn, nineBtn, zeroBtn, delBtn;

    private Bundle extras;
    private LoginPinPresenter loginPinPresenter;
    private String inputPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin);

        loginPin = findViewById(R.id.login_pin);
        pinError = findViewById(R.id.login_pin_error);

        extras = getIntent().getExtras();
        loginPinPresenter = new LoginPinPresenter(this);

        setUpPinView();
        setUpKeyboard();
    }

    private void setUpKeyboard() {
        inputPin = "";
        oneBtn = findViewById(R.id.one_btn);
        twoBtn = findViewById(R.id.two_btn);
        threeBtn = findViewById(R.id.three_btn);
        fourBtn = findViewById(R.id.four_btn);
        fiveBtn = findViewById(R.id.five_btn);
        sixBtn = findViewById(R.id.six_btn);
        sevenBtn = findViewById(R.id.seven_btn);
        eightBtn = findViewById(R.id.eight_btn);
        nineBtn = findViewById(R.id.nine_btn);
        zeroBtn = findViewById(R.id.zero_btn);
        delBtn = findViewById(R.id.del_btn);

        oneBtn.setOnClickListener(view -> addPin(1));
        twoBtn.setOnClickListener(view -> addPin(2));
        threeBtn.setOnClickListener(view -> addPin(3));
        fourBtn.setOnClickListener(view -> addPin(4));
        fiveBtn.setOnClickListener(view -> addPin(5));
        sixBtn.setOnClickListener(view -> addPin(6));
        sevenBtn.setOnClickListener(view -> addPin(7));
        eightBtn.setOnClickListener(view -> addPin(8));
        nineBtn.setOnClickListener(view -> addPin(9));
        zeroBtn.setOnClickListener(view -> addPin(0));
        delBtn.setOnClickListener(view -> delete());
    }

    private void addPin(int number) {
        if (inputPin.length() < 4) {
            inputPin += ""+number;
            loginPin.setText(inputPin);
        }
    }

    private void delete() {
        if(inputPin.length() > 0) {
            inputPin = inputPin.substring(0, inputPin.length()-1);
            loginPin.setText(inputPin);
        }
    }

    private void setUpPinView() {
        loginPin.requestFocus();

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