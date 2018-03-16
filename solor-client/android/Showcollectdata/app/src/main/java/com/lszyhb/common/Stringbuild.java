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

    public static String toHex(String str) {
        String hexString="0123456789ABCDEF";
        byte[] bytes=str.getBytes();
        StringBuilder hex=new StringBuilder(bytes.length * 2);
        for(int i=0;i<bytes.length;i++) {
            hex.append(hexString.charAt((bytes[i] & 0xf0) >> 4));  // 作用同 n / 16
            hex.append(hexString.charAt((bytes[i] & 0x0f) >> 0));  // 作用同 n
            hex.append(' ');  //中间用空格隔开
        }
        return hex.toString();
    }


    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式
     * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     * @param src String
     * @return byte[]
     **/
    public static byte[] HexString2Bytes(String src)  {
        byte[] ret = new byte[8];
        byte[] tmp = src.getBytes();
        for(int i=0; i<8; i++)  {
            ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节；
     * 如："EF"--> 0xEF
     * @param src0 byte
     * @param src1 byte
     * @return byte
     **/
    public static byte uniteBytes(byte src0, byte src1)  {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte)(_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte)(_b0 ^ _b1);
        return ret;
    }


    public static int[] BinstrToIntArray(String binStr)
    {
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
}
