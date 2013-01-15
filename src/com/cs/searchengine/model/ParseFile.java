/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.model;

import com.cs.searchengine.TmpIndexing;
import com.cs.searchengine.entities.DocumentEntity;
import com.cs.searchengine.entities.UrlDocEntity;
import com.cs.searchengine.entities.HashMapGen;
import com.cs.searchengine.entities.Statistic;
import com.cs.searchengine.entities.TempHashGen;
import com.cs.searchengine.entities.TokensEntity;
import com.cs.searchengine.services.DocumentContainer;
import com.cs.searchengine.services.RegexContainer;
import com.cs.searchengine.services.SpecialChar;
import com.cs.searchengine.services.StringManipulator;
import com.cs.searchengine.services.TokenContainer;
import com.cs.searchengine.services.TupleContainer;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

/**
 *
 * @author hendri
 */
public class ParseFile implements Callable {

    private File file;
    private FileInputStream fstream;
    private DataInputStream in;
    private BufferedReader br;
    private String path;
    private String temp;
    private String strLine;
    private String tokens;
    private TokensEntity tokenEntity;
    private Integer job, done;
    private int lines;
    private String[] result;
    private int tuple;
    private ProcessFile walker;
    private TempHashGen tempHash;
    private int position;
    private String before;
    private long end;

    public ParseFile() {
        done = new Integer(1);
    }

    /**
     * @author hendri
     * @param job
     */
    public void setJob(int job) {
        this.job = job;
    }

    /**
     * @author hendri
     * @param walker
     */
    public void setWalker(ProcessFile walker) {
        this.walker = walker;
    }

    /**
     * @author hendri
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @author hendri method will performed. method is a parser that will parse
     * all of word that find in every each tuple for each files that find from
     * file walker and the method will add the word and count the term to the
     * temporary hashmap return void
     * @throws FileNotFoundException
     */
    public void parseProgress() throws FileNotFoundException {
        try {
            position = 0;
            tempHash = new TempHashGen();
            file = new File(this.path);
            fstream = new FileInputStream(file);
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));

            int end = 0;
            boolean body = false;
            String symbols = " ";
            int near = 0;
            //documents indexing

            DocumentEntity docEnt;

            if (!DocumentContainer.getDocuments().contains(path)) {
//                docEnt = new DocumentEntity();
//                docEnt.setIndex(job.toString());
//                docEnt.setUrl(path);
//                DocumentContainer.getDocuments().put(path, docEnt);
                DocumentContainer.addUrlToFile(job.toString(), path);
            }

            while ((strLine = br.readLine()) != null) {

                strLine = strLine.trim();

                lines = 94;
//                RegexContainer.setRegex("X-\\w+:\\w*-*.*\\\\*\\_*\\s*");
                RegexContainer.setRegex("X-\\w+:\\w*-*.*\\\\*\\_*\\s*");
                RegexContainer.setPattern(Pattern.compile(RegexContainer.getRegex()));
                Matcher matcher = RegexContainer.getPattern().matcher(strLine);

                if (matcher.matches()) {
                    continue;
                }

                if (!TupleContainer.getTuples().containsKey(strLine.substring(0, strLine.indexOf(":") + 1))) {
                    if (body == false) {
                        if (strLine.indexOf("Content-") > -1) {
                            body = true;
                            continue;
                        } else {
                            continue;
                        }
                    }
                }

                if (body == false) {
                    temp = strLine.substring(0, strLine.indexOf(":") + 1);
                    strLine = strLine.substring(strLine.indexOf(":") + 1, strLine.length());
                } else {
                    temp = "Content-";
                    if ((strLine.indexOf(" ") == -1)
                            && (strLine.length() > 45)) {
                        continue;
                    }
                }

                if (strLine.length() == 0) {
                    continue;
                }

                RegexContainer.setRegex(SpecialChar.SYMBOL_REGEX);
                RegexContainer.setPattern(Pattern.compile(RegexContainer.getRegex()));

                Matcher lastMatcher = RegexContainer.getPattern().matcher(strLine);

                strLine = lastMatcher.replaceAll("#");
                result = strLine.split("#");

                if (strLine.length() == 0) {
                    break;
                }

                for (int i = 0; i < result.length; i++) {

                    tokens = result[i];
                    tokens = tokens.trim();
                    tokens = tokens.toLowerCase();

                    if (tokens.length() == 0) {
                        continue;
                    }

                    if ((tokens.length() == 1) && (!tokens.equals("a"))) {
                        continue;
                    }

                    if (tokens.matches("\\d+\\w+")) {
                        tokens = tokens.replaceAll("\\d+", "");
                        tokens.trim();
                    }

                    if (tokens.matches("\\d+")) {
                        continue;
                    }


                    if (tokens.length() > 26) {
                        continue;
                    }

                    if (tokens.length() == 0) {
                        continue;
                    }

                    tuple = TupleContainer.getTuples().get(temp);
                    if (position != 0) {

                        if (!temp.equals(before)) {
                            position = 0;
                        }

                        before = temp;

                    }
                    position++;

                    switch (tuple) {
                        case 1:
                            addTokens(tempHash.getToTeokens());
                            break;
                        case 2:
                            addTokens(tempHash.getFromTokens());
                            break;
                        case 3:
//                                System.out.println("Date pass parsing...");
                            addTokens(tempHash.getDateTokens());
                            break;
                        case 4:
                            addTokens(tempHash.getSubjectTokens());
                            break;
                        case 5:
                            addTokens(tempHash.getBodyTokens());
                            break;
                    }


                }

            }
            body = false;
            in.close();
            br.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("string" + strLine + "|======================================================>> and lines :" + this.lines);
            e.printStackTrace();
        }
        in = null;
        br = null;
        file = null;
        result = null;

    }

    /**
     * @author hendri method performance. To add a token to the temporary
     * hashmap and check the token condition that the token have input to the
     * hashmap before
     * @param tokenHash
     */
    public void addTokens(HashMapGen tokenHash) {

        TokensEntity data;
        UrlDocEntity urlEnt;
        //HashMap<String, UrlDocEntity> hashDoc;
        DocumentEntity docEnt;

        int count = 0;
        int urlCount = 0;
        lines = 219;
        String temp;

        Statistic.setAllTokens(Statistic.getAllTokens() + 1);
        if (!tokenHash.containsKey(tokens)) {
            lines = 221;
            data = new TokensEntity();
            urlEnt = new UrlDocEntity();
            //hashDoc = new HashMap<String, UrlDocEntity>();

            data.setCount(1);
            data.setTokens(tokens);
            data.setDocFreq(1);
//            data.setWeight(0.0);

            urlEnt.setTermFreq(1);
            urlEnt.setIndex(job.toString());
            temp = VByteCoverter.decToVByte(position);
            urlEnt.setTermPosition(temp);
            data.getUrl().put(job.toString(), urlEnt);
            // System.out.println(data.getUrl());

            tokenHash.put(tokens, data);

        } else {
            lines = 231;
            data = tokenHash.get(tokens);
            count = data.getCount();
            data.setCount(count + 1);

            if (data.getUrl().containsKey(job.toString())) {
                urlEnt = data.getUrl().get(job.toString());
                urlEnt.setTermFreq(urlEnt.getTermFreq() + 1);
                temp = urlEnt.getTermPosition() + "," + VByteCoverter.decToVByte(position);
                urlEnt.setTermPosition(temp);
//                System.out.println("parsing url found");
            } else {
                urlEnt = new UrlDocEntity();
                urlEnt.setIndex(job.toString());
                urlEnt.setTermFreq(1);
                temp = VByteCoverter.decToVByte(position);

//                System.out.println("parsing url not found");
            }
            urlEnt.setTermPosition(temp);
            data.getUrl().put(job.toString(), urlEnt);
            lines = 244;
            tokenHash.put(tokens, data);

        }
//        System.out.println("inverted : " + tokenHash.get(tokens).getUrl().get(job.toString()).getTermPosition()
//                + " | temp : " + temp);

    }

    @Override
    public Boolean call() throws Exception {
//        parseProgress();
        if (walker == null) {
            walker = new ProcessFile();
        }
        walker.listener(job, tempHash);
        return true;
    }

    public void after5Th() {

        // System.out.println("up");
//        System.out.println("marge out of container");
        TokenContainer.margeTokens(TokenContainer.getDateTokens(), tempHash.getDateTokens());
        TokenContainer.margeTokens(TokenContainer.getFromTokens(), tempHash.getFromTokens());
        TokenContainer.margeTokens(TokenContainer.getToTeokens(), tempHash.getToTeokens());
        TokenContainer.margeTokens(TokenContainer.getSubjectTokens(), tempHash.getSubjectTokens());
        TokenContainer.margeTokens(TokenContainer.getBodyTokens(), tempHash.getBodyTokens());
        if (job % 1000 == 0) {
            System.out.println("task finished : " + job + " from: " + Statistic.getCountsFiles() + " files, time : " + ((System.currentTimeMillis() - Statistic.getStarTime()) / 1000) + " sec");
        }

//        System.out.println(" job : " + job);
        if ((job % 5000 == 0) || (job >= Statistic.getCountsFiles())) {
            System.out.println("clear map... paket send to file ==>>" + job);
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
        if (job >= Statistic.getCountsFiles()) {


            DocumentContainer.closeProntTolFile();

            System.out.println("");
            end = System.currentTimeMillis();
            end = end - Statistic.getStarTime();
            System.out.println("waktu =========> " + end);

            System.out.println("\n\n");
//
//            Set<String> key;
//            Set<String> url;
//            HashMap<String, UrlDocEntity> urls;
//            LinkedHashMap<String, TokensEntity> list = null;
//            Set<String> tuples = TupleContainer.getTuples().keySet();
//
//            tuples = TupleContainer.getTuples().keySet();
//            String field = null;
//            //for each field
//            for (String tuple : tuples) {
//                switch (TupleContainer.getTuples().get(tuple)) {
//                    case 1:
//                        field = "To";
//                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getToTeokens());
//                        break;
//                    case 2:
//                        field = "From";
//                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getFromTokens());
//                        break;
//                    case 3:
//                        field = "Date";
//                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getDateTokens());
//                        break;
//                    case 4:
//                        field = "Subject";
//                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getSubjectTokens());
//                        break;
//                    case 5:
//                        tuple = "Body";
//                        list = (LinkedHashMap<String, TokensEntity>) TokenContainer.sortByComparator(TokenContainer.getBodyTokens());
//                        break;
//                }
//
//                int k = 1;
//                key = list.keySet();
//                System.out.println(" Load information tokens for " + tuple + " " + list.size() + " tokens");
//
////                TokenContainer.printInformation(tuple, list);
//                System.out.println("=================================================================================================================");
//                System.out.println("Done.....\n\n");
//                end = System.currentTimeMillis();
//                end = end - Statistic.getStarTime();
//                System.out.println("waktu field " + tuple + " =========> " + end);
//
//                for (String term : key) {
//                    System.out.println(" token : " + list.get(term).getTokens());
//                    System.out.println(" inverted index : " + list.get(term).getInverted());
//                    System.out.println("======================================================");
//                }


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
//            serviceEx.shutdown();
//            serviceEx.awaitTermination((long) 100, TimeUnit.MILLISECONDS);

    }
}
