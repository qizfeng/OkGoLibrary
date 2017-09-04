package com.library.okgo.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qizfeng on 17/8/8.
 * 常用正则验证工具类
 */

public class ValidateUtil {
    /**
     * 验证用户姓名且字符长度为1~50也就是2~25个汉字
     * <p>
     * param userName
     * return
     */
    public static boolean isUserName(String userName) {
        Pattern pattern = Pattern.compile("^([\u4E00-\u9FA5]|[\uF900-\uFA2D]|[\u258C]|[\u2022]|[\u2E80-\uFE4F])+$");
        Matcher mc = pattern.matcher(userName);
        return mc.matches();
    }

    /**
     * 邮箱验证
     * <p>
     * param mail
     * return
     */
    public static boolean isValidEmail(String mail) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher mc = pattern.matcher(mail);
        return mc.matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通） 147
         * 177 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 判断输入是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断手机号是否合法
     */
    public static boolean isPhone(String mobiles) {
        Pattern p =
                Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
//        Pattern p = Pattern.compile("1[0-9]{10}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证联系方式
     * <p>
     * param str 座机或手机
     * return
     */
    public static boolean isMobileOrPhone(String str) {
        String regex = "^((([0\\+]\\d{2,3}-)|(0\\d{2,3})-))(\\d{7,8})(-(\\d{3,}))?$|^1[0-9]{10}$";
        return match(regex, str);
    }

    /**
     * 验证金额有效性
     * <p>
     * param price
     * return
     */
    public static boolean isPrice(String price) {
        String regex = "^([1-9][0-9]{0,7})(\\.\\d{1,2})?$";
        return match(regex, price);
    }

    /**
     * 判断是否含有中文
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为纯英文
     */
    public static boolean isLetter(String str) {
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否为纯中文
     */
    public static boolean isChiness(String str) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FFF]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断必须是否为密码类型
     */
    public static boolean isPassword(String str) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z.*]{6,16}$");//6-16位密码
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isPwd(String str) {
        if (str.length() >= 6 && str.length() <= 16) {
            return true;
        } else
            return false;
    }


    /**
     * 判断必须是否为警员号
     */
    public static boolean isPoliceNumberAndLength(String str) {
        Pattern p = Pattern.compile("[A-Za-z0-9]{6,12}");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断邮件email是否正确格式
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        // Pattern p = Pattern.compile("\\w+(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 用于判断用户输入字符长度6到16位
     * <p>
     * param str
     * return
     */
    public static boolean isLength(String str) {
        Pattern pattern = Pattern.compile(".{6,16}");
        return pattern.matcher(str).matches();
    }

    /**
     * 用于判断用户输入住址字符长度1到250位
     * <p>
     * param str
     * return
     */
    public static boolean isAddressLength(String str) {
        Pattern pattern = Pattern.compile(".{1,250}");
        return pattern.matcher(str).matches();
    }

    /**
     * 用于判断用户输入描述字符长度1到50位
     * <p>
     * param str
     * return
     */
    public static boolean isRemarksLength(String str) {
        Pattern pattern = Pattern.compile(".{1,50}");
        return pattern.matcher(str).matches();
    }

    /**
     * 用于判断用户真名长度
     * <p>
     * param str
     * return
     */
    public static boolean isNameLength(String str) {
        Pattern pattern = Pattern.compile(".{2,25}");
        return pattern.matcher(str).matches();
    }

    /**
     * 用于判断用户输入字符首字母是否英文
     * <p>
     * param str
     * return
     */
    public static boolean isEnglish(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]+");
        return pattern.matcher(str).matches();
    }

    /**
     * 用于判断银行卡位数13到19位
     * <p>
     * param str
     * return
     */
    public static boolean isbank(String str) {
        Pattern pattern = Pattern.compile(".{13,19}");
        return pattern.matcher(str).matches();
    }

    /**
     * 中文占两个字符，英文占一个
     */
    public static int String_length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 过滤特殊字符
     */
    public static String stringFilter(String str) {
        String regEx = "[`~!#$%^&*()+=|{}':;',\\[\\].<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 验证输入密码长度 (6-20位除空格回车tab外的字符)
     */
    public static boolean isPasswLength(String str) {
        String regex = "^\\S{6,20}$";
        return match(regex, str);
    }

    /**
     * param regex 正则表达式字符串
     * param str   要匹配的字符串
     * return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 功能：判断字符串是否为日期格式
     * <p>
     * return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串转date
     * <p>
     * param strTime
     * param formatType
     * return
     * throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType) {
        Date date = new Date();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            date = formatter.parse(strTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 验证身份证号格式是否符合规则
     * <p>
     * param text 身份证号
     * return
     */
    public static boolean personIdValidation(String text) {
        String regx = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)";
        return text.matches(regx);
    }

    /**
     * 精确验证身份证号
     *
     * @param text
     * @return
     */
    public static boolean personIdValidation2(String text) {
        if (text.length() != 18)
            return false;
        // 正则表达式判断基本 身份证号是否满足格式
        //如果通过该验证，说明身份证格式正确，但准确性还需计算
        if (!personIdValidation(text)) {
            return false;
        }
        //** 开始进行校验 *//

        //将前17位加权因子保存在数组里
        String[] idCardWiArray = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};

        //这是除以11后，可能产生的11位余数、验证码，也保存成数组
        String[] idCardYArray = {"1", "0", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
        //用来保存前17位各自乖以加权因子后的总和
        int idCardWiSum = 0;
        for (int i = 0; i < 17; i++) {
            int subStrIndex = Integer.parseInt(text.substring(i, 1 + i));
            int idCardWiIndex = Integer.parseInt(idCardWiArray[i]);
            idCardWiSum += subStrIndex * idCardWiIndex;
        }

        //计算出校验码所在数组的位置
        int idCardMod = idCardWiSum % 11;
        //得到最后一位身份证号码
        String idCardLast = text.substring(17, 18);
        //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
        if (idCardMod == 2) {
            if (!"X".equals(idCardLast) || "x".equals(idCardLast)) {
                return false;
            }
        } else {
            //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
            if (!idCardLast.equals(idCardYArray[idCardMod])) {
                return false;
            }
        }
        return true;
    }


    public static String getUserAgeByCardId(String ids) {
        String birthday = getUserBrithdayByCardId(ids);
        Date birthDate = stringToDate(birthday, "yyyy-MM-dd");
        Integer age;
        Integer year;
        Integer month;
        Integer day;
        Calendar birthdayCalender = Calendar.getInstance();
        birthdayCalender.setTime(birthDate);
        Calendar todayCalender = Calendar.getInstance();
        todayCalender.setTime(new Date());
        day = todayCalender.get(Calendar.DAY_OF_MONTH)
                - birthdayCalender.get(Calendar.DAY_OF_MONTH);
        month = todayCalender.get(Calendar.MONTH)
                - birthdayCalender.get(Calendar.MONTH);
        year = todayCalender.get(Calendar.YEAR)
                - birthdayCalender.get(Calendar.YEAR);
        // 按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
        if (day < 0) {
            month -= 1;
            todayCalender.add(Calendar.MONTH, -1);// 得到上一个月，用来得到上个月的天数。
            day = day + todayCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (month < 0) {
            month = (month + 12) % 12;
            year--;
        }
        if (year > 0) {
            age = year;
        } else if (month > 0) {
            age = month;
        } else {
            age = day;
        }
        return age + "";
    }


    /**
     * 功能：根据身份证号获取用户生日
     * <p>
     * return
     */
    public static String getUserBrithdayByCardId(String ids) {
        String brithday = "";
        if (ids.length() == 18) {
            brithday = ids.substring(6, 14); // 18位
            String years = brithday.substring(0, 4);
            String moths = brithday.substring(4, 6);
            String days = brithday.substring(6, 8);
            brithday = years + "-" + moths + "-" + days;

        } else if (ids.length() == 15) {
            brithday = ids.substring(6, 12); // 15位
            String years = brithday.substring(0, 2);
            String moths = brithday.substring(2, 4);
            String days = brithday.substring(4, 6);
            brithday = "19" + years + "-" + moths + "-" + days;
        }
        return brithday;
    }

    /**
     * 功能：根据身份证号获取用户性别
     * <p>
     * return
     */
    public static String getUserSexByCardId(String ids) {
        String sexshow = "";
        if (ids.length() == 18) {
            String sexstring = ids.trim().substring(ids.length() - 2, ids.length() - 1); // 取得身份证倒数第二位
            int sexnum = Integer.parseInt(sexstring); // 转换成数字
            if (sexnum % 2 != 0) {
                sexshow = "男";
            } else {
                sexshow = "女";
            }
        } else if (ids.length() == 15) {
            String sexstring = ids.trim().substring(ids.length() - 1, ids.length()); // 取得身份证最后一位
            int sexnum = Integer.parseInt(sexstring); // 转换成数字
            if (sexnum % 2 != 0) {
                sexshow = "男";
            } else {
                sexshow = "女";
            }
        }

        return sexshow;
    }
}
