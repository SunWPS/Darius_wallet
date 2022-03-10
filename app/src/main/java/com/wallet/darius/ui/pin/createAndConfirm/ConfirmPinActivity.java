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
    private TextView pinError;

    private String pin;
    private Bundle extras;
    private ConfirmPinPresenter pinPresenter;
    private int check;
    private Dialog dialog;
    private InputMethodManager inputMethodManager;

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



        confirmPin.requestFocus();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

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
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } else {
                            pinPresenter.updatePin(pin);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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