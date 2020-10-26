package de.atb.typhondl.xtext.ui.utilities;

public class Tripple<T, U, V> {

    public T first;
    public U second;
    public V third;

    public Tripple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
