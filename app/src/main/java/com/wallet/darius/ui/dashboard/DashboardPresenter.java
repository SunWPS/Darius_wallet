package com.wallet.darius.ui.dashboard;

import com.google.firebase.auth.FirebaseAuth;
import com.wallet.darius.API.CoinInfoAPi;
import com.wallet.darius.model.coinModel.Root;
import com.wallet.darius.model.coinModel.USD;

public class DashboardPresenter {

    DashboardView dashboardView;
    FirebaseAuth auth;
    CoinInfoAPi coinInfoAPi;

    public DashboardPresenter(DashboardView dashboardView) {
        this.dashboardView = dashboardView;
        this.auth = FirebaseAuth.getInstance();
        coinInfoAPi = new CoinInfoAPi(this);
    }

    public String getUserEmail() {
        return auth.getCurrentUser().getEmail();
    }

    public void userSignOut() {
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
