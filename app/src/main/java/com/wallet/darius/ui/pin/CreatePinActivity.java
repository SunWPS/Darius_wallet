package com.wallet.darius.ui.pin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.chaos.view.PinView;
import com.wallet.darius.R;

public class CreatePinActivity extends AppCompatActivity {
    private PinView createPin;
    private Button nextBtn;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        createPin = findViewById(R.id.create_pin);
        nextBtn = findViewById(R.id.create_pin_next_btn);

        nextBtn.setEnabled(false);

        bundle = getIntent().getExtras();

        setUpPin();

    }

    private void setUpPin() {
        createPin.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        createPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length()==4) {
                    nextBtn.setOnClickListener(view -> {
                        Intent intent = new Intent(CreatePinActivity.this, ConfirmPinActivity.class);
                        bundle.putString("pin", charSequence.toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}