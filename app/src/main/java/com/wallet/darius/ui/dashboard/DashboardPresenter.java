package com.wallet.darius.ui.dashboard;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.API.CoinInfoAPI;
import com.wallet.darius.model.coinModel.USD;

import java.io.File;

public class DashboardPresenter {

    private DashboardView dashboardView;
    private FirebaseAuth auth;
    private CoinInfoAPI coinInfoAPi;

    public DashboardPresenter(DashboardView dashboardView, ApplicationInfo ai) {
        this.dashboardView = dashboardView;
        this.auth = FirebaseAuth.getInstance();

        Bundle bundle = ai.metaData;
        coinInfoAPi = new CoinInfoAPI(this,
                bundle.getString("cmcAPIkey"),
                bundle.getString("cmcURI"));
    }

    public String getUserEmail() {
        return auth.getCurrentUser().getEmail();
    }

    public void userSignOut(String credentialPath) {
        File file  = new File(credentialPath);
        file.delete();
        auth.signOut();
    }

    public void getEthTracking() {
        coinInfoAPi.apiInteract();
    }

    public void extractEthInfo(USD priceInfo) {
        Double price = priceInfo.getPrice();
        Double changeIn1H = priceInfo.getPercent_change_1h();
        Double changeIn7D = priceInfo.getPercent_change_7d();

        Double changeIn24H = priceInfo.getPercent_change_24h();
        dashboardView.setTextToTracking(price, changeIn1H, changeIn24H, changeIn7D);
    }
}
