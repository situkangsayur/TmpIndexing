/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.entities;

/**
 *
 * @author hendri Class to save all of information of files and directory count
 */
public class Statistic {

    private static Integer files;
    private static Integer directory;
    public static final Integer COUNT = 999000;
    public static final String TEAM = "hendri_ken_adhe";
    private static Integer allTokens;
    private static Integer kTop;
    private static Integer fileStep;
    private static Long starTime;

    public static Long getStarTime() {
        return starTime;
    }

    public static void setStarTime(Long starTime) {
        Statistic.starTime = starTime;
    }

    public static void setCount() {
        if ((files == null) || (directory == null)) {
            files = new Integer(0);
            directory = new Integer(0);
        }
        if (allTokens == null) {
            allTokens = new Integer(0);
        }
        if (directory == null) {
            kTop = new Integer(40);
        }
        if (fileStep == null) {
            fileStep = new Integer(0);
        }
    }

    public static Integer getFileStep() {
        setCount();;
        return fileStep;
    }

    public static void setFileStep(Integer fileStep) {
        setCount();
        Statistic.fileStep = fileStep;
    }

    public static Integer getkTop() {
        setCount();
        return kTop;
    }

    public static void setkTop(Integer kTop) {
        setCount();
        Statistic.kTop = kTop;
    }

    public static Integer getAllTokens() {
        setCount();
        return allTokens;
    }

    public static void setAllTokens(Integer allTokens) {
        setCount();
        Statistic.allTokens = allTokens;
    }

    public static int getFiles() {
        setCount();
        return files;
    }

    public static void setFiles() {
        setCount();
        Statistic.files++;
    }

    public static int getDirectory() {
        setCount();
        return directory;
    }

    public static void setDirectory() {
        setCount();
        Statistic.directory++;
    }

    public static Integer getCountsDir() {
        setCount();
        return Statistic.directory;
    }

    public static Integer getCountsFiles() {
        setCount();
        return Statistic.files;
    }
}
