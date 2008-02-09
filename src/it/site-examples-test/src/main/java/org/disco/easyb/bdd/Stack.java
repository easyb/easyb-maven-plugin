package org.disco.easyb.bdd;

import java.util.ArrayList;

public class Stack<E> {

    private ArrayList<E> list;

    public Stack() {
        this.list = new ArrayList<E>();
    }

    public void push(E value) {
        if (value == null) {
            throw new RuntimeException("Can't push null");
        } else {
            this.list.add(value);
        }
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public E pop() {
        if (this.list.size() > 0) {
            return this.list.remove(this.list.size() - 1);
        } else {
            throw new RuntimeException("Nothing to pop");
        }
    }

    public E peek() {
        if (this.list.size() > 0) {
            return this.list.get(this.list.size() - 1);
        } else {
            return null;
        }
    }
}
