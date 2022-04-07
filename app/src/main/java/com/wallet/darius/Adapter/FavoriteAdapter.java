package com.wallet.darius.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.R;
import com.wallet.darius.model.itemCard.FavoriteCard;
import com.wallet.darius.ui.depositAndTransfer.TransferActivity;
import com.wallet.darius.ui.verifyEmail.VerifyEmailBfTxnActivity;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {

    private Context context;
    private ArrayList<FavoriteCard> favoriteList;
    private WalletAPI wallet;
    private String network;
    private String networkLink;

    public FavoriteAdapter(Context context, ArrayList<FavoriteCard> favoriteList, WalletAPI wallet, String network, String networkLink) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.wallet = wallet;
        this.network = network;
        this.networkLink = networkLink;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_favorite, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        FavoriteCard favorite = favoriteList.get(position);
        holder.setDetails(favorite);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }


    class FavoriteHolder extends RecyclerView.ViewHolder {
        private TextView fvName, fvAddress;

        FavoriteHolder(View view) {
            super(view);
            fvName = view.findViewById(R.id.fv_name);
            fvAddress = view.findViewById(R.id.fv_address);

            view.setOnClickListener(view1 -> {
                Intent intent;
                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                    intent = new Intent(context.getApplicationContext(), TransferActivity.class);
                } else {
                    intent = new Intent(context.getApplicationContext(), VerifyEmailBfTxnActivity.class);
                    intent.putExtra("nextPage", "tf");
                }
                intent.putExtra("wallet", wallet);
                intent.putExtra("network", network);
                intent.putExtra("networkLink", networkLink);
                intent.putExtra("address", fvAddress.getText());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }

        void setDetails(FavoriteCard favoriteCard) {
            fvName.setText(favoriteCard.getName());
            fvAddress.setText(favoriteCard.getAddress());
        }
    }
}
