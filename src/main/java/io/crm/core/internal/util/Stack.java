package io.crm.core.internal.util;

/**
 * Created by xiongxl in 2022/4/4
 */
public interface Stack<T> {
    void push(T element);
    T pop();
    T pull();
}
