package com.library.okgo.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/8/9.
 * 数据对象转json
 */

public class SimpleJson {
    private static final String TAG = SimpleJson.class.getSimpleName();

    private SimpleJson() {
    }

    public static String toJson(Object obj) {
        String json = "";
        if (obj != null) {
            StringBuffer sb = new StringBuffer();
            if (obj instanceof List) {
                sb.append("[");
                List<?> list = (List<?>) obj;
                for (int i = 0; i < list.size(); i++) {
                    parseObjToJson(sb, list.get(i));
                    if (i < list.size() - 1) {
                        sb.append(",");
                    }
                }
            } else {
                parseObjToJson(sb, obj);
            }
            json = sb.toString();
        }
        return json;
    }

    private static void parseObjToJson(StringBuffer sb, Object obj) {
        if (sb != null && obj != null) {
            sb.append("{");
            List<Field> fields = new ArrayList<>();
            getAllFields(obj.getClass(), fields);
            if (!fields.isEmpty()) {
                for (int i = 0; i < fields.size(); i++) {
                    Method method = null;
                    Field field = fields.get(i);
                    Object fieldValue = null;
                    String fieldName = field.getName();
                    String methodName = "";
                    if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        if (!TextUtils.isEmpty(fieldName) && fieldName.startsWith("is")) {
                            methodName = fieldName;
                        } else {
                            methodName = "is" + ((char) (fieldName.charAt(0) - 32) + fieldName.substring(1));
                        }
                    } else {
                        methodName = "get" + ((char) (fieldName.charAt(0) - 32) + fieldName.substring(1));
                    }
                    try {
                        method = obj.getClass().getMethod(methodName);
                    } catch (NoSuchMethodException e) {
                    }
                    if (method != null) {
                        try {
                            fieldValue = method.invoke(obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fieldValue != null) {
                        sb.append("\"");
                        sb.append(fieldName);
                        sb.append("\":");
                        if (fieldValue instanceof Integer
                                || fieldValue instanceof Double ||
                                fieldValue instanceof Long ||
                                fieldValue instanceof Boolean) {
                            sb.append(fieldValue.toString());
                        } else if (fieldValue instanceof String) {
                            sb.append("\"");
                            sb.append(fieldValue.toString());
                            sb.append("\"");
                        } else if (fieldValue instanceof List) {
                            parseListToJson(sb, fieldValue);
                        } else {
                            parseObjToJson(sb, fieldValue);
                        }
                        if (i != (fields.size() - 1)) {//排除最后一个字段加逗号
                            sb.append(",");
                        }
                    }
                }
            }
            sb.append("}");
        }
    }

    private static void parseListToJson(StringBuffer sb, Object fieldValue) {
        if (sb != null && fieldValue != null) {
            List list = (List) fieldValue;
            sb.append("[");
            for (int i = 0; i < list.size(); i++) {
                parseObjToJson(sb, list.get(i));
                if (i != (list.size() - 1)) {
                    sb.append(",");
                }
            }
            sb.append("]");
        }
    }

    private static void getAllFields(Class<?> clazz, List<Field> fields) {
        if (clazz == null) {
            return;
        }
        if (fields == null) {
            fields = new ArrayList<>();
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!Modifier.isFinal(field.getModifiers())) {
                fields.add(field);
            }
        }
    }
}
