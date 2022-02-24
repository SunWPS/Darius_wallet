package com.wallet.darius.ui.deposit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.darius.R;

public class DepositActivity extends AppCompatActivity {

    private TextView totalBalance, currentNetwork, address;
    private ImageButton clipboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        totalBalance = findViewById(R.id.availableBalance);
        currentNetwork = findViewById(R.id.transferCurrentNetwork);
        address = findViewById(R.id.walletAddress);
        clipboardBtn = findViewById(R.id.clipboardBtn);

        Bundle bundle = getIntent().getExtras();

        totalBalance.setText(bundle.getString("balance"));
        address.setText(bundle.getString("address"));

        setUpClipboard();

    }

    private void setUpClipboard() {
        clipboardBtn.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("Address", address.getText());
            clipboard.setPrimaryClip(data);

            Toast.makeText(this, "Copied Address!!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}