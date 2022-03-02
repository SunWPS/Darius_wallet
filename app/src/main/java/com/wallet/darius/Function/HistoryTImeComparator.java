package com.wallet.darius.Function;

import com.wallet.darius.model.itemCard.HistoryCard;

import java.sql.Timestamp;
import java.util.Comparator;

public class HistoryTImeComparator implements Comparator<HistoryCard> {
    @Override
    public int compare(HistoryCard t0, HistoryCard t1) {
        Timestamp ts0 = new Timestamp(t0.getTimeStamp());
        Timestamp ts1 = new Timestamp(t1.getTimeStamp());

        return ts1.compareTo(ts0);
    }
}
