package com.hive.hive.utils;

import android.content.res.Resources;
import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Nícolas Oreques de Araujo on 1/28/18.
 * General purpose class to store methods used across all the application that don't belong to any
 * specific part of it.
 *
 */

public abstract class Utils {
    /**
     * Extracts a trimmed string from a TextView element
     *
     * @param v Field that contains the value to be extracted
     * @return TextView value as a trimmed string
     */
    public static String getText(TextInputEditText v) {
        return v.getText().toString().trim();
    }
    public static String getText(TextView v) {
        return v.getText().toString().trim();
    }

    private static Pattern PATTERN_GENERIC = Pattern.compile("[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}");
    private static Pattern PATTERN_NUMBERS = Pattern.compile("(?=^((?!((([0]{11})|([1]{11})|([2]{11})|([3]{11})|([4]{11})|([5]{11})|([6]{11})|([7]{11})|([8]{11})|([9]{11})))).)*$)([0-9]{11})");

    public static boolean isValid(String cpf) {
        if (cpf != null && PATTERN_GENERIC.matcher(cpf).matches()) {
            cpf = cpf.replaceAll("-|\\.", "");
            if (cpf != null && PATTERN_NUMBERS.matcher(cpf).matches()) {
                int[] numbers = new int[11];
                for (int i = 0; i < 11; i++) numbers[i] = Character.getNumericValue(cpf.charAt(i));
                int i;
                int sum = 0;
                int factor = 100;
                for (i = 0; i < 9; i++) {
                    sum += numbers[i] * factor;
                    factor -= 10;
                }
                int leftover = sum % 11;
                leftover = leftover == 10 ? 0 : leftover;
                if (leftover == numbers[9]) {
                    sum = 0;
                    factor = 110;
                    for (i = 0; i < 10; i++) {
                        sum += numbers[i] * factor;
                        factor -= 10;
                    }
                    leftover = sum % 11;
                    leftover = leftover == 10 ? 0 : leftover;
                    return leftover == numbers[10];
                }
            }
        }
        return false;
    }

    public static Map<String, String> getHashMapFilter(){
        Map<String, String> mmap = new HashMap<>();
        mmap.put("res/drawable/ic_category_services.xml", "Services");
        mmap.put("res/drawable/ic_category_gardening.xml", "Gardening");
        mmap.put("res/drawable/ic_category_cleaning.xml", "Cleaning");
        mmap.put("res/drawable/ic_category_all.xml", "All");
        mmap.put("res/drawable/ic_category_security.xml", "Security");
        mmap.put("res/drawable/ic_outros_laranja.xml", "Others");

        return mmap;
    }

    public static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    public static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
