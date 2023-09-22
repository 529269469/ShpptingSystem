package com.fhxh.shpptingsystem.utils;


import static com.fhxh.shpptingsystem.utils.ByteUtil.BytegetBit8Reverse;
import static com.fhxh.shpptingsystem.utils.ByteUtil.INTBit16Reverse;
import static com.fhxh.shpptingsystem.utils.ByteUtil.bitStringToByte;

public class CrcUtilsReverse {

    public static byte[] CRC16_Ccitt_Reverse(byte[] bytes,int count) {
        int crc = 0x0000; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < count; index++) {
            String ReverseInputStr = BytegetBit8Reverse(bytes[index]);
            byte[] bb = {0x00};
            bb[0]=bitStringToByte(ReverseInputStr);
            byte b = bb[0];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        String ReverseS = INTBit16Reverse(crc);
        String ReverseSH = ReverseS.substring(0,8);
        String ReverseSL = ReverseS.substring(8,16);
        byte[] bb = {0x00,0x00};
        bb[0] = bitStringToByte(ReverseSH);
        bb[1] = bitStringToByte(ReverseSL);
        return bb;
    }

    public static String CRC2ByteCusumStr(byte[] bytes,int count){
        int crc = 0;
        for (int i = 0; i < count; i++) {
            int t = bytes[i] & 0x000000FF;
            crc = crc + t;
        }
        return decimalfitHex((long) crc, 4);
    }


    /**
     * 把十进制数字转换成足位的十六进制字符串,并补全空位
     *
     * @param num
     * @param strLength 字符串的长度
     * @return
     */
    public static String decimalfitHex(long num, int strLength) {
        String hexStr = Long.toHexString(num).toUpperCase();
        StringBuilder stringBuilder = new StringBuilder(hexStr);
        while (stringBuilder.length() < strLength) {
            stringBuilder.insert(0, '0');
        }
        return stringBuilder.toString();
    }
}
