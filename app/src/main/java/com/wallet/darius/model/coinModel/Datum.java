package com.wallet.darius.model.coinModel;

import java.util.ArrayList;

public class Datum{
    public String id;
    public String name;
    public String symbol;
    public String slug;
    public String num_market_pairs;
    public String date_added;
    public ArrayList<String> tags;
    public String max_supply;
    public String circulating_supply;
    public String total_supply;
    public String platform;
    public String cmc_rank;
    public String self_reported_circulating_supply;
    public String self_reported_market_cap;
    public String last_updated;
    public Quote quote;

    public Datum(String id, String name, String symbol, String slug, String num_market_pairs, String date_added, ArrayList<String> tags, String max_supply, String circulating_supply, String total_supply, String platform, String cmc_rank, String self_reported_circulating_supply, String self_reported_market_cap, String last_updated, Quote quote) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.slug = slug;
        this.num_market_pairs = num_market_pairs;
        this.date_added = date_added;
        this.tags = tags;
        this.max_supply = max_supply;
        this.circulating_supply = circulating_supply;
        this.total_supply = total_supply;
        this.platform = platform;
        this.cmc_rank = cmc_rank;
        this.self_reported_circulating_supply = self_reported_circulating_supply;
        this.self_reported_market_cap = self_reported_market_cap;
        this.last_updated = last_updated;
        this.quote = quote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNum_market_pairs() {
        return num_market_pairs;
    }

    public void setNum_market_pairs(String num_market_pairs) {
        this.num_market_pairs = num_market_pairs;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(String max_supply) {
        this.max_supply = max_supply;
    }

    public String getCirculating_supply() {
        return circulating_supply;
    }

    public void setCirculating_supply(String circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public String getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(String total_supply) {
        this.total_supply = total_supply;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCmc_rank() {
        return cmc_rank;
    }

    public void setCmc_rank(String cmc_rank) {
        this.cmc_rank = cmc_rank;
    }

    public String getSelf_reported_circulating_supply() {
        return self_reported_circulating_supply;
    }

    public void setSelf_reported_circulating_supply(String self_reported_circulating_supply) {
        this.self_reported_circulating_supply = self_reported_circulating_supply;
    }

    public String getSelf_reported_market_cap() {
        return self_reported_market_cap;
    }

    public void setSelf_reported_market_cap(String self_reported_market_cap) {
        this.self_reported_market_cap = self_reported_market_cap;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }
}