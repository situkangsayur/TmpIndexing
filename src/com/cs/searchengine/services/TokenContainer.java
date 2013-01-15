/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.services;

import com.cs.searchengine.entities.Statistic;
import com.cs.searchengine.entities.UrlDocEntity;
import com.cs.searchengine.entities.HashMapGen;
import com.cs.searchengine.entities.TokensEntity;
import com.cs.searchengine.model.DeltaConverter;
import com.cs.searchengine.model.VByteCoverter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author hendriggg b
 */
public class TokenContainer {

    private static HashMapGen dateTokens;
    private static HashMapGen bodyTokens;
    private static HashMapGen toTokens;
    private static HashMapGen fromTokens;
    private static HashMapGen subjectTokens;
    private static String tokens;
    private static DeltaConverter deltaConv;
    private static VByteCoverter vbyteConv;

    /**
     *
     */
    public static void checkAvailHash() {

        if (bodyTokens == null) {
            bodyTokens = new HashMapGen();
        }

        if (toTokens == null) {
            toTokens = new HashMapGen();
        }

        if (fromTokens == null) {
            fromTokens = new HashMapGen();
        }

        if (subjectTokens == null) {
            subjectTokens = new HashMapGen();
        }

        if (dateTokens == null) {
            dateTokens = new HashMapGen();
        }

        if (deltaConv == null) {
            deltaConv = new DeltaConverter();
        }

    }

    /**
     *
     * @return
     */
    public static HashMapGen getBodyTokens() {
        checkAvailHash();
        return bodyTokens;
    }

    /**
     *
     * @param mainTokens
     */
    public static void setBodyTokens(HashMapGen bodyTokens) {
        checkAvailHash();
        TokenContainer.bodyTokens = bodyTokens;
    }

    public static HashMapGen getDateTokens() {
        checkAvailHash();
        return dateTokens;
    }

    public static void setDateTokens(HashMapGen dateTokens) {
        checkAvailHash();
        TokenContainer.dateTokens = dateTokens;
    }

    public static HashMapGen getToTeokens() {
        checkAvailHash();
        return toTokens;
    }

    public static void setToTeokens(HashMapGen toTeokens) {
        checkAvailHash();
        TokenContainer.toTokens = toTeokens;
    }

    public static HashMapGen getFromTokens() {
        checkAvailHash();
        return fromTokens;
    }

    public static void setFromTokens(HashMapGen fromTokens) {
        checkAvailHash();
        TokenContainer.fromTokens = fromTokens;
    }

    public static HashMapGen getSubjectTokens() {
        checkAvailHash();
        return subjectTokens;
    }

    public static void setSubjectTokens(HashMapGen subjectTokens) {
        checkAvailHash();
        TokenContainer.subjectTokens = subjectTokens;
    }

    /**
     * @author hendri Method Performance. This method will perform to marge all
     * of temporary token that have passed by the parameter and marge it to the
     * main tokens.
     * @param tokenHash
     */
    public static void margeTokens(HashMapGen container, HashMapGen tokenHash) {

        Set<String> result = tokenHash.keySet();
        vbyteConv = new VByteCoverter();
//        System.out.println("berfore marge container");
        int i = 0;
        for (String data : result) {
            tokens = data;
            addTokens(container, tokenHash.get(data));
//            System.out.println("pass " + i);
//            i++;

        }
//        System.out.println("after marge in container");
    }

    /**
     * @author hendri This method perform to add a token to the hashmap
     * @param entity
     */
    public static void addTokens(HashMapGen hashObject, TokensEntity entity) {

        TokensEntity data = null;
//        Integer docEnt;
//        Integer docHash;
        ConcurrentHashMap<String, UrlDocEntity> tempHash;
        ConcurrentHashMap<String, UrlDocEntity> tempToken;

//        Double weight;
//        int tF = 0;
//        int dF = 0;
//        int nMess = Statistic.getFiles();
//        double tempR;
//        double tempL;

        Set<String> key;

        int urlCount = 0;

        if (!hashObject.containsKey(tokens)) {
            data = new TokensEntity();
            tempToken = entity.getUrl();
            data.setCount(entity.getCount());
//            data.setWeight(entity.getWeight());
            data.setUrl(entity.getUrl());
            data.setDocFreq(tempToken.size());
            data.setTokens(entity.getTokens());
            deltaConv.setListDoc(tempToken);
            data.setInverted(deltaConv.generateDelta(tempToken));
//            System.out.println("inv idx : " + data.getInverted());

            // key = entity.getUrl().keySet();
        } else {

            data = hashObject.get(tokens);
            data.setCount(data.getCount() + entity.getCount());
//            tempHash = deltaConv.inverseDelta(data.getInverted());
            String invStr = data.getInverted();
            tempToken = entity.getUrl();
            key = tempToken.keySet();
            if (tempToken.size() != 0) {
                data.setDocFreq(data.getDocFreq() + tempToken.size());

//                for (ConcurrentHashMap.Entry<String, UrlDocEntity> urlData : tempToken.entrySet()) {
////                    tempHash.put(urlData.getKey(), urlData.getValue());
//                    
//                }
                invStr += "," + deltaConv.generateDelta(tempToken);

                data.setInverted(invStr);
            }
//            data.setUrl(tempHash);
        }
//        System.out.println("marge in container");
//        dF = data.getDocFreq();
//        tF = data.getCount();
//        tempL = (double) tF / (double) nMess;
//        tempR = Math.log((double) nMess / (double) dF);
//        weight = tempL / tempR;

//        data.setWeight(weight);

        data.setUrl(null);

        if (!hashObject.containsKey(tokens)) {
            hashObject.put(tokens, data);
        } else {
            hashObject.replace(tokens, data);
        }

    }

    /**
     * @author ken : to sorting the main hashmap and change to be Map that have
     * been sorted by weight
     * @param unsortMap
     * @return
     */
    public static Map<String, TokensEntity> sortByComparator(HashMapGen unsortMap) {

        // Set<String> key = unsortMap.keySet();

        List<TokensEntity> list = new LinkedList<TokensEntity>(unsortMap.values());
//        System.out.println(" size hash before to be list :" + unsortMap.size());
//        System.out.println(" size before sort : " + list.size());

        return selectionSort(list);
    }

    /**
     * @author ken : to sort the List of values from the hashmap or main hashmap
     * and it will be sorting by Collection class and using sort method from
     * collection interface
     * @param unsortList
     * @return
     */
    public static Map<String, TokensEntity> selectionSort(List<TokensEntity> unsortList) {
        List<TokensEntity> sortList = unsortList;

        //Collections.sort(unsortList, );
        Collections.sort(sortList, new Comparator<TokensEntity>() {
            public int compare(TokensEntity o1, TokensEntity o2) {
                TokensEntity val1 = o1;
                TokensEntity val2 = o2;
                return val1.getTokens().compareTo(val2.getTokens());
            }
        });
        Map<String, TokensEntity> sortedMap = new LinkedHashMap();

        for (int i = 0; i < sortList.size(); i++) {
            sortedMap.put(sortList.get(i).getTokens(), sortList.get(i));
        }
//        System.out.println(" size sorted list :" + sortList.size());
        return sortedMap;
    }

    /**
     * @author Hendri: to write the results of sorted hashmap to the file, and
     * manage the string of results for each values in hashmap.
     * @param field
     * @param sortedMap
     */
    public static void printToEchDoc(String field, Integer indexFile, LinkedHashMap<String, TokensEntity> sortedMap) {

//        System.out.println("A");

        String result = "";
        String tempIdxB = "A";
        String tempIdxA = "ZZZ";
        Set<String> key = sortedMap.keySet();
        System.out.println("banyak key - " + key.size());
        int i = 0;
        List<String> texts = new ArrayList<String>();

        for (String token : key) {
            if (sortedMap.get(token).getTokens().equals("")) {
                System.out.println(" token kosong is " + token + " : " + sortedMap.get(token).getTokens());
            }

            if (sortedMap.get(token).getTokens().equals(" ")) {
                System.out.println(" token kosong with space is " + token + " : " + sortedMap.get(token).getTokens());
            }

            tempIdxB = sortedMap.get(token).getTokens().substring(0, 1);

            if (i == 0) {
                tempIdxA = tempIdxB;
                i++;
            }

//            System.out.println("...." + field);


            if ((!tempIdxB.equals(tempIdxA)) && (i > 0)) {
                printToFile("fileindex/" + field + "/" + field + "-" + tempIdxA + "-" + indexFile + ".txt", texts);
                result = "";
                texts.clear();
//                texts = new ArrayList<String>();
                texts.add("999999\r\n");
                tempIdxA = tempIdxB;
            }


            result = sortedMap.get(token).getTokens() + " " + sortedMap.get(token).getInverted()
                    + "\r\n";
            texts.add(result);

        }

    }

    /**
     * @author adhe : main method to write the string to the file.txt
     * @param fileName
     * @param text
     */
    public static void printToFile(String fileName, List<String> list) {
        RandomAccessFile file;
        try {
            file = new RandomAccessFile(fileName, "rw");
            for (String teks : list) {
                file.write(teks.getBytes());
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            file = null;
        }
    }
}
