package net.lastcraft.luckywars.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<T> implements Iterable<T> {

    private T[] items;
    private int size;

    public RandomizedQueue() {
        items = (T[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int N) {
        T[] temp = (T[]) new Object[N];
        for (int i = 0; i < size; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    public void enqueue(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        items[size++] = item;
        if (size == items.length) {
            resize(2 * size);
        }
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int i = (int) (Math.random() * size);
        T item = items[i];
        items[i] = items[--size];
        items[size] = null;
        if (size <= items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    public T sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int i = (int) (Math.random() * size);
        return items[i];
    }

    public Iterator<T> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<T> {
        private int i = 0;
        private T[] temp;

        public RandomizedQueueIterator() {
            temp = (T[]) new Object[size];

            for (int j = 0; j < size; j++) {
                temp[j] = items[j];
            }
        }

        public boolean hasNext() {
            return i < size;
        }

        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int k = (int) (Math.random() * (size - i));
            T item = temp[k];
            temp[k] = temp[size - (++i)];
            temp[size - i] = null;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}