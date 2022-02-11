package com.ljw.jmeter.plugin.dubbo.common;

import com.google.common.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 林杰炜 linjw
 *  class类工具
 * @Description class类工具
 * @date 2018/12/14 17:23
 */
public class ClassUtils {
    private static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    private static final String TYPE_NAME_PREFIX = "class ";

    public static String getClassName(Type type) {
        if (type == null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    @SuppressWarnings("rawtypes")
    public static String[] getMethodParamType(String interfaceName,
                                              String methodName) {
        try {
            // 创建类
            Class<?> class1 = Class.forName(interfaceName);
            // 获取所有的公共的方法
            Method[] methods = class1.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] paramClassList = method.getParameterTypes();
                    String[] paramTypeList = new String[paramClassList.length];
                    int i = 0;
                    for (Class className : paramClassList) {
                        paramTypeList[i] = className.getName();
                        i++;
                    }
                    return paramTypeList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean isBlank(String paramValue) {
        return StringUtils.isBlank(paramValue) || "null".equalsIgnoreCase(paramValue);
    }

    @SuppressWarnings("serial")
    public static void parseParameter(List<String> paramterTypeList,
                                      List<Object> parameterValuesList, MethodArgument arg) {
        try {
            String className = arg.getParamType();
            if ("int".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.INT_DEFAULT : Integer.parseInt(arg.getParamValue()));
            } else if ("int[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.INT_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<int[]>() {
                }.getType()));
            } else if ("double".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.DOUBLE_DEFAULT : Double.parseDouble(arg.getParamValue()));
            } else if ("double[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.DOUBLE_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<double[]>() {
                }.getType()));
            } else if ("short".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.SHORT_DEFAULT : Short.parseShort(arg.getParamValue()));
            } else if ("short[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.SHORT_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<short[]>() {
                }.getType()));
            } else if ("float".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.FLOAT_DEFAULT : Float.parseFloat(arg.getParamValue()));
            } else if ("float[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.FLOAT_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<float[]>() {
                }.getType()));
            } else if ("long".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.LONG_DEFAULT : Long.parseLong(arg.getParamValue()));
            } else if ("long[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.LONG_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<long[]>() {
                }.getType()));
            } else if ("byte".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.BYTE_DEFAULT : Byte.parseByte(arg.getParamValue()));
            } else if ("byte[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.BYTE_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<byte[]>() {
                }.getType()));
            } else if ("boolean".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.BOOLEAN_DEFAULT : Boolean.parseBoolean(arg.getParamValue()));
            } else if ("boolean[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.BOOLEAN_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<boolean[]>() {
                }.getType()));
            } else if ("char".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.CHAR_DEFAULT : arg.getParamValue().charAt(0));
            } else if ("char[]".equals(className)) {
                paramterTypeList.add(arg.getParamType());
                parameterValuesList.add(isBlank(arg.getParamValue()) ? Constants.CHAT_ARRAY_DEFAULT : JsonUtils.readValue(arg.getParamValue(), new TypeToken<char[]>() {
                }.getType()));
            } else if ("java.lang.String".equals(className)
                    || "String".equals(className)
                    || "string".equals(className)) {
                paramterTypeList.add("java.lang.String");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : String.valueOf(arg.getParamValue()));
            } else if ("java.lang.String[]".equals(className)
                    || "String[]".equals(className)
                    || "string[]".equals(className)) {
                paramterTypeList.add("java.lang.String[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<String[]>() {
                }.getType()));
            } else if ("java.lang.Integer".equals(className)
                    || "Integer".equals(className)
                    || "integer".equals(className)) {
                paramterTypeList.add("java.lang.Integer");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : Integer.valueOf(arg.getParamValue()));
            } else if ("java.lang.Integer[]".equals(className)
                    || "Integer[]".equals(className)
                    || "integer[]".equals(className)) {
                paramterTypeList.add("java.lang.Integer[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<Integer[]>() {
                }.getType()));
            } else if ("java.lang.Double".equals(className)
                    || "Double".equals(className)) {
                paramterTypeList.add("java.lang.Double");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : Double.valueOf(arg.getParamValue()));
            } else if ("java.lang.Double[]".equals(className)
                    || "Double[]".equals(className)) {
                paramterTypeList.add("java.lang.Double[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<Double[]>() {
                }.getType()));
            } else if ("java.lang.Short".equals(className)
                    || "Short".equals(className)) {
                paramterTypeList.add("java.lang.Short");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : Short.valueOf(arg.getParamValue()));
            } else if ("java.lang.Short[]".equals(className)
                    || "Short[]".equals(className)) {
                paramterTypeList.add("java.lang.Short[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<Short[]>() {
                }.getType()));
            } else if ("java.lang.Long".equals(className)
                    || "Long".equals(className)) {
                paramterTypeList.add("java.lang.Long");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : Long.valueOf(arg.getParamValue()));
            } else if ("java.lang.Long[]".equals(className)
                    || "Long[]".equals(className)) {
                paramterTypeList.add("java.lang.Long[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<Long[]>() {
                }.getType()));
            } else if ("java.lang.Float".equals(className)
                    || "Float".equals(className)) {
                paramterTypeList.add("java.lang.Float");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : Float.valueOf(arg.getParamValue()));
            } else if ("java.lang.Float[]".equals(className)
                    || "Float[]".equals(className)) {
                paramterTypeList.add("java.lang.Float[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<Float[]>() {
                }.getType()));
            } else if ("java.lang.Byte".equals(className)
                    || "Byte".equals(className)) {
                paramterTypeList.add("java.lang.Byte");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : Byte.valueOf(arg.getParamValue()));
            } else if ("java.lang.Byte[]".equals(className)
                    || "Byte[]".equals(className)) {
                paramterTypeList.add("java.lang.Byte[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<Byte[]>() {
                }.getType()));
            } else if ("java.lang.Boolean".equals(className)
                    || "Boolean".equals(className)) {
                paramterTypeList.add("java.lang.Boolean");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : Boolean.valueOf(arg.getParamValue()));
            } else if ("java.lang.Boolean[]".equals(className)
                    || "Boolean[]".equals(className)) {
                paramterTypeList.add("java.lang.Boolean[]");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), new TypeToken<Boolean[]>() {
                }.getType()));
            } else if ("java.sql.Timestamp".equals(className)) {
                paramterTypeList.add("java.sql.Timestamp");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : new Timestamp(Long.parseLong(arg.getParamValue())));
            } else if ("java.util.Date".equals(className)
                    || "Date".equals(className)) {
                paramterTypeList.add("java.util.Date");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : new Date(Long.parseLong(arg.getParamValue())));
            } else if ("java.time.LocalDate".equals(className) || "LocalDate".equals(className)) {
                paramterTypeList.add("java.time.LocalDate");
                DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                parameterValuesList.add(isBlank(arg.getParamValue()) ? null : LocalDate.parse(arg.getParamValue().replaceAll("\"", ""), ymd));
            } else if (className.contains("/")) {
                String[] clas = className.split("/");
                String type = clas[0];
                String cla = clas[1];
                Class clazz = Class.forName(cla);
                List<?> list = null;
                if (!isBlank(arg.getParamValue())) {
                    list = JsonUtils.readValueToList(arg.getParamValue(), clazz);
                }
                paramterTypeList.add(type);
                parameterValuesList.add(list == null ? null : list.toArray());
            } else {
                if (className.endsWith("[]")) {
                    List<?> list = null;
                    if (!isBlank(arg.getParamValue())) {
                        list = JsonUtils.fromJson(arg.getParamValue(), new TypeToken<List<?>>() {
                        }.getType());
                    }
                    paramterTypeList.add(arg.getParamType());
                    parameterValuesList.add(list == null ? null : list.toArray());
                } else {
                    try {
                        Class<?> clazz = Class.forName(className);
                        paramterTypeList.add(arg.getParamType());
                        if (clazz.isEnum()) {
                            parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.fromJson(arg.getParamValue(), clazz));
                        } else {
                            parameterValuesList.add(isBlank(arg.getParamValue()) ? null : JsonUtils.readValue(arg.getParamValue(), clazz));
                        }
                    } catch (ClassNotFoundException e) {
                        //不是jdk或者lib下的类，使用通用map格式反序列化值
                        paramterTypeList.add(arg.getParamType());
                        parameterValuesList.add(JsonUtils.readValue(arg.getParamValue(), new TypeToken<Map<String, Object>>() {
                        }.getType()));
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("无效参数 => [ParamType=" + arg.getParamType() + ",ParamValue=" + arg.getParamValue() + "]", e);
        }
    }

}
