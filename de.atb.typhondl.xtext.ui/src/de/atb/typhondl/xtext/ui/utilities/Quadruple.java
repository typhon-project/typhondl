package de.atb.typhondl.xtext.ui.utilities;

public class Quadruple<T, U, V, W> {

    public T first;
    public U second;
    public V third;
    public W fourth;

    public Quadruple(T first, U second, V third, W fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
}
