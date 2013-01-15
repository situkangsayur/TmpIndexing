/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.services;

import java.util.HashMap;

/**
 *
 * @author hendri
 */
public class TupleContainer {

    private static HashMap<String, Integer> tuples;

    /**
     * Constructor method
     */
    public static void checkAvailTuples() {

        if (tuples == null) {
            tuples = new HashMap<String, Integer>();
            tuples.put("To:", 1);
            tuples.put("From:", 2);
            tuples.put("Date:", 3);
            tuples.put("Subject:", 4);
            tuples.put("Content-", 5);

        }
    }

    /**
     *
     * @return
     */
    public static HashMap<String, Integer> getTuples() {
        checkAvailTuples();
        return tuples;
    }

    /**
     *
     * @param keyWord
     * @return
     */
    public static Integer getTuplesIndex(String keyWord) {
        checkAvailTuples();
        return TupleContainer.tuples.get(keyWord);
    }

    /**
     *
     * @param index
     * @param value
     */
    public static void setTuplesIndex(String index, Integer value) {
        checkAvailTuples();
        TupleContainer.tuples.put(index, value);
    }
}
