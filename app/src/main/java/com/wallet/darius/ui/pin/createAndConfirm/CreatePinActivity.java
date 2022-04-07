package com.wallet.darius.ui.pin.createAndConfirm;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.wallet.darius.R;

public class CreatePinActivity extends AppCompatActivity {
    private PinView createPin;
    private Button nextBtn;
    private TextView pinTopic, oneBtn, twoBtn, threeBtn, fourBtn,
            fiveBtn, sixBtn, sevenBtn, eightBtn, nineBtn, zeroBtn, delBtn;

    private Bundle bundle;
    private String inputPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        createPin = findViewById(R.id.create_pin);
        nextBtn = findViewById(R.id.create_pin_next_btn);
        pinTopic = findViewById(R.id.pin_topic);

        nextBtn.setEnabled(false);

        bundle = getIntent().getExtras();

        if (bundle.getInt("check") == 1) {
            pinTopic.setText("Create Pin");
        } else {
            pinTopic.setText("Update Pin");
        }

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
            createPin.setText(inputPin);
        }
    }

    private void delete() {
        if(inputPin.length() > 0) {
            inputPin = inputPin.substring(0, inputPin.length()-1);
            createPin.setText(inputPin);
        }
    }

    private void setUpPin() {
        createPin.requestFocus();

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
                        if (bundle.getInt("check") == 2) {
                            finish();
                        }
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