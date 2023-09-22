package com.fhxh.shpptingsystem.utils;

public class CalculateCheckDigit {
    /** 输入十六进制，输出两位校验位 */
    public static void open(String sHex) {
//        String sHex = "01 AA 55 82 5F 05";// 输入十六进制
        sHex = sHex.replace(" ", "");// 去掉中间空格
        String result = makeCheckSum(sHex);// 计算并获取校验位
        System.out.println(result);// 输入两位校验位 结果是B0
    }

    /** 计算校验位 ，返回十六进制校验位 */
    public static String makeCheckSum(String data) {
        int dSum = 0;
        int length = data.length();
        int index = 0;
        // 遍历十六进制，并计算总和
        while (index < length) {
            String s = data.substring(index, index + 2); // 截取2位字符
            dSum += Integer.parseInt(s, 16); // 十六进制转成十进制 , 并计算十进制的总和
            index = index + 2;
        }

        int mod = dSum % 256; // 用256取余，十六进制最大是FF，FF的十进制是255
        String checkSumHex = Integer.toHexString(mod); // 余数转成十六进制
        length = checkSumHex.length();
        if (length < 2) {
            checkSumHex = "0" + checkSumHex;  // 校验位不足两位的，在前面补0
        }
        return checkSumHex;
    }
}
