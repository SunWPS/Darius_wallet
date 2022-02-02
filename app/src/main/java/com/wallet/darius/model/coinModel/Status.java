package com.wallet.darius.model.coinModel;

public class Status{
    public String timestamp;
    public String error_code;
    public String error_message;
    public String elapsed;
    public String credit_count;
    public String notice;
    public String total_count;

    public Status(String timestamp, String error_code, String error_message, String elapsed, String credit_count, String notice, String total_count) {
        this.timestamp = timestamp;
        this.error_code = error_code;
        this.error_message = error_message;
        this.elapsed = elapsed;
        this.credit_count = credit_count;
        this.notice = notice;
        this.total_count = total_count;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getElapsed() {
        return elapsed;
    }

    public void setElapsed(String elapsed) {
        this.elapsed = elapsed;
    }

    public String getCredit_count() {
        return credit_count;
    }

    public void setCredit_count(String credit_count) {
        this.credit_count = credit_count;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }
}