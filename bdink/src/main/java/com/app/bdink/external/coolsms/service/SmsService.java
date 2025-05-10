package com.app.bdink.external.coolsms.service;

import com.app.bdink.external.coolsms.controller.dto.PhoneRequest;
import com.app.bdink.external.coolsms.entity.PhoneVerify;
import com.app.bdink.external.coolsms.repository.PhoneRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final PhoneRepository phoneRepository;

    public boolean verifyPhone(PhoneRequest phoneRequest) {
        Optional<PhoneVerify> verifyOpt = phoneRepository.findById(
                UUID.fromString(
                        phoneRequest.transactionId()
                )
        );

        if (verifyOpt.isEmpty()){
            throw new CustomException(Error.VERIFY_EXPIRED_EXCEPTION, Error.VERIFY_EXPIRED_EXCEPTION.getMessage());
        }

        PhoneVerify verify = verifyOpt.get();

        if (verify.isExpired()){
            throw new CustomException(Error.VERIFY_EXPIRED_EXCEPTION, Error.VERIFY_EXPIRED_EXCEPTION.getMessage());
        }
        
        // 402에러 분기처리 추가
        if(!verify.getVerifyCode().equals(phoneRequest.code())){
            throw new CustomException(Error.INVALID_CODE_EXCEPTION, Error.INVALID_CODE_EXCEPTION.getMessage());
        }

        return verify.getVerifyCode().equals(phoneRequest.code());
    }

    @Transactional
    public PhoneVerify createVerify(String code, String phone){

        return phoneRepository.save(
                PhoneVerify.builder()
                        .verifyCode(code)
                        .phone(phone)
                        .build()
        );
    }

    @Transactional
    public void deleteVerify(UUID uuid){
        phoneRepository.deleteById(uuid);
    }
}
