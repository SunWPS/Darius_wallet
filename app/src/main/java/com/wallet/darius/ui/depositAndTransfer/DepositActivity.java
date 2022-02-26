package com.wallet.darius.ui.depositAndTransfer;

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

        totalBalance = findViewById(R.id.dp_available_balance);
        currentNetwork = findViewById(R.id.dp_transfer_current_network);
        address = findViewById(R.id.dp_wallet_address);
        clipboardBtn = findViewById(R.id.dp_clipboard_btn);

        Bundle bundle = getIntent().getExtras();

        totalBalance.setText(bundle.getString("balance"));
        address.setText(bundle.getString("address"));

        String network = bundle.getString("network");
        currentNetwork.setText(network.substring(0, 1).toUpperCase() + network.substring(1));

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