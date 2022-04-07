package com.wallet.darius.ui.favorite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wallet.darius.API.WalletAPI;
import com.wallet.darius.Adapter.FavoriteAdapter;
import com.wallet.darius.Function.FavoriteNameComparator;
import com.wallet.darius.R;
import com.wallet.darius.model.itemCard.FavoriteCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class FavoriteActivity extends AppCompatActivity {

    private Button addBtn;
    private RecyclerView recyclerView;

    private Dialog dialog;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private Gson gson;

    private WalletAPI wallet;
    private String network, networkLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        addBtn = findViewById(R.id.addFavoriteBtn);
        recyclerView = findViewById(R.id.favRecycleViewCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog = new Dialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("favorite");
        gson = new Gson();
        Bundle extras = getIntent().getExtras();
        wallet = extras.getParcelable("wallet");
        network = extras.getString("network");
        networkLink = extras.getString("networkLink");

        showFavoriteCard();

        addBtn.setOnClickListener(view -> {
            addFavorite();
        });
    }

    private void addFavorite() {
        dialog.setContentView(R.layout.dialog_add_favorite);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancelBtn = dialog.findViewById(R.id.addCancelBtn);
        Button submitBtn = dialog.findViewById(R.id.addSubmitBtn);
        TextView errorName = dialog.findViewById(R.id.errorFavName);
        TextView errorAddress = dialog.findViewById(R.id.errorFavAddress);
        EditText inputName = dialog.findViewById(R.id.favInputName);
        EditText inputAddress = dialog.findViewById(R.id.favInputAddress);

        cancelBtn.setOnClickListener(view -> {
            dialog.dismiss();
        });

        submitBtn.setOnClickListener(view -> {
            errorName.setText("");
            errorAddress.setText("");

            String name = inputName.getText().toString();
            String address = inputAddress.getText().toString();

            if (name.isEmpty()) {
                errorName.setText("Name is required");
                return;
            }
            if(address.isEmpty()) {
                errorAddress.setText("Address is required");
                return;
            }

            putDataToDB(name, address);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void putDataToDB(String name, String address) {
        FavoriteCard favoriteCard = new FavoriteCard(name, address);
        myRef.child(user.getUid() + "/" + UUID.randomUUID().toString().replace("-", ""))
                .setValue(favoriteCard);
        Toast.makeText(this, "add successful", Toast.LENGTH_SHORT).show();
    }

    private void showFavoriteCard() {
        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<FavoriteCard> favoriteCards = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    favoriteCards.add(gson.fromJson(snapshot.getValue().toString(),
                            FavoriteCard.class));
                }

                Collections.sort(favoriteCards, new FavoriteNameComparator());
                FavoriteAdapter adapter = new FavoriteAdapter(getApplicationContext(), favoriteCards, wallet, network, networkLink);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}