/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.model;

/**
 *
 * @author hendri
 */
public class VByteCoverter {

    private static Integer index;
    private Long newIndex;
    private static String binary;
    private static String hexa;

    public static String getHexa() {
        return hexa;
    }

    public static void setHexa(String hexa) {
        VByteCoverter.hexa = hexa;
    }

    public static Integer getIndex() {
        return index;
    }

    public static void setIndex(Integer index) {
        VByteCoverter.index = index;
    }

    public Long getNewIndex() {
        return newIndex;
    }

    public void setNewIndex(Long newIndex) {
        this.newIndex = newIndex;
    }

    public static String getBinary() {
        return binary;
    }

    public static void setBinary(String binary) {
        VByteCoverter.binary = binary;
    }

    public static String decToVByte(Integer index) {
        String temp = index.toBinaryString(index);

        int length = temp.length();
//        System.out.println("binter sebelum " + temp);
        int k = 0;

        for (int i = length - 8; i >= 0;) {
            if ((temp.charAt(i) == '1') || (i != 0)) {
                if (k == 0) {
                    temp = temp.substring(0, i + 1).concat("1").concat(temp.substring(i + 1, temp.length()));
                } else {
                    temp = temp.substring(0, i + 1).concat("0").concat(temp.substring(i + 1, temp.length()));
                }
                i++;
                k++;
            }
            if ((i != 0) && ((i - 8) >= 0)) {
                i = i - 8;
            } else {
                break;
            }
        }

        while (temp.length() % 8 != 0) {
            if ((k == 0) && (temp.length() == 7)) {
                temp = "1".concat(temp);
            } else {
                temp = "0".concat(temp);
            }
        }


        Integer newInt = Integer.parseInt(temp, 2);
//        System.out.println("biner" + temp);
        VByteCoverter.hexa = Integer.toHexString(newInt);

        return VByteCoverter.hexa;
    }

    public static Integer vByteToDec(String vByte) {


        Integer byteStr = Integer.parseInt(vByte, 16);
        String byteTemp = Integer.toBinaryString(byteStr);

        while (byteTemp.length() % 8 != 0) {
            byteTemp = "0".concat(byteTemp);
        }
//        System.out.println("" + byteTemp);
        String temp = "";
        int length = byteTemp.length();

        for (int i = length; i >= 1; i--) {

            if (((i - 1) % 8 == 0) && (i > 1)) {
                continue;
            }
            temp = byteTemp.charAt(i - 1) + temp;
//            System.out.println(temp);
        }
//        System.out.println("" + temp);
        VByteCoverter.index = Integer.parseInt(temp, 2);

        return VByteCoverter.index;
    }
}
