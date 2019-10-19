package net.vokhmin.exam.fxpro.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import net.vokhmin.exam.fxpro.domain.Quote;

public class TbarQueue implements Queue<Quote> {


    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Quote> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Quote quote) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Quote> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(Quote quote) {
        return false;
    }

    @Override
    public Quote remove() {
        return null;
    }

    @Override
    public Quote poll() {
        return null;
    }

    @Override
    public Quote element() {
        return null;
    }

    @Override
    public Quote peek() {
        return null;
    }
}
