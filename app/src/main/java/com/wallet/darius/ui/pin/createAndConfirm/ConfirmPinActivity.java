package com.wallet.darius.ui.pin.createAndConfirm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.wallet.darius.R;
import com.wallet.darius.ui.dashboard.DashboardActivity;

public class ConfirmPinActivity extends AppCompatActivity {

    private PinView confirmPin;
    private TextView pinError, oneBtn, twoBtn, threeBtn, fourBtn,
            fiveBtn, sixBtn, sevenBtn, eightBtn, nineBtn, zeroBtn, delBtn;

    private String pin, inputPin;
    private Bundle extras;
    private ConfirmPinPresenter pinPresenter;
    private int check;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin);

        confirmPin = findViewById(R.id.confirm_pin);
        pinError = findViewById(R.id.pin_error);

        extras = getIntent().getExtras();
        pin = extras.getString("pin");
        check = extras.getInt("check");

        pinPresenter = new ConfirmPinPresenter();

        dialog = new Dialog(this);
        
        setUpPin();
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
            confirmPin.setText(inputPin);
        }
    }

    private void delete() {
        if(inputPin.length() > 0) {
            inputPin = inputPin.substring(0, inputPin.length()-1);
            confirmPin.setText(inputPin);
        }
    }

    private void setUpPin() {
        confirmPin.requestFocus();

        confirmPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length()==4) {
                    if (charSequence.toString().equals(pin)) {

                        if (check == 1){
                            // put to firebase
                            pinPresenter.putPinToDataBase(pin);

                            // dashboard
                            Intent intent = new Intent(ConfirmPinActivity.this, DashboardActivity.class);
                            intent.putExtras(extras);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            pinPresenter.updatePin(pin);
                            showDialog();
                        }
                    } else {
                        pinError.setText("Pin is Not Match");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showDialog() {
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_confirm_update_pin);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button backToDB = dialog.findViewById(R.id.cf_pin_back_to_dashboard_btn);

        backToDB.setOnClickListener(view -> {
            dialog.dismiss();
            finish();

        });

        dialog.show();
    }
}