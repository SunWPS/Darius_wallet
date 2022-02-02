package com.wallet.darius.model.coinModel;

import java.util.ArrayList;

public class Root {
    public Status status;
    public ArrayList<Datum> data;

    public Root(Status status, ArrayList<Datum> data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

}
