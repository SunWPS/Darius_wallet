package com.wallet.darius.ui.history;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wallet.darius.Adapter.HistoryAdapter;
import com.wallet.darius.Function.HistoryTImeComparator;
import com.wallet.darius.R;
import com.wallet.darius.model.itemCard.HistoryCard;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private Dialog dialog;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private Gson gson;
    private String myAddress, network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.historyRecycleViewCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog = new Dialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("history");
        gson = new Gson();

        Bundle extras = getIntent().getExtras();
        myAddress = extras.getString("myAddress");
        network = extras.getString("network");

        showHistoryCard();
    }

    private void showHistoryCard() {
        myRef.child(user.getUid() + "/" + network).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HistoryCard> historyCards = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    historyCards.add(gson.fromJson(snapshot.getValue().toString(),
                            HistoryCard.class));
                }

                Collections.sort(historyCards, new HistoryTImeComparator());
                HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(), historyCards, myAddress, dialog);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}