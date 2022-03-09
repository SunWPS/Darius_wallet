package com.wallet.darius.ui.pin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.wallet.darius.R;
import com.wallet.darius.ui.dashboard.DashboardActivity;

public class ConfirmPinActivity extends AppCompatActivity {

    private PinView confirmPin;
    private TextView pinError;

    private String pin;
    private Bundle extras;
    private ConfirmPinPresenter pinPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin);

        confirmPin = findViewById(R.id.confirm_pin);
        pinError = findViewById(R.id.pin_error);

        extras = getIntent().getExtras();
        pin = extras.getString("pin");
        pinPresenter = new ConfirmPinPresenter();


        confirmPin.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        confirmPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length()==4) {
                    if (charSequence.toString().equals(pin)) {
                        // put to firebase
                        pinPresenter.putPinToDataBase(pin);

                        // dashboard
                        Intent intent = new Intent(ConfirmPinActivity.this, DashboardActivity.class);
                        intent.putExtras(extras);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
}