/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hendri
 */
public class StringManipulator {

    private static StringBuffer sentences;
    private static List<Integer> listOfSpace;

    /**
     *
     * @return
     */
    public static StringBuffer getSentences() {
        return sentences;
    }

    /**
     *
     * @param sentences
     */
    public static void setSentences(StringBuffer sentences) {
        StringManipulator.sentences = sentences;
    }

    /**
     *
     */
    protected static void checkAvailListOfSpace() {
        if (listOfSpace == null) {
            listOfSpace = new ArrayList<Integer>();
        }
    }

    /**
     *
     */
    public static void deleteAll() {
        StringManipulator.sentences.delete(0, StringManipulator.sentences.length());
        listOfSpace.clear();
    }

    /**
     *
     * @return
     */
    public static List<Integer> getListOfSpace() {
        for (int i = 0; i < StringManipulator.sentences.length(); i++) {
            if (StringManipulator.sentences.charAt(i) == ' ') {
                listOfSpace.add(i);
            }
        }
        return StringManipulator.listOfSpace;
    }
}
