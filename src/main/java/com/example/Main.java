package com.example;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        NumberConverter numberConverter = new NumberConverter();
        System.out.println(numberConverter.sumProp(921_611_001_012L, "М", "П"));
        System.out.println(numberConverter.sumProp(100000000001L, "Ж", "Т"));
    }

}