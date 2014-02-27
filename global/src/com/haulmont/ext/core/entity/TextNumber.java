/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mahdi on 2/6/14.
 */
public class TextNumber {

    static Map<Integer, String> ten = initTen();
    static Map <Integer, String> tens = initTens();
    static Map <Integer, String> hundred = initHundred();
    static int iterator = -1;

    private static Map<Integer, String> initTen() {
        Map <Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "один");
        map.put(2, "два");
        map.put(3, "три");
        map.put(4, "четыре");
        map.put(5, "пять");
        map.put(6, "шесть");
        map.put(7, "семь");
        map.put(8, "восемь");
        map.put(9, "девять");
        return map;
    }

    private static Map<Integer, String> initTens() {
        Map <Integer, String> map = new HashMap<Integer, String>();
        map.put(10, "десять");
        map.put(11, "одиннадцать");
        map.put(12, "двенадцать");
        map.put(13, "тринадцать");
        map.put(14, "четырнадцать");
        map.put(15, "пятнадцать");
        map.put(16, "шестнадцать");
        map.put(17, "семнадцать");
        map.put(18, "восемнадцать");
        map.put(19, "девятнадцать");
        map.put(20, "двадцать");
        map.put(30, "тридцать");
        map.put(40, "сорок");
        map.put(50, "пятьдесят");
        map.put(60, "шестьдесят");
        map.put(70, "семьдесят");
        map.put(80, "восемьдесят");
        map.put(90, "девяносто");
        return map;
    }

    private static Map<Integer, String> initHundred() {
        Map <Integer, String> map = new HashMap<Integer, String>();
        map.put(100, "сто");
        map.put(200, "двести");
        map.put(300, "триста");
        map.put(400, "четыреста");
        map.put(500, "пятьсот");
        map.put(600, "шестьсот");
        map.put(700, "семьсот");
        map.put(800, "восемьсот");
        map.put(900, "девятьсот");
        return map;
    }

    private static String initTenString(Integer number) {
        if (iterator == 1) {
            if (number == 1) {iterator --; return "одна тысяча";}
            else if (number == 2) {iterator --; return "две тысячи";}
            else if (number > 2 && number < 5) {iterator --; return ten.get(number) + " тысячи";}
            else {iterator --; return ten.get(number) + " тысяч";}
        } else if (iterator == 2) {
            if (number == 1) {iterator --;return ten.get(number) + " миллион";}
            else if (number > 1 && number < 5) {iterator --; return ten.get(number) + " миллиона ";}
            else {iterator --; return ten.get(number) + " миллионов";}
        } else if (iterator == 3) {
            if (number == 1) {iterator --; return ten.get(number) + " миллиард";}
            else if (number > 1 && number < 5) {iterator --; return ten.get(number) + " миллиарда";}
            else {iterator --; return ten.get(number) + " миллиардов";}
        }
        return ten.get(number);
    }

    private static String initTensString(Integer number) {
        if (number <= 20)
            if (iterator == 1) {iterator --; return tens.get(number) + " тысяч";}
            else if (iterator == 2) {iterator --; return tens.get(number) + " миллионов";}
            else if (iterator == 3) {iterator --; return tens.get(number) + " миллиардов";}
            else {return tens.get(number);}
        else if (number % 10 != 0) return tens.get(Math.round(number / 10)*10) + " " + initTenString(number % 10);
        else return tens.get(number);
    }

    private static String initHundredString(Integer number) {
        if (Math.round(number % 100) == 0)
            if (iterator == 1) {iterator --; return hundred.get(number) + " тысяч";}
            else if (iterator == 2) {iterator --; return hundred.get(number) + " миллионов";}
            else if (iterator == 3) {iterator --; return hundred.get(number) + " миллиардов";}
            else {return hundred.get(number);}
        else if (Math.round(number % 100) > 9) return hundred.get(Math.round(number / 100)*100) + " " + initTensString(Math.round(number % 100));
        else return hundred.get(Math.round(number / 100)*100) + " " + initTenString(Math.round(number % 100));
    }

    private static String init(Integer num, int countZero) {
        String string = "";
        if (countZero == 1) string = initTenString(num);
        if (countZero == 2) string = initTensString(num);
        if (countZero == 3) string = initHundredString(num);
        if (countZero >= 4 && countZero < 7)
            if (Math.abs((Math.round(num / 1000)*1000 - num)) != 0) string = init((Integer)Math.round(num / 1000), String.valueOf(Math.round(num / 1000)).length()) + " "
                    + init(Math.abs((Math.round(num / 1000)*1000 - num)),
                    String.valueOf(Math.abs((Math.round(num / 1000)*1000 - num))).length());
            else string = init((Integer)Math.round(num / 1000), String.valueOf(Math.round(num / 1000)).length());
        if (countZero >= 7 && countZero < 10)
            if (Math.abs((Math.round(num / 1000000)*1000000 - num)) != 0) string = init((Integer)Math.round(num / 1000000), String.valueOf(Math.round(num / 1000000)).length()) + " "
                    + init(Math.abs((Math.round(num / 1000000)*1000000 - num)),
                    String.valueOf(Math.abs((Math.round(num / 1000000)*1000000 - num))).length());
            else string = init((Integer)Math.round(num / 1000000), String.valueOf(Math.round(num / 1000000)).length());
        if (countZero >= 10 && countZero < 12)
            if (Math.abs((Math.round(num / 1000000000)*1000000000 - num)) != 0) string = init((Integer)Math.round(num / 1000000000), String.valueOf(Math.round(num / 1000000000)).length()) + " "
                    + init(Math.abs((Math.round(num / 1000000000)*1000000000 - num)),
                    String.valueOf(Math.abs((Math.round(num / 1000000000)*1000000000 - num))).length());
            else string = init((Integer)Math.round(num / 1000000000), String.valueOf(Math.round(num / 1000000000)).length());
        return string;
    }

    public static String start(Integer num) {
        int i = 1000000000;
        if (num != 0)
            while (num % i == num) {
                i /= 10;
            }
        else return "ноль";
        int countZero = (String.valueOf(i)).length();
        iterator = (countZero - 1) / 3;
        return init(num, countZero);
    }
}