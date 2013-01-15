/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.model;

import com.cs.searchengine.entities.HashMapGen;
import com.cs.searchengine.entities.TokensEntity;
import com.cs.searchengine.entities.UrlDocEntity;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author hendri
 */
public class DeltaConverter {

    private ConcurrentHashMap<String, UrlDocEntity> listDoc;
    private String result;

    public ConcurrentHashMap<String, UrlDocEntity> getListDoc() {
        return listDoc;
    }

    public void setListDoc(ConcurrentHashMap<String, UrlDocEntity> listDoc) {
        this.listDoc = listDoc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String generateDelta(ConcurrentHashMap<String, UrlDocEntity> list) {
        StringBuffer temp = null;


        LinkedHashMap<String, UrlDocEntity> tempList = (LinkedHashMap<String, UrlDocEntity>) sortByComparator(list);
        Integer tempInt = null;
        Set<String> key = tempList.keySet();
        int j = 0;
        for (String idx : key) {

            String number = VByteCoverter.decToVByte(new Integer(idx));
//            String number = idx;
            //System.out.println("" + VByteCoverter.decToVByte(new Integer(idx)));
            if (temp == null ) {
//                tempInt = number;
                temp = new StringBuffer(number + "," + VByteCoverter.decToVByte(tempList.get(idx).getTermFreq()));
            } else {
                temp.append("," + number + "," + VByteCoverter.decToVByte(tempList.get(idx).getTermFreq()));
            }
            temp.append("," + tempList.get(idx).getTermPosition());

//            temp += tempSource;
            j++;
        }
        return temp.toString();
    }

    public String generateDelta() {
        StringBuffer temp = null;


        LinkedHashMap<String, UrlDocEntity> tempList = (LinkedHashMap<String, UrlDocEntity>) sortByComparator(listDoc);
        Integer tempInt = new Integer(0);
        Integer before = null;
        Set<String> key = tempList.keySet();
        int j = 0;

        for (String idx : key) {
            tempInt = Integer.valueOf(idx);
            if (before == null) {
                before = new Integer(tempInt);
            } else {
                tempInt = tempInt - before;
                before = before + tempInt;
            }


            String number = VByteCoverter.decToVByte(tempInt);

//            String number = idx;
            //System.out.println("" + VByteCoverter.decToVByte(new Integer(idx)));
            if (temp == null) {
//                tempInt = number;
                temp = new StringBuffer(number + "," + VByteCoverter.decToVByte(tempList.get(idx).getTermFreq()));
            } else {
                temp.append("," + number + "," + VByteCoverter.decToVByte(tempList.get(idx).getTermFreq()));
            }

            temp.append("," + tempList.get(idx).getTermPosition());
            j++;
        }
        return temp.toString();
    }

    /**
     * @author hendri : to sorting the main hashmap and change to be Map that
     * have been sorted by weight
     * @param unsortMap
     * @return
     */
    public static Map<String, UrlDocEntity> sortByComparator(ConcurrentHashMap<String, UrlDocEntity> unsortMap) {

        List<UrlDocEntity> list = new LinkedList<UrlDocEntity>(unsortMap.values());

        return selectionSort(list);
    }

    /**
     * @author hendri : to sort the List of values from the hashmap or main
     * hashmap and it will be sorting by Collection class and using sort method
     * from collection interface
     * @param unsortList
     * @return
     */
    public static Map<String, UrlDocEntity> selectionSort(List<UrlDocEntity> unsortList) {
        List<UrlDocEntity> sortList = unsortList;

        //Collections.sort(unsortList, );
        Collections.sort(sortList, new Comparator<UrlDocEntity>() {
            public int compare(UrlDocEntity o1, UrlDocEntity o2) {
                UrlDocEntity val1 = o1;
                UrlDocEntity val2 = o2;
                return (Integer.compare(new Integer(val1.getIndex()), new Integer(val2.getIndex())));
            }
        });
        Map<String, UrlDocEntity> sortedMap = new LinkedHashMap();

        for (int i = 0; i < sortList.size(); i++) {
            sortedMap.put(sortList.get(i).getIndex(), sortList.get(i));
        }
//        System.out.println(" size sorted list :" + sortList.size());
        return sortedMap;
    }

    public ConcurrentHashMap<String, UrlDocEntity> inverseDelta(String delta) {
        String tempDelta = delta;
        listDoc = new ConcurrentHashMap<String, UrlDocEntity>();
        int i = 0;
        String page = null;
        Integer tempPage = new Integer(0);
        while (tempDelta.indexOf(",") != -1) {
            String tempPosition = "";

            UrlDocEntity newEnt = new UrlDocEntity();
            page = tempDelta.substring(0, tempDelta.indexOf(","));
            page = VByteCoverter.vByteToDec(page).toString();
            tempPage = Integer.valueOf(page);
            if (i > 0) {
              //delta
            }

            tempDelta = tempDelta.substring(tempDelta.indexOf(",") + 1, tempDelta.length());
            Integer freq = VByteCoverter.vByteToDec(tempDelta.substring(0, tempDelta.indexOf(",")));

            tempDelta = tempDelta.substring(tempDelta.indexOf(",") + 1, tempDelta.length());
            newEnt.setIndex(page);
            newEnt.setTermFreq(freq);

            for (int j = 0; j < freq; j++) {
                if (tempDelta.indexOf(",") != -1) {
                    tempPosition += tempDelta.substring(0, tempDelta.indexOf(","));
                    tempDelta = tempDelta.substring(tempDelta.indexOf(",") + 1, tempDelta.length());
                } else {
                    tempPosition += tempDelta.substring(0, tempDelta.length());
                    //tempDelta = tempDelta.substring(tempDelta.indexOf(",") + 1, tempDelta.length());
                }

                if (j + 1 != freq) {
                    tempPosition += ",";
                }
            }
            newEnt.setTermPosition(tempPosition);
            i++;
            listDoc.put(page, newEnt);
        }
        return listDoc;
    }

    public String addUrlToInverted(String inverted, UrlDocEntity newData) {

        ConcurrentHashMap<String, UrlDocEntity> newList = inverseDelta(inverted);

        newList.put(newData.getIndex(), newData);

        return generateDelta(newList);

    }
}
