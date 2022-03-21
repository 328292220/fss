package com.zx.fss.util;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.cglib.beans.BeanMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConvertOp {

    //可能的时间格式
    private static final String[] format = {" HH:mm:ss", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd"};
    //时间匹配正则表达式
    private static final String[] dateRegex = {"^[\\d]{4}+[-]+[\\d]{1,2}+[-]+[\\d]{1,2}$", "\\d{8}", "^[\\d]{4}+[/]+[\\d]{1,2}+[/]+[\\d]{1,2}$"};
    //时间前后分割
    private static String space = "[A-Za-z\\s.]+"; //字母，空格，小数点


    public static <T> HashMap<String, Object> convertBeanToMap(T bean) {
        HashMap<String, Object> map = new HashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    public static <T> T convertMapToBean(Map<Object, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        for (Object beankey :beanMap.keySet()) {
            for (Object resultkey:map.keySet()){
                if(ConvertOp.convert2String(beankey).toLowerCase().equals(ConvertOp.convert2String(resultkey).toLowerCase())){
                    beanMap.put(beankey,map.get(resultkey));
                    continue;
                }
            }
        }
        return bean;
    }

    public static <T> T convertLinkHashMapToBean(LinkedHashMap<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        for (Object beankey :beanMap.keySet()) {
            for (Object resultkey:map.keySet()){
                if(ConvertOp.convert2String(beankey).toLowerCase().equals(ConvertOp.convert2String(resultkey).toLowerCase())){
                    beanMap.put(beankey,map.get(resultkey));
                    continue;
                }
            }
        }
        return bean;
    }

    public static  <T1,T2> T2 convertSameStructObj(T1 fromBean,T2 toBean){
        Map fromBeanMap = convertBeanToMap(fromBean);
        return (T2)convertMapToBean(fromBeanMap,toBean);
    }

    public static boolean isNull(Object obj)    {
        if(obj==null){
            return true;
        }else{
            return false;
        }
    }

    public static Integer convert2Int(Object obj){
        Integer result;
        if(!isNull(obj)){
            try{
                result = Integer.parseInt(String.valueOf(obj));
            }catch (Exception e){
                result = 0;
            }
        }else{
            result = 0;
        }
        return result;
    }

    public static Double convert2Double(Object obj){
        Double result;
        if(!isNull(obj)){
            try{
                result = Double.parseDouble(String.valueOf(obj));
            }catch (Exception e){
                result = 0.0;
            }
        }else{
            result = 0.0;
        }
        return result;
    }

    public static BigDecimal convert2Decimal(Object obj){
        BigDecimal result;
        if(!isNull(obj)){
            try{
                result = new BigDecimal(String.valueOf(obj));
            }catch (Exception e){
                result = new BigDecimal(0);
            }
        }else{
            result = new BigDecimal(0);
        }
        return result;
    }

    public static BigDecimal convert2Decimal(Object obj,int scale){
        BigDecimal result;
        if(!isNull(obj)){
            try{
                result = new BigDecimal(String.valueOf(obj)).setScale(scale, RoundingMode.HALF_UP);
            }catch (Exception e){
                result = new BigDecimal(0);
            }
        }else{
            result = new BigDecimal(0);
        }
        return result;
    }

    public static Boolean convert2Boolean(Object obj){
        Boolean result;
        if(!isNull(obj)){
            try{
                result = Boolean.parseBoolean(String.valueOf(obj));
            }catch (Exception e){
                result = false;
            }
        }else{
            result = false;
        }
        return result;
    }

    public static Date convert2Date(Object obj){
        Date result = new Date(1753,1,1);;
        if(!isNull(obj)){
            try{
                String date = String.valueOf(obj);
                if (date != null && !date.matches("[A-Za-z.]+")) {
                    date = date.split("[A-Za-z.]+")[0].trim();
                    String pre = date.split("[\\s]+")[0];
                    if (pre.matches(dateRegex[0])) {
                        result = DateUtils.parseDate(date, format[1], format[1] + format[0]);
                    } else if (pre.matches(dateRegex[1])) {
                        result = DateUtils.parseDate(date, format[2], format[2] + format[0]);
                    } else if (pre.matches(dateRegex[2])) {
                        result = DateUtils.parseDate(date, format[3], format[3] + format[0]);
                    }
                }

            }catch (Exception e){
                result = new Date(1753,1,1);
            }
        }else{
            result = new Date(1753,1,1);
        }
        return result;
    }

    public static String convert2String(Object obj){
        String result;
        if(!isNull(obj)){
            try{
                result = String.valueOf(obj);
            }catch (Exception e){
                result = StringUtil.EMPTY;
            }
        }else{
            result = StringUtil.EMPTY;
        }
        return result;
    }
}
