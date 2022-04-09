package io.crm.core.internal.util.impl;

import io.crm.core.internal.util.Stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Created by xiongxl in 2022/4/4
 */
public class DequeStack<T> implements Stack<T> {
    private final Deque<T> deque;

    public DequeStack() {
        this(new ArrayDeque<>());
    }

    public DequeStack(Deque<T> deque) {
        Objects.requireNonNull(deque);
        this.deque = deque;
    }

    @Override
    public void push(T element) {
        deque.addFirst(element);
    }

    @Override
    public T pop() {
        return deque.removeFirst();
    }

    @Override
    public T pull() {
        return deque.pollFirst();
    }
}
