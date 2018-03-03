package com.lszyhb.common;

/**
 * Created by kkk8199 on 2/1/18.
 */

public class Stringbuild {

    public static String join(String join, CharSequence[] strAry) {
        StringBuffer sb=new StringBuffer();
        for (int i=0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }

        return new String(sb);
    }
}
