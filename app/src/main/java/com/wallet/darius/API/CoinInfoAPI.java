package com.wallet.darius.API;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.google.gson.Gson;

import com.wallet.darius.model.coinModel.Root;
import com.wallet.darius.ui.dashboard.DashboardPresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoinInfoAPI {

    private DashboardPresenter dashboardPresenter;
    private static String apiKey;
    private static String uri;

    public CoinInfoAPI(DashboardPresenter dashboardPresenter, String apiKey, String uri) {
        this.dashboardPresenter = dashboardPresenter;
        this.apiKey = apiKey;
        this.uri = uri;
    }

    public void apiInteract() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(uri).newBuilder();

        httpBuilder.addQueryParameter("start","2");
        httpBuilder.addQueryParameter("limit", "1");
        httpBuilder.addQueryParameter("convert", "USD");

        Request request = new Request.Builder()
                .url(String.valueOf(httpBuilder))
                .header("Accept", "application/json")
                .addHeader("X-CMC_PRO_API_KEY", apiKey)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("CoinInfo", "Error: cannot access content - " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                Gson gson = new Gson();
                Root coin = gson.fromJson(responseBody, Root.class);


                dashboardPresenter.extractEthInfo(coin.getData().get(0).getQuote().getUSD());
            }
        });

    }


}
