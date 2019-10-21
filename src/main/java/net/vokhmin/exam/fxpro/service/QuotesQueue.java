package net.vokhmin.exam.fxpro.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import net.vokhmin.exam.fxpro.domain.Quote;

public class QuotesQueue {

    private final PriorityBlockingQueue<Quote> quotes = new PriorityBlockingQueue<>();

//    public int size() {
//        return quotes.size();
//    }

//    public boolean isEmpty() {
//        return quotes.isEmpty();
//    }

    public void put(Quote quote) {
        quotes.put(quote);
    }

    public Quote take() throws InterruptedException {
        return quotes.take();
    }

//    public void clear() {
//        quotes.clear();
//    }

}
