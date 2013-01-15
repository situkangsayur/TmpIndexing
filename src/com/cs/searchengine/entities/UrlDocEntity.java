/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.entities;

/**
 *
 * @author hendri
 */
public class UrlDocEntity {

    private Integer termFreq;
    private String termPosition;
    private String index;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Integer getTermFreq() {
        return termFreq;
    }

    public void setTermFreq(Integer termFreq) {
        this.termFreq = termFreq;
    }

    public String getTermPosition() {
        return termPosition;
    }

    public void setTermPosition(String termPosition) {
        this.termPosition = termPosition;
    }
}
