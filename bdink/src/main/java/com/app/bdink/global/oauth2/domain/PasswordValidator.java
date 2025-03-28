package com.app.bdink.global.oauth2.domain;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.template.RspTemplate;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@NoArgsConstructor
public class PasswordValidator {

    public static int validatePassword(String password) {

        if (password.isBlank()){
            return -400;
        }
        if (password.length() < 8 || password.length() > 16) {
            return -401; // "비밀번호는 8자 이상 16자 이하여야 합니다."
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return -402; // "비밀번호에 최소 1개의 대문자가 포함되어야 합니다."
        }
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            return -403; // "비밀번호에 최소 1개의 소문자가 포함되어야 합니다."
        }
        if (!Pattern.compile("\\d").matcher(password).find()) {
            return -404; //"비밀번호에 최소 1개의 숫자가 포함되어야 합니다."
        }
        if (!Pattern.compile("[@$!%*?&]").matcher(password).find()) {
            return -405; //"비밀번호에 최소 1개의 특수문자가 포함되어야 합니다."
        }
        return 200;
    }
}
