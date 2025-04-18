package com.app.bdink.external.kollus.service;

import com.app.bdink.external.kollus.dto.request.CallbackRequest;
import com.app.bdink.external.kollus.dto.request.KollusRequest;
import com.app.bdink.external.kollus.dto.response.KollusResponse;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KollusService {

    private final KollusMediaLinkRepository kollusMediaLinkRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void uploadCallbackService(CallbackRequest.uploadRequestDTO uploadRequestDTO){
        log.info(String.valueOf(uploadRequestDTO));
    }

    @Transactional
    public void CreateKollusUserKey(KollusRequest.userKeyDTO userKeyDTO, @Value("${kollus.API_ACCESS_TOKEN}") String apiAccessToken) throws IOException {

        String ACCESS_TOKEN = apiAccessToken;
        String user_agent = userKeyDTO.getUser_agent();
        String client_user_id = userKeyDTO.getClient_user_id();
        String remote_addr = userKeyDTO.getRemote_addr();
        Long user_key_timeout = userKeyDTO.getUser_key_timeout();

//        Optional<Member> memberOpt = memberRepository.findByKakaoId(client_user_id); //Todo: kollusClientUserId로 유저 찾아서 넣으면 될 듯.

        String payload = String.format(
                "user_agent=%s&client_user_id=%s&remote_addr=%s&user_key_timeout=%d",
                user_agent, client_user_id, remote_addr, user_key_timeout
        );

        String kollusUrl = String.format(
          "https://c-api-kr.kollus.com/api/media/user-key?access_token=%s",
          ACCESS_TOKEN
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
            KollusResponse.KollusUserKeyResponse kollusUserKeyResponse = objectMapper.readValue(responseBody, KollusResponse.KollusUserKeyResponse.class);
            String userKey = kollusUserKeyResponse.getUser_key();

            //Todo: UserKey save 로직
        }
    }

    @Transactional
    public void deleteCallbackService(CallbackRequest.deleteRequestDTO deleteRequestDTO){
        log.info(String.valueOf(deleteRequestDTO));
    }

    @Transactional
    public void playCallbackService(CallbackRequest.playRequestDTO playRequestDTO){
        log.info(String.valueOf(playRequestDTO));
    }
}
