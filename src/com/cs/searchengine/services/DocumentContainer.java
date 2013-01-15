/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.services;

import com.cs.searchengine.entities.DocumentEntity;
import com.cs.searchengine.entities.Statistic;
import com.cs.searchengine.entities.TokensEntity;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hendri
 */
public class DocumentContainer {

    private static ConcurrentHashMap<String, DocumentEntity> documents;
    private static RandomAccessFile fstream;
    private static BufferedWriter out;

    public static void checkDocuments() {
        if (documents == null) {
            documents = new ConcurrentHashMap<String, DocumentEntity>();
        }
    }

    public static ConcurrentHashMap<String, DocumentEntity> getDocuments() {
        checkDocuments();

        return documents;
    }

    public static void setDocuments(ConcurrentHashMap<String, DocumentEntity> documents) {
        checkDocuments();

        DocumentContainer.documents = documents;
    }

    /**
     * @author Hendri : to write the results of sorted hashmap to the file, and
     * manage the string of results for each values in hashmap.
     * @param field
     * @param sortedMap
     */
    public static void addUrlToFile(String index, String url) {

        String urlTemp = url.trim();
        DecimalFormat df = new DecimalFormat("####.#####");

        String result = "";

        int i = 0;

        result += index + "#" + url + "#\r\n";
        printToFile(Statistic.TEAM + "-index_url"
                + ".txt", result);
    }

    /**
     * @author Hendri : main method to write the string to the file.txt
     * @param fileName
     * @param text
     */
    public static void printToFile(String fileName, String text) {
        try {
            if (fstream == null) {
                fstream = new RandomAccessFile(fileName, "rw");
            }
            fstream.writeUTF(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeProntTolFile() {
        try {
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
