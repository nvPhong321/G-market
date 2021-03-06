package com.example.phong.g_market.ultil;

/**
 * Created by phong on 8/25/2017.
 */

public class StringManupulation {
    public static String expandCost(String cost){
        return cost.replace(",","");
    }
    public static String condenseUsername(String username){
        return username.replace(" ",".");
    }
    public static String getTags(String string){
        if (string.indexOf("#") > 0){
            StringBuilder stringBuilder = new StringBuilder();
            char[] charArray = string.toCharArray();
            boolean foundWord = false;
            for (char c : charArray){
                if(c == '#'){
                    foundWord = true;
                    stringBuilder.append(c);
                }else {
                    if (foundWord) {
                        stringBuilder.append(c);
                    }
                }if(c == ' '){
                    foundWord = false;

                }
            }
            String s = stringBuilder.toString().replace(" ", "").replace("#", ",#");
            return s.substring(1,s.length());
        }
        return string;
    }
}
