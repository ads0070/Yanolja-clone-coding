package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    /**
     * Email 정규표현식
     */
    public static boolean isRegexEmail(String target) {
        String regex = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    /**
     * 휴대폰번호 정규식
     */
    public static boolean isRegexPhone(String target) {
        String regex = "\\d{3}\\d{3,4}\\d{4}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    /**
     * 비밀번호 정규식
     * 8~20자 영문+숫자+특수문자
     */
    public static boolean isRegexPassword(String target) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,20}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    /**
     * 닉네임 정규식
     * 2~8글자 한글/영문/숫자
     */
    public static boolean isRegexNickname(String target) {
        String regex = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,8}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
}

