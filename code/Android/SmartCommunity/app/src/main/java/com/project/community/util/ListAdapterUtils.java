package com.project.community.util;

import java.util.List;

/**
 * author：JiaXing
 * e-mail：JiaXingGoo@gmail.com
 */
public class ListAdapterUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isSize(List list,int size){
        return !isEmpty(list)&&list.size()>=size;
    }

    public static int size(List list){
        return isEmpty(list)?0:list.size();
    }

    public static String toStr(String space,List<String> list){
        StringBuffer sb = new StringBuffer();
        if (!ListAdapterUtils.isEmpty(list)) {
            for (String str :
                    list) {
                sb.append(str).append(space);
            }
        }
        return sb.toString().isEmpty()?"":sb.substring(0,sb.length()-space.length());
    }
}
