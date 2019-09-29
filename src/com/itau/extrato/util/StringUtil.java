package com.itau.extrato.util;

import java.text.Normalizer;

public final class StringUtil {

    public static String paddingRight(String str,int padding){
        return String.format("%1$-" + padding + "s", str);
    }

    public static String convertToAscII(String str) {
        if(str == null || str.trim().isEmpty()){
            return "";
        }
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String convertFromCurrency(String value){

        if(value == null || value.trim().isEmpty()){
            return "";
        }

        value = value.replace(".", "");
        value = value.replace(",", ".");
        value = value.replace(" ", "");
        return value;
    }
}
