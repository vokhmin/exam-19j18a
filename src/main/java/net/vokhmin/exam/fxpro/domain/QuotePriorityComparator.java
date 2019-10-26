package net.vokhmin.exam.fxpro.domain;

import java.util.Comparator;

public class QuotePriorityComparator implements Comparator<Quote> {

    @Override
    public int compare(Quote q1, Quote q2) {
        return Long.compare(q1.timestamp, q2.timestamp);
    }
}
