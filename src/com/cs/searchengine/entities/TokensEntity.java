/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.entities;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author hendri Entity class to save all of information about count of every
 * tuple that find in a file
 */
public class TokensEntity {

    private int count;
//    private Double weight;
    private int docFreq;
    private String tokens;
    private String inverted;
    private ConcurrentHashMap<String, UrlDocEntity> url;
    //private int docFreq;

    // private String tokens;
    public void checkUrl() {
        if (url == null) {
            url = new ConcurrentHashMap< String, UrlDocEntity>();
        }
    }

    public String getInverted() {
        return inverted;
    }

    public void setInverted(String inverted) {
        this.inverted = inverted;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public int getDocFreq() {
        return docFreq;
    }

    public void setDocFreq(int docFreq) {
        this.docFreq = docFreq;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
//
//    public Double getWeight() {
//        return weight;
//    }
//
//    public void setWeight(Double weight) {
//        this.weight = weight;
//    }

    public ConcurrentHashMap<String, UrlDocEntity> getUrl() {
        checkUrl();
        return url;
    }

    public void setUrl(ConcurrentHashMap<String, UrlDocEntity> url) {
        checkUrl();
        this.url = url;
    }
}
