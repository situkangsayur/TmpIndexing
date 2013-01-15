/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.model;

import com.cs.searchengine.TmpIndexing;
import com.cs.searchengine.entities.DocumentEntity;
import com.cs.searchengine.entities.Statistic;
import com.cs.searchengine.entities.HashMapGen;
import com.cs.searchengine.entities.TempHashGen;
import com.cs.searchengine.entities.TokensEntity;
import com.cs.searchengine.entities.UrlDocEntity;
import com.cs.searchengine.services.DocumentContainer;
import com.cs.searchengine.services.TokenContainer;
import com.cs.searchengine.services.TupleContainer;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hendri this class for walking files activity, to explore all of
 * directory that have send from main class and do parsing activity to add all
 * of token to the hashmap. This class will do all of activity in multithread.
 * So first thread will do searching files to explore all of possible directory
 * and all of files and another threads will process the parserProgress for each
 * files that have got from the first thread
 */
public class ProcessFile extends SimpleFileVisitor<Path> {

    private ParseFile parser;
    private Collection<ParseFile> listTask;
    private List<String> listPath;
    private Integer jumlahFiles;
    private StringBuilder outPut;
    private long start;
    private long end;
    private long midle;
    private ExecutorService serviceEx;
    private Runtime runtime = Runtime.getRuntime();

    /**
     *
     * @param start
     */
    public ProcessFile(long start, Collection<ParseFile> listTask, ExecutorService serviceEx, List<String> listPath) {
        this.start = start;
        this.jumlahFiles = Statistic.COUNT;
        this.outPut = new StringBuilder();
        this.listTask = listTask;
        this.serviceEx = serviceEx;
        this.listPath = listPath;
    }

    public ProcessFile() {
    }

    public void sentUrlToFile(String job, String path) {
        DocumentEntity docEnt;

        if (!DocumentContainer.getDocuments().contains(path)) {
            docEnt = new DocumentEntity();
            docEnt.setIndex(job);
            docEnt.setUrl(path);
            DocumentContainer.getDocuments().put(path, docEnt);
            DocumentContainer.addUrlToFile(job, path);
        }
    }

    /*
     * @author hendri
     * method to explore all of possible "files"
     */
    @Override
    public FileVisitResult visitFile(
            Path aFile, BasicFileAttributes aAttrs) throws IOException {

        if (Statistic.getCountsFiles() > jumlahFiles) {
            return FileVisitResult.TERMINATE;
        } else {
            if (aFile.toString().indexOf("all_documents") == -1) {
                Statistic.setFiles();

//                parser = new ParseFile();
//                parser.setPath(aFile.toString());
//                parser.setJob(Statistic.getCountsFiles());
//                parser.setWalker(this);

//                sentUrlToFile(Statistic.getCountsFiles().toString(), aFile.toString());

                if (Statistic.getCountsFiles() % 1000 == 0) {
                    outPut.append("task stack : " + Statistic.getCountsFiles());
                    System.out.println(outPut);
                    outPut.delete(0, outPut.length());
                }
//                listTask.add(parser);
                listPath.add(aFile.toString());
//                serviceEx.submit(parser);
//                parser.parseProgress();
//                try {
//                    parser.call();
//                    //serviceEx.
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }

                return FileVisitResult.CONTINUE;
            } else {
                //    System.out.println("\nskip" + aFile.toString() + "\n");
                return FileVisitResult.SKIP_SIBLINGS;
            }
        }
    }

    /*
     * @author hendri
     * method to explore all of possible "directories"
     */
    @Override
    public FileVisitResult preVisitDirectory(
            Path aDir, BasicFileAttributes aAttrs) throws IOException {
        Statistic.setDirectory();
        return FileVisitResult.CONTINUE;

    }

    /**
     * @author hendri Method performance. This method will calling from class
     * ParseFile when the progress of parsing file activity is done, and the
     * listener will accept the tikenHash parameter that contains temporary hash
     * all of token that find in a file, and then the this method will marge all
     * of tokenHash tokens to the Main Token in TokenContainer. And the listener
     * will show all of tokens that contains in TokenContainers
     * @param done
     * @param tokenHash
     * @throws InterruptedException
     */
    public void listener(int done, TempHashGen tokenHash) throws InterruptedException {

        // System.out.println("up");
//        System.out.println("marge out of container");
        TokenContainer.margeTokens(TokenContainer.getDateTokens(), tokenHash.getDateTokens());
        TokenContainer.margeTokens(TokenContainer.getFromTokens(), tokenHash.getFromTokens());
        TokenContainer.margeTokens(TokenContainer.getToTeokens(), tokenHash.getToTeokens());
        TokenContainer.margeTokens(TokenContainer.getSubjectTokens(), tokenHash.getSubjectTokens());
        TokenContainer.margeTokens(TokenContainer.getBodyTokens(), tokenHash.getBodyTokens());
        if (done % 1000 == 0) {
            System.out.println("task finished : " + done + " from: " + Statistic.getCountsFiles() + " files, time : " + ((System.currentTimeMillis() - start) / 1000) + " sec");
        }


        if ((done % 5000 == 0) || (done >= Statistic.getCountsFiles())) {
            System.out.println("clear map... paket send to file ==>>" + done);
            Statistic.setFileStep(Statistic.getFileStep() + 1);
            LinkedHashMap<String, TokensEntity> list = null;
            Set<String> tuples = TupleContainer.getTuples().keySet();
            String field = null;
//            DocumentContainer.closeProntTolFile();
            //for each field
//            serviceEx.awaitTermination(120, TimeUnit.SECONDS);
            System.out.println("tuple size " + tuples.size());
            for (int i = TupleContainer.getTuples().size(); i >= 1; i--) {
                //   field = tuple;
//                System.out.println("tuple number : " + TupleContainer.getTuples().get(tuple));
                System.out.println("tuple number : " + i);
//                switch (TupleContainer.getTuples().get(tuple)) {
                switch (i) {

                    case 1:
                        field = "To";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getToTeokens());
                        System.out.println("To : " + list.size());
                        break;

                    case 2:
                        field = "From";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getFromTokens());
                        System.out.println("From : " + list.size());
                        break;
                    case 3:
                        field = "Date";
                        System.out.println(" pass date ");
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getDateTokens());
                        System.out.println("Date : " + list.size());
                        break;
                    case 4:
                        field = "Subject";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getSubjectTokens());
                        System.out.println("Subject :" + list.size());
                        break;
                    case 5:
                        field = "Body";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getBodyTokens());
                        System.out.println("body size: " + list.size());
                        break;
                }
                TokenContainer.printToEchDoc(field, Statistic.getFileStep(), list);

            }

            TokenContainer.setBodyTokens(null);
            TokenContainer.setDateTokens(null);
            TokenContainer.setFromTokens(null);
            TokenContainer.setToTeokens(null);
            TokenContainer.setSubjectTokens(null);
//            System.gc();
            list = null;
            System.gc();
//            serviceEx.shutdown();
        }
        // System.out.println("down");
        /**
         * Terminate job while all job has finished
         */
        if (done >= Statistic.getCountsFiles()) {


            DocumentContainer.closeProntTolFile();

            System.out.println("");
            end = System.currentTimeMillis();
            end = end - start;
            System.out.println("waktu =========> " + end);

            System.out.println("\n\n");

            Set<String> key;
            Set<String> url;
            HashMap<String, UrlDocEntity> urls;
            LinkedHashMap<String, TokensEntity> list = null;
            Set<String> tuples = TupleContainer.getTuples().keySet();

            tuples = TupleContainer.getTuples().keySet();
            String field = null;
            //for each field
            for (String tuple : tuples) {
                switch (TupleContainer.getTuples().get(tuple)) {
                    case 1:
                        field = "To";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getToTeokens());
                        break;
                    case 2:
                        field = "From";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getFromTokens());
                        break;
                    case 3:
                        field = "Date";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getDateTokens());
                        break;
                    case 4:
                        field = "Subject";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getSubjectTokens());
                        break;
                    case 5:
                        tuple = "Body";
                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getBodyTokens());
                        break;
                }

                int k = 1;
                key = list.keySet();
                System.out.println(" Load information tokens for " + tuple + " " + list.size() + " tokens");

//                TokenContainer.printInformation(tuple, list);
                System.out.println("=================================================================================================================");
                System.out.println("Done.....\n\n");
                end = System.currentTimeMillis();
                end = end - start;
                System.out.println("waktu field " + tuple + " =========> " + end);

                for (String term : key) {
                    System.out.println(" token : " + list.get(term).getTokens());
                    System.out.println(" inverted index : " + list.get(term).getInverted());
                    System.out.println("======================================================");
                }


//                for (String term : key) {
//                    System.out.println(" term : " + term);
//                    urls = list.get(term).getUrl();
//                    url = urls.keySet();
//                    int i = 0;
//                    for (String get : url) {
//                        i++;
//                        System.out.println(i + " url : " + get
//                                + " ; freq : " + urls.get(get).getTermFreq() + " ;");
//                    }
//
//                }

                System.out.println("waktu field " + tuple + " =========> " + end);
            }

            //for all field :

//            System.out.println("\n\n All field");
//            HashMapGen allHashMap = new HashMapGen();
//            
//            TokenContainer.margeTokens(allHashMap, TokenContainer.getDateTokens());
//            TokenContainer.margeTokens(allHashMap, TokenContainer.getFromTokens());
//            TokenContainer.margeTokens(allHashMap, TokenContainer.getToTeokens());
//            TokenContainer.margeTokens(allHashMap, TokenContainer.getSubjectTokens());
//            TokenContainer.margeTokens(allHashMap, TokenContainer.getBodyTokens());
//            
//            list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(allHashMap);
//            
//            
//            int k = 1;
//            key = list.keySet();
//            System.out.println(" Load information tokens for " + " All " + " " + list.size() + " tokens");
//
//            // TokenContainer.printInformation("All", list);
//            System.out.println("=================================================================================================================");
//            System.out.println("Done.....\n\n");
//            end = System.currentTimeMillis();
//            end = end - start;
//            System.out.println("waktu All field =========> " + end);

            //for all field
            serviceEx.shutdown();
            serviceEx.awaitTermination((long) 100, TimeUnit.MILLISECONDS);

        }
    }
}
