package com.wallet.darius.model.itemCard;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class HistoryCard {
    private String receiverAddress;
    private String amount;
    private String fee;
    private String transactionHash;
    private long timeStamp;

    public HistoryCard(String receiverAddress, String amount, String fee, String transactionHash, long timeStamp) {
        this.receiverAddress = receiverAddress;
        this.amount = amount;
        this.fee = fee;
        this.transactionHash = transactionHash;
        this.timeStamp = timeStamp;
    }

    public String shortReceiverAddress() {
        return receiverAddress.substring(0,5) + "..." + receiverAddress.substring(37);
    }

    public String shortDateTime() {
        Timestamp ts = new Timestamp(timeStamp);
        SimpleDateFormat dFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return dFormat.format(ts);
    }

    public String fullDateTime() {
        Timestamp ts = new Timestamp(timeStamp);
        SimpleDateFormat dFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        return dFormat.format(ts);
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

