package com.app.bdink.external.kollus.service;

import com.app.bdink.external.kollus.dto.KollusTokenDTO;
import com.app.bdink.external.kollus.dto.request.CallbackRequest;
import com.app.bdink.external.kollus.dto.request.KollusRequest;
import com.app.bdink.external.kollus.dto.response.KollusApiResponse;
import com.app.bdink.external.kollus.dto.response.KollusResponse;
import com.app.bdink.external.kollus.entity.KollusMedia;
import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.external.kollus.repository.KollusMediaRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.model.net.CustomJavaNetException;
import com.app.bdink.global.token.TokenProvider;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KollusService {

    private final KollusMediaLinkRepository kollusMediaLinkRepository;
    private final KollusMediaRepository kollusMediaRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    @Value("${kollus.API_ACCESS_TOKEN}")
    private String apiAccessToken;


    @Transactional
    public void uploadCallbackService(CallbackRequest.uploadRequestDTO uploadRequestDTO) {
        log.info(String.valueOf(uploadRequestDTO));
    }
    
    //todo:콜러스쪽 jwt 토큰에 문제가 있어 추후에 추가하는 방식으로 전환(주석 처리 부분)
    @Transactional
    public KollusApiResponse.KollusUrlResponse createKollusURLService(Principal principal, Long lectureId) {
        Long memberId = Long.valueOf(principal.getName());
        Optional<Member> memberOPT = memberRepository.findById(memberId); //todo:예외처리 추가 예정
        Member member = memberOPT.get();
        String userKey = member.getUserKey();

        LocalDateTime kollusCreatedAt = LocalDateTime.now();

        Optional<KollusMedia> kollusMediaOPT = kollusMediaRepository.findByLectureId(lectureId); //todo:예외처리 추가 예정
        KollusMedia kollusMedia = kollusMediaOPT.get();
//        Long kollusMediaId = kollusMedia.getId();
        String mediaContentKey = kollusMedia.getMediaContentKey();

//        Optional<KollusMediaLink> kollusMediaLinkOPT = kollusMediaLinkRepository.findByMemberIdAndKollusMediaId(memberId, kollusMediaId); //todo:예외처리 추가 예정
//        KollusMediaLink kollusMediaLink = kollusMediaLinkOPT.get();
        
//        KollusTokenDTO kollusJwtToken = tokenProvider.createKollusJwtToken(userKey, mediaContentKey);
//        kollusMediaLink.updateMediaToken(kollusJwtToken.kollusAccessToken(), kollusCreatedAt);
//
//        kollusMediaRepository.save(kollusMedia);

        String url = "https://v.kr.kollus.com/" + mediaContentKey; //todo:임시 url 생성
//        String url = "https://v.kr.kollus.com/s?jwt=" + kollusJwtToken.kollusAccessToken() + "&custom_key=" + userKey;
        log.info("생성된 kollus media 접속 url은 : {}", url);

        return KollusApiResponse.KollusUrlResponse.builder()
                .url(url) //todo:url 생성후 url 넣어주기
                .build();
    }

    @Transactional
    public void createKollusUserKey(KollusRequest.userKeyDTO userKeyDTO) throws IOException {

        String API_ACCESS_TOKEN = apiAccessToken;
        String user_agent = userKeyDTO.getUser_agent();
        String client_user_id = userKeyDTO.getClient_user_id();
        String remote_addr = userKeyDTO.getRemote_addr();
        Long user_key_timeout = userKeyDTO.getUser_key_timeout();

        Optional<Member> memberOPT = memberRepository.findByKollusClientUserId(client_user_id);
        Member member = memberOPT.orElseThrow(() -> new CustomException(com.app.bdink.global.exception.Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

        if (member.getUserKey() != null) {
            log.warn("이미 존재하는 userKey 요청: {}", member.getEmail());
            throw new CustomException(Error.EXIST_USERKEY, Error.EXIST_USERKEY.getMessage());
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
