package com.tiger.socol.gu.utils;

import java.math.BigDecimal;

public class DoubleUtils {

    public static double toPrice(double price) {
        BigDecimal b = new BigDecimal(price);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

}
