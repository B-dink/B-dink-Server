package com.app.bdink.external.kollus.service;

import com.app.bdink.external.kollus.dto.KollusTokenDTO;
import com.app.bdink.external.kollus.dto.request.UserKeyDTO;
import com.app.bdink.external.kollus.dto.request.callback.DeleteRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.PlayRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.UploadRequestDTO;
import com.app.bdink.external.kollus.dto.response.KollusApiResponse;
import com.app.bdink.external.kollus.entity.KollusMedia;
import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.external.kollus.entity.UserKey;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.external.kollus.repository.KollusMediaRepository;
import com.app.bdink.external.kollus.repository.UserKeyRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.token.KollusTokenProvider;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final KollusTokenProvider kollusTokenProvider;
    private final UserKeyRepository userKeyRepository;
//    @Value("${kollus.API_ACCESS_TOKEN}")
//    private String apiAccessToken;


    @Transactional
    public void uploadCallbackService(UploadRequestDTO uploadRequestDTO) {
        log.info("채널 업로드 서비스 시작");
        log.info("content_provider_key is : {}", uploadRequestDTO.content_provider_key());

        String mediaContentKey = uploadRequestDTO.media_content_key();
        Optional<KollusMedia> existingMedia = kollusMediaRepository.findByMediaContentKey(mediaContentKey);
        
        if(uploadRequestDTO.content_provider_key() == null){
            log.info("kollus 채널 callback api url 접속을 위한 api 요청");
            return;
        }

        if(existingMedia.isPresent()) {
            log.warn("이미 존재하는 미디어키 입니다. : {}", mediaContentKey);
            return;
        }

        KollusMedia kollusMedia = KollusMedia.builder()
                .filename(uploadRequestDTO.filename())
                .uploadFileKey(uploadRequestDTO.upload_file_key())
                .mediaContentKey(mediaContentKey)
                .channelKey(uploadRequestDTO.channel_key())
                .channelName(uploadRequestDTO.channel_name())
                .lecture(null)
                .build();

        kollusMediaRepository.save(kollusMedia);
    }
    
    //todo:콜러스쪽 jwt 토큰에 문제가 있어 추후에 추가하는 방식으로 전환(주석 처리 부분)
    @Transactional
    public KollusApiResponse.KollusUrlResponse createKollusURLService(Principal principal, Long lectureId) {
        Long memberId = Long.valueOf(principal.getName());
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
        String clientUserId = member.getKollusClientUserId();

        UserKey userKey = userKeyRepository
                .findFirstByMemberIdAndIsRevokedFalseOrderByAssignedAtDesc(memberId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USERKEY, Error.NOT_FOUND_USERKEY.getMessage()));

        LocalDateTime kollusCreatedAt = LocalDateTime.now();

        KollusMedia kollusMedia = kollusMediaRepository
                .findByLectureId(lectureId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage()));

        Long kollusMediaId = kollusMedia.getId();
        String mediaContentKey = kollusMedia.getMediaContentKey();

        KollusMediaLink kollusMediaLink = kollusMediaLinkRepository
                .findByMemberIdAndKollusMediaId(memberId, kollusMediaId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage()));

        KollusTokenDTO kollusJwtToken = kollusTokenProvider.createKollusJwtToken(clientUserId, mediaContentKey);
        kollusMediaLink.updateMediaToken(kollusJwtToken.kollusAccessToken(), kollusCreatedAt);

        kollusMediaRepository.save(kollusMedia);

        String url = "https://v.kr.kollus.com/s?jwt=" + kollusJwtToken.kollusAccessToken() + "&custom_key=" + userKey.getUserKey();
        log.info("생성된 kollus media 접속 url은 : {}", url);

        return KollusApiResponse.KollusUrlResponse.builder()
                .url(url)
                .build();
    }

    @Transactional
    public void createKollusUserKey(UserKeyDTO userKeyDTO){
        UserKey userKey = UserKey.of(userKeyDTO.userKey());
        userKeyRepository.save(userKey);
    }

//    @Transactional
//    public void createKollusUserKeyApi(KollusRequest.userKeyDTO userKeyDTO) throws IOException {
//
//        String API_ACCESS_TOKEN = apiAccessToken;
//        String user_agent = userKeyDTO.getUser_agent();
//        String client_user_id = userKeyDTO.getClient_user_id();
//        String remote_addr = userKeyDTO.getRemote_addr();
//        Long user_key_timeout = userKeyDTO.getUser_key_timeout();
//
//        Optional<Member> memberOPT = memberRepository.findByKollusClientUserId(client_user_id);
//        Member member = memberOPT.orElseThrow(() -> new CustomException(com.app.bdink.global.exception.Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
//
//        if (member.getUserKey() != null) {
//            log.warn("이미 존재하는 userKey 요청: {}", member.getEmail());
//            throw new CustomException(Error.EXIST_USERKEY, Error.EXIST_USERKEY.getMessage());
//        }
//
//        String payload = String.format(
//                "user_agent=%s&client_user_id=%s&remote_addr=%s&user_key_timeout=%d",
//                user_agent, client_user_id, remote_addr, user_key_timeout
//        );
//
//        String kollusUrl = String.format(
//                "https://c-api-kr.kollus.com/api/media/user-key?access_token=%s",
//                API_ACCESS_TOKEN
//        );
//
//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, payload);
//        Request request = new Request.Builder()
//                .url(kollusUrl)
//                .post(body)
//                .addHeader("accept", "application/json")
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        if (response.isSuccessful()) {
//            String responseBody = response.body().string();
//            ObjectMapper objectMapper = new ObjectMapper();
//            KollusResponse kollusResponse = objectMapper.readValue(responseBody, KollusResponse.class);
//            String userKey = kollusResponse.getData().getUser_key();
//
//            //UserKey save 로직
//            member.updateUserKey(userKey);
//            memberRepository.save(member);
//        }
//    }

    @Transactional
    public void deleteCallbackService(DeleteRequestDTO deleteRequestDTO) {
        Optional<KollusMedia> deleteOPT = kollusMediaRepository.findByMediaContentKey(deleteRequestDTO.media_content_key());
        if (deleteOPT.isPresent()) {
            kollusMediaRepository.delete(deleteOPT.get());
        }else{
            log.info("이미 kollusmedia가 삭제되었거나 요청이 제대로 들어오지 않았습니다.");
            log.info("해당 미디어키 : {}", deleteRequestDTO.media_content_key());
        }
    }

    @Transactional
    public void playCallbackService(PlayRequestDTO playRequestDTO) {
        log.info(String.valueOf(playRequestDTO));
    }
}
