package com.app.bdink.external.coolsms.dto.request;


import java.util.Random;

public record SmsSendRequest(
        String phone
) {
    public String generateRandomCode(){
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000);
        return ""+randomCode;
    }
}
