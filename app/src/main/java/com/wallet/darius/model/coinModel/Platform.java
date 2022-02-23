package com.wallet.darius.model.coinModel;

public class Platform {
    public String id;
    public String name;
    public String symbol;
    public String slug;
    public String token_address;

    public Platform(String id, String name, String symbol, String slug, String token_address) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.slug = slug;
        this.token_address = token_address;
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

    public String getToken_address() {
        return token_address;
    }

    public void setToken_address(String token_address) {
        this.token_address = token_address;
    }
}
