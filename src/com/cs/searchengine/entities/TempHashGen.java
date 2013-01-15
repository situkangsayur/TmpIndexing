/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.entities;

/**
 *
 * @author hendri
 */
public class TempHashGen {

    private HashMapGen dateTokens;
    private HashMapGen bodyTokens;
    private HashMapGen toTeokens;
    private HashMapGen fromTokens;
    private HashMapGen subjectTokens;

    public TempHashGen() {
        bodyTokens = new HashMapGen();
        toTeokens = new HashMapGen();
        fromTokens = new HashMapGen();
        subjectTokens = new HashMapGen();
        dateTokens = new HashMapGen();
    }

    public HashMapGen getDateTokens() {
        return dateTokens;
    }

    public void setDateTokens(HashMapGen dateTokens) {
        this.dateTokens = dateTokens;
    }

    public HashMapGen getBodyTokens() {
        return bodyTokens;
    }

    public void setBodyTokens(HashMapGen bodyTokens) {
        this.bodyTokens = bodyTokens;
    }

    public HashMapGen getToTeokens() {
        return toTeokens;
    }

    public void setToTeokens(HashMapGen toTeokens) {
        this.toTeokens = toTeokens;
    }

    public HashMapGen getFromTokens() {
        return fromTokens;
    }

    public void setFromTokens(HashMapGen fromTokens) {
        this.fromTokens = fromTokens;
    }

    public HashMapGen getSubjectTokens() {
        return subjectTokens;
    }

    public void setSubjectTokens(HashMapGen subjectTokens) {
        this.subjectTokens = subjectTokens;
    }
}
