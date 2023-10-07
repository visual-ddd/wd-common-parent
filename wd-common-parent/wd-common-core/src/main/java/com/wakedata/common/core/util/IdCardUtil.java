package com.wakedata.common.core.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 身份证号校验
 * @author wenyp
 * @date 2021/3/25 16:04
 */
public class IdCardUtil {
    /**
     * 省,直辖市代码表
     */
    public static Map<Integer,String> provinceAndCityMap = null;
    public static String provinceAndCityJson = "{11: '北京',12: '天津',13: '河北',14: '山西',15: '内蒙古',\n" +
            "21: '辽宁',22: '吉林',23: '黑龙江',\n" +
            "31: '上海',32: '江苏',33: '浙江',34: '安徽',35: '福建',36: '江西',37: '山东',\n" +
            "    41: '河南',42: '湖北',43: '湖南',44: '广东',45: '广西',46: '海南',\n" +
            "50: '重庆',51: '四川',52: '贵州',53: '云南',54: '西藏',\n" +
            "61: '陕西',62: '甘肃',63: '青海',64: '宁夏',65: '新疆',\n" +
            "71: '台湾',81: '香港',82: '澳门',91: '国外'}";

    /**
     * 每位加权因子
     */
    public static List<Integer> powers = Arrays.asList(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);

    /**
     * 第18位校检码
     */
    public static List<String> parityBit = Arrays.asList("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");


    static {
        provinceAndCityMap = JSONObject.parseObject(provinceAndCityJson,Map.class);
    }

    // 地址码正则
    public static String addressCodeRegex = "^[1-9]\\d{5}$";

    // 日期码正则
    public static String birthDayCodeRegex = "^[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$";

    // 身份证号正则
    public static String idCardNoRegex = "^\\d{15}|(\\d{17}(\\d|x|X))$";

    // 15位身份证号码正则
    public static String idCardNo15Regex = "^[1-9]\\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\\d{3}$";
    // 18位身份证号码正则
    public static String idCardNo18Regex = "^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\\d{3}(\\d|x|X)$";

    /**
     * 校验地址码
     * @param addressCodeStr
     * @return
     */
    public static boolean checkAddressCode(String addressCodeStr){
        if (StringUtils.isEmpty(addressCodeStr)){
            return false;
        }
        boolean matches = addressCodeStr.matches(addressCodeRegex);
        if (!matches) return false;
        String addressCode = addressCodeStr.substring(0, 2);
        if (provinceAndCityMap.get(Integer.valueOf(addressCode)) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验日期码
     * @param birthDayCodeStr
     * @return
     */
    public static boolean checkBirthDayCode(String birthDayCodeStr){
        if (StringUtils.isEmpty(birthDayCodeStr)){
            return false;
        }
        boolean matches = birthDayCodeStr.matches(birthDayCodeRegex);
        if (!matches){
            return false;
        }
        int yyyy = Integer.valueOf(birthDayCodeStr.substring(0, 4), 10);
        int mm = Integer.valueOf(birthDayCodeStr.substring(4, 6), 10);
        int dd = Integer.valueOf(birthDayCodeStr.substring(6), 10);
        LocalDate birthDay = LocalDate.of(yyyy, mm - 1, dd);
        //生日不能大于当前日期
        if (birthDay.isAfter(LocalDate.now())){
            return false;
        }
        return true;
    }

    /**
     * 计算校检码
     * @param idCardNoStr
     * @return
     */
    public static String getParityBit(String idCardNoStr){
        if (StringUtils.isEmpty(idCardNoStr)){
            return null;
        }
        String idCardNo = idCardNoStr.substring(0, 17);
        // 加权
        int power = 0;
        for (int i = 0; i < 17; i++) {
            power += Integer.parseInt(String.valueOf(idCardNo.charAt(i)), 10) * powers.get(i);
        }
        // 取模
        int mod = power % 11;
        return parityBit.get(mod);
    }

    /**
     * 验证校检码
     * @param idCardNoStr
     * @return
     */
    public static boolean checkParityBit(String idCardNoStr){
        if (StringUtils.isEmpty(idCardNoStr)){
            return false;
        }
        char parityBit = idCardNoStr.charAt(17);
        // 获取校验码
        String getParityBit = getParityBit(idCardNoStr);
        if ( getParityBit.equalsIgnoreCase(String.valueOf(parityBit))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验15位或18位的身份证号码
     * @param idCardNoStr
     * @return
     */
    public static boolean checkIdCardNo(String idCardNoStr){
        if (StringUtils.isEmpty(idCardNoStr)){
            return false;
        }
        // 15位和18位身份证号码的基本校验
        boolean matches = idCardNoStr.matches(idCardNoRegex);
        if (!matches){
            return false;
        }
        //判断长度为15位或18位
        int length = idCardNoStr.length();
        if (length == 15) {
            return check15IdCardNo(idCardNoStr);
        } else if (length == 18) {
            return check18IdCardNo(idCardNoStr);
        } else {
            return false;
        }
    }

    /**
     * 校验15位的身份证号码
     * @param idCardNoStr
     * @return
     */
    public static boolean check15IdCardNo(String idCardNoStr){
        if (StringUtils.isEmpty(idCardNoStr)){
            return false;
        }
        boolean matches = idCardNoStr.matches(idCardNo15Regex);
        if (!matches){
            return false;
        }
        String addressCode = idCardNoStr.substring(0, 6);
        boolean checkAddressCode = checkAddressCode(addressCode);
        if (!checkAddressCode){
            return false;
        }
        String birDayCode = "19" + idCardNoStr.substring(6, 12);
        //校验日期码
        return checkBirthDayCode(birDayCode);
    }

    /**
     * 校验18位的身份证号码
     * @param idCardNoStr
     * @return
     */
    public static boolean check18IdCardNo(String idCardNoStr){
        if (StringUtils.isEmpty(idCardNoStr)){
            return false;
        }
        boolean matches = idCardNoStr.matches(idCardNo18Regex);
        if (!matches){
            return false;
        }
        //校验地址码
        String addressCode = idCardNoStr.substring(0, 6);
        boolean checkAddressCode = checkAddressCode(addressCode);
        if (!checkAddressCode){
            return false;
        }
        //校验日期码
        String birDayCode = idCardNoStr.substring(6, 14);
        boolean checkBirthDay = checkBirthDayCode(birDayCode);
        if (!checkBirthDay){
            return false;
        }
        //验证校检码
        return checkParityBit(idCardNoStr);
    }

    public static void main(String[] args) {
        String idCard = "360731199811078235";
        System.out.println(checkIdCardNo(idCard));
    }

}
