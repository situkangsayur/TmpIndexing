/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cs.searchengine.services;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hendri
 */
public class RegexContainer {

    private static Pattern pattern;
    private static Matcher matcher;
    private static String regex;
    private static String target;

    /**
     *
     * @return
     */
    public static String getRegex() {
        return regex;
    }

    /**
     *
     * @param regex
     */
    public static void setRegex(String regex) {
        RegexContainer.regex = regex;
    }

    /**
     *
     * @return
     */
    public static String getTarget() {
        return target;
    }

    /**
     *
     * @param target
     */
    public static void setTarget(String target) {
        RegexContainer.target = target;
    }

    /**
     *
     * @return
     */
    public static Pattern getPattern() {
        return pattern;
    }

    /**
     *
     * @param pattern
     */
    public static void setPattern(Pattern pattern) {
        RegexContainer.pattern = pattern;
    }

    /**
     *
     * @return
     */
    public static Matcher getMatcher() {
        return matcher;
    }

    /**
     *
     * @param matcher
     */
    public static void setMatcher(Matcher matcher) {
        RegexContainer.matcher = matcher;
    }
}
