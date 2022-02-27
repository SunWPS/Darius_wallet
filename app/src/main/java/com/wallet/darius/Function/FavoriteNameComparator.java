package com.wallet.darius.Function;

import com.wallet.darius.model.favorite.FavoriteCard;

import java.util.Comparator;

public class FavoriteNameComparator implements Comparator<FavoriteCard> {
    @Override
    public int compare(FavoriteCard t0, FavoriteCard t1) {
        return t0.getName().compareTo(t1.getName());
    }
}
