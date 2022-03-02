package com.wallet.darius.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wallet.darius.R;
import com.wallet.darius.model.itemCard.HistoryCard;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private Context context;
    private ArrayList<HistoryCard> historyList;
    private String myAddress;
    private Dialog dialog;

    public HistoryAdapter(Context context, ArrayList<HistoryCard> historyList, String myAddress, Dialog dialog) {
        this.context = context;
        this.historyList = historyList;
        this.myAddress = myAddress;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_history, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        HistoryCard historyCard = historyList.get(position);
        holder.setDetails(historyCard);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    class HistoryHolder extends RecyclerView.ViewHolder {
        private TextView hcTimestamp, hcReceiver, hcAmount;
        private HistoryCard historyCard;

        HistoryHolder(View view) {
            super(view);
            hcTimestamp = view.findViewById(R.id.hcTimestamp);
            hcReceiver = view.findViewById(R.id.hcReceiver);
            hcAmount = view.findViewById(R.id.hcAmouth);

            view.setOnClickListener(view1 -> {
                dialog.setContentView(R.layout.dialog_transaction_info);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView infoTimeStamp = dialog.findViewById(R.id.infoTimestamp);
                TextView infoTransactionHash = dialog.findViewById(R.id.infoTransactionHash);
                TextView infoMyAddress = dialog.findViewById(R.id.infoMyAddress);
                TextView infoReceiverAddress = dialog.findViewById(R.id.infoReceiverAddress);
                TextView infoAmount = dialog.findViewById(R.id.infoAmount);
                TextView infoFee = dialog.findViewById(R.id.infoFee);
                Button closeBtn = dialog.findViewById(R.id.infoCloseBtn);

                infoTimeStamp.setText(historyCard.fullDateTime());
                infoTransactionHash.setText(historyCard.getTransactionHash());
                infoMyAddress.setText(myAddress);
                infoReceiverAddress.setText(historyCard.getReceiverAddress());
                infoAmount.setText(historyCard.getAmount() + " ETH");
                infoFee.setText(historyCard.getFee() + " ETH");

                closeBtn.setOnClickListener(view2 -> {
                    dialog.dismiss();
                });

                dialog.show();
            });
        }

        void setDetails(HistoryCard historyCard) {
            this.historyCard = historyCard;
            hcTimestamp.setText(historyCard.shortDateTime());
            hcReceiver.setText(historyCard.shortReceiverAddress());
            hcAmount.setText(historyCard.getAmount() + " ETH");
        }

    }
}
