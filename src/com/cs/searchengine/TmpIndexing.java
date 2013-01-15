/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine;

import com.cs.searchengine.entities.HashMapGen;
import com.cs.searchengine.entities.Statistic;
import com.cs.searchengine.model.ParseFile;
import com.cs.searchengine.model.ProcessFile;
import com.cs.searchengine.services.TokenContainer;
import com.cs.searchengine.services.TupleContainer;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author hendri
 */
public class TmpIndexing {
    
    public static HashMapGen test;
    public static Collection<ParseFile> listTask;
    public static boolean allDoct;
    public static String outPut;
    public static final int jumlahFiles = 1000;
    public static final long start = System.currentTimeMillis();
    public static long end;
    public static ExecutorService serviceEx;
    public static int availableProc = Runtime.getRuntime().availableProcessors();
    public static List<String> listPath;
    
    public static void main(String[] args) throws IOException {
//        String ROOT = "/home/hendri/enron_mail_20110402/";
        outPut = "";
//        System.out.println("56639");
//        outPut = VByteCoverter.decToVByte(56639);
//
//        System.out.println("desimal : " + VByteCoverter.vByteToDec(outPut));
//        System.out.println("");
//        System.out.println("33545535");
//        outPut = VByteCoverter.decToVByte(33545535);
//
//        System.out.println("desimal : " + VByteCoverter.vByteToDec(outPut));
//        System.out.println("");
//        System.out.println("hexa vbyte : " + outPut);

        
        if (args.length == 0) {
            System.exit(0);
        }

//        DeltaConverter convertD = new DeltaConverter();
//
//        HashMap<String, UrlDocEntity> list = new HashMap<String, UrlDocEntity>();
//        UrlDocEntity data1 = new UrlDocEntity();
//        
//        
//        data1.setIndex("56639");
//        data1.setTermFreq(2);
//        data1.setTermPosition("1,2");
//        
//        list.put("56639", data1);
//        data1 = new UrlDocEntity();
//        data1.setIndex("56");
//        data1.setTermFreq(3);
//        data1.setTermPosition("3,4,16");
//        list.put("56", data1);
//        data1 = new UrlDocEntity();
//        data1.setIndex("120");
//        data1.setTermFreq(1);
//        data1.setTermPosition("5");
//        list.put("120", data1);
//        data1 = new UrlDocEntity();
//        data1.setIndex("10");
//        data1.setTermFreq(5);
//        data1.setTermPosition("5,7,9,12,14");
//        list.put("10", data1);
//
//        convertD.setListDoc(list);
//        System.out.println("hasil genrate :" + convertD.generateDelta());
//        String deltaStr = convertD.generateDelta();
////        System.out.println("" + convertD.inverseDelta(deltaStr));
//        HashMap<String, UrlDocEntity> cr = convertD.inverseDelta(deltaStr);
//
//        Set<String> keys = cr.keySet();
//        for (String term : keys) {
//            System.out.println(" index     : " + cr.get(term).getIndex());
//            System.out.println(" Freq      : " + cr.get(term).getTermFreq());
//            System.out.println(" Postition : " + cr.get(term).getTermPosition());
//            System.out.println(" ==================================== ");
//        }

        //1,2,12,3,3,3,416,4,1,55,5,5,5,7,9,1214
        //
        String ROOT = args[0];
        
        if (args[1] != null) {
            Statistic.setkTop(new Integer(args[1]));
        }
        if (listTask == null) {
            listTask = new ArrayList<ParseFile>();
        }
        listPath = new ArrayList<String>();
        
        Statistic.setStarTime(start);
        serviceEx = Executors.newFixedThreadPool(availableProc);
        
        TupleContainer.checkAvailTuples();
        TokenContainer.checkAvailHash();
        
        FileVisitor<Path> fileProcessor = new ProcessFile(start, listTask, serviceEx, listPath);
        //fileProcessor.
        Files.walkFileTree(Paths.get(ROOT), fileProcessor);
//        serviceEx.submit(listTask);
        ParseFile parser = new ParseFile();
        System.out.println("size list path : " + listPath.size());
        for (int i = 0; i < listPath.size(); i++) {
            parser.setPath(listPath.get(i));
            parser.setJob(i + 1);
//            parser.setWalker(fileProcessor);
//            serviceEx.submit(task);
            parser.parseProgress();
//            System.out.println("ke " + i);
            parser.after5Th();
            listPath.set(i, null);
            
        }
        
        System.out.println(" jumlah direktory : " + Statistic.getDirectory());
        System.out.println(" jumlah files : " + Statistic.getCountsFiles());
    }
}
