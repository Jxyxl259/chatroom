package com.jiang.chatroom.common;

import com.google.common.base.Objects;

/**
 * @ClassName Pair
 * @Description
 * @Author jiangxy
 * @Date 2018\11\23 0023 21:04
 * @Version 1.0.0
 */
public class Pair<K,V> {

    private K k;

    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equal(k, pair.k) &&
                Objects.equal(v, pair.v);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(k, v);
    }
}
