package com.app.bdink.external.kollus.service;

import com.app.bdink.external.kollus.dto.request.CallbackRequest;
import com.app.bdink.external.kollus.dto.request.KollusRequest;
import com.app.bdink.external.kollus.dto.response.KollusResponse;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.model.net.CustomJavaNetException;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KollusService {

    private final KollusMediaLinkRepository kollusMediaLinkRepository;
    private final MemberRepository memberRepository;
    @Value("${kollus.API_ACCESS_TOKEN}")
    private String apiAccessToken;


    @Transactional
    public void uploadCallbackService(CallbackRequest.uploadRequestDTO uploadRequestDTO) {
        log.info(String.valueOf(uploadRequestDTO));
    }

    @Transactional
    public void CreateKollusUserKey(KollusRequest.userKeyDTO userKeyDTO) throws IOException {

        String API_ACCESS_TOKEN = apiAccessToken;
        String user_agent = userKeyDTO.getUser_agent();
        String client_user_id = userKeyDTO.getClient_user_id();
        String remote_addr = userKeyDTO.getRemote_addr();
        Long user_key_timeout = userKeyDTO.getUser_key_timeout();

        Optional<Member> memberOPT = memberRepository.findByKollusClientUserId(client_user_id);
        Member member = memberOPT.orElseThrow(() ->  new CustomException(com.app.bdink.global.exception.Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

        if(!member.getUserKey().equals(null)){
            throw new CustomJavaNetException(com.app.bdink.global.exception.Error.EXIST_USERKEY, Error.EXIST_USERKEY.getMessage());
        }
        String payload = String.format(
                "user_agent=%s&client_user_id=%s&remote_addr=%s&user_key_timeout=%d",
                user_agent, client_user_id, remote_addr, user_key_timeout
        );

        String kollusUrl = String.format(
                "https://c-api-kr.kollus.com/api/media/user-key?access_token=%s",
                API_ACCESS_TOKEN
        );

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(kollusUrl)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            KollusResponse kollusResponse = objectMapper.readValue(responseBody, KollusResponse.class);
            String userKey = kollusResponse.getData().getUser_key();

            //UserKey save 로직
            member.updateUserKey(userKey);
            memberRepository.save(member);
        }
    }

    @Transactional
    public void deleteCallbackService(CallbackRequest.deleteRequestDTO deleteRequestDTO) {
        log.info(String.valueOf(deleteRequestDTO));
    }

    @Transactional
    public void playCallbackService(CallbackRequest.playRequestDTO playRequestDTO) {
        log.info(String.valueOf(playRequestDTO));
    }
}
