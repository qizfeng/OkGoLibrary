package com.project.community.util;

import android.text.TextUtils;

/**
 * Created by qizfeng on 17/7/13.
 * 字符串工具类
 */

public class StringUtils {
    /**
     * 半角转换为全角
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        if(TextUtils.isEmpty(input)){
            return "";

        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}
