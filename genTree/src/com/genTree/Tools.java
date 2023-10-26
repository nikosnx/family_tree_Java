package com.genTree;

public class Tools {
    public static String format(String s) {
        if (s.length() == 0)
            return s;

        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static String convertToLowerCaseTrim(String s) {
        return s.trim().toLowerCase();
    }

}
