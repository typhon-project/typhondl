package de.atb.typhondl.xtext.ui.utilities;

/**
 * Utility class for reading the ML model
 * 
 * @author flug
 *
 * @param <T> probably the dbName
 * @param <U> probably the abstractType
 */
public class Pair<T, U> {

    /**
     * probably the dbName
     */
    public T firstValue;
    /**
     * probably the abstractType
     */
    public U secondValue;

    /**
     * @param firstValue  probably the dbName
     * @param secondValue probably the abstractType
     */
    public Pair(T firstValue, U secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

}
