package com.lsyf.pay.logger.common.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class StringUtil {
    public static String replace(String source,
                                 Map<String, Object> parameter,
                                 String prefix, String suffix,
                                 boolean enableSubstitutionInVariables) {
        //StrSubstitutor不是线程安全的类
        StringSubstitutor strSubstitutor = new StringSubstitutor(parameter, prefix, suffix);
        //是否在变量名称中进行替换
        strSubstitutor.setEnableSubstitutionInVariables(enableSubstitutionInVariables);
        return strSubstitutor.replace(source);
    }

    public static String frontWithZore(int number, int len) {
        String newString = String.format("%0" + len + "d", number);
        return newString;
    }
}
