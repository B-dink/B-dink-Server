package com.app.bdink.external.kollus.service;

import com.app.bdink.external.kollus.dto.KollusTokenDTO;
import com.app.bdink.external.kollus.dto.request.UserKeyDTO;
import com.app.bdink.external.kollus.dto.request.callback.DeleteRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.LmsRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.PlayRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.UploadRequestDTO;
import com.app.bdink.external.kollus.dto.response.KollusApiResponse;
import com.app.bdink.external.kollus.dto.response.callback.KollusPlayKind1DTO;
import com.app.bdink.external.kollus.dto.response.callback.KollusPlayKind3DTO;
import com.app.bdink.external.kollus.entity.KollusMedia;
import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.external.kollus.entity.UserKey;
import com.app.bdink.external.kollus.repository.KollusMediaLinkRepository;
import com.app.bdink.external.kollus.repository.KollusMediaRepository;
import com.app.bdink.external.kollus.repository.UserKeyRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.token.KollusTokenProvider;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.repository.MemberRepository;
import com.app.bdink.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final MemberService memberService;
    private final KollusTokenProvider kollusTokenProvider;
    private final UserKeyRepository userKeyRepository;
    private final LectureService lectureService;
    private final MemberRepository memberRepository;

//    @Value("${kollus.API_ACCESS_TOKEN}")
//    private String apiAccessToken;

    public KollusMedia findById(Long id) {
        return kollusMediaRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_KOLLUSMEDIA, Error.NOT_FOUND_KOLLUSMEDIA.getMessage())
        );
    }

    public KollusMedia findByLectureId(Long lectureId) {
        return kollusMediaRepository.findByLectureId(lectureId).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage())
        );
    }


    @Transactional
    public void uploadCallbackService(UploadRequestDTO uploadRequestDTO) {
        log.info("채널 업로드 서비스 시작");
        log.info("content_provider_key is : {}", uploadRequestDTO.content_provider_key());

        String mediaContentKey = uploadRequestDTO.media_content_key();
        Optional<KollusMedia> existingMedia = kollusMediaRepository.findByMediaContentKey(mediaContentKey);

        if (uploadRequestDTO.content_provider_key() == null) {
            log.info("kollus 채널 callback api url 접속을 위한 api 요청");
            return;
        }

        if (existingMedia.isPresent()) {
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

    @Transactional
    public KollusApiResponse.KollusUrlResponse createKollusURLService(Principal principal, Long lectureId) {
        Long memberId = Long.valueOf(principal.getName());
        Member member = memberService.findById(memberId);

        String clientUserId = member.getKollusClientUserId();

        UserKey userKey = userKeyRepository
                .findByMember(member)
                .orElseThrow(
                        () -> new CustomException(Error.NOT_FOUND_USERKEY, Error.NOT_FOUND_USERKEY.getMessage())
                );

//        UserKey userKey = userKeyRepository
//                .findFirstByMemberIdAndIsRevokedFalseOrderByAssignedAtDesc(memberId)
//                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USERKEY, Error.NOT_FOUND_USERKEY.getMessage()));

        LocalDateTime kollusCreatedAt = LocalDateTime.now();


        //TODO: KOLLUS 채널 업로드키 LECTURE에서 들고와도 될것 같기도. -> 사이드이펙트 고려해서 생각 확장해보기.
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
    public void createKollusUserKey(UserKeyDTO userKeyDTO) {
        UserKey userKey = UserKey.of(userKeyDTO.userKey());
        userKeyRepository.save(userKey);
    }

    @Transactional
    public String connectKollusAndLecture(final Lecture lecture, final KollusMedia kollusMedia) {

        kollusMedia.updateLecture(lecture);

        kollusMediaRepository.save(kollusMedia);

        return String.valueOf(kollusMedia.getId());
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
        } else {
            log.info("이미 kollusmedia가 삭제되었거나 요청이 제대로 들어오지 않았습니다.");
            log.info("해당 미디어키 : {}", deleteRequestDTO.media_content_key());
        }
    }

    @Transactional
    public String saveMediaLink(final Member member, final KollusMedia kollusMedia, final Long lectureId) {
        if (kollusMediaLinkRepository.existsByMemberAndKollusMedia(member, kollusMedia)) {
            throw new CustomException(Error.EXIST_KOLLUSMEDIALINK, Error.EXIST_KOLLUSMEDIALINK.getMessage());
        }
        //TODO: 시청기록 생성, 시청기록은 처음 한번만 호출할지? 아니면 이렇게 유지할지 생각

        Lecture lecture = lectureService.findById(lectureId);

        KollusMediaLink kollusMediaLink = KollusMediaLink.builder()
                .member(member)
                .kollusMedia(kollusMedia)
                .lecture(lecture)
                .build();
        kollusMediaLinkRepository.save(kollusMediaLink);
        return String.valueOf(kollusMediaLink.getId());
    }

    @Transactional
    public void lmsCallbackService(LmsRequestDTO lmsRequestDTO) {
        String mediaContentKey = lmsRequestDTO.media_content_key();
        int playtime = Integer.parseInt(lmsRequestDTO.play_time());
        int duration = Integer.parseInt(lmsRequestDTO.duration());
        int playTimePercent = Integer.parseInt(lmsRequestDTO.playtime_percent());
        String kollusId = lmsRequestDTO.client_user_id();

        Member member = memberService.findByKollusClientUserId(kollusId);
        KollusMedia kollusMedia = kollusMediaRepository.findByMediaContentKey(mediaContentKey).orElse(null);

        KollusMediaLink kollusMediaLink = kollusMediaLinkRepository.findByMemberIdAndKollusMediaId(member.getId(), kollusMedia.getId()).
                orElse(null);

        if (playtime > kollusMediaLink.getWatchProgress() && playtime <= duration) {
            kollusMediaLink.updateWatchProgress(playtime);
        }

        if (playTimePercent > kollusMediaLink.getPlaytimePercent() && playTimePercent <= 100) {
            kollusMediaLink.updatePlaytimePercent(playTimePercent);

            if (playTimePercent >= 90) {
                kollusMediaLink.updateCompleted(true);
            }

            kollusMediaLinkRepository.save(kollusMediaLink);
        }
    }

    //todo: 잘 돌아가는지 테스트 해보기 그전에 콜러스 콜백 설정하기
    @Transactional
    public ResponseEntity<?> playCallbackService(PlayRequestDTO playRequestDTO) {
        long unixExp = System.currentTimeMillis() / 1000 + 3600;

        Integer kind = playRequestDTO.getKind();

        if (kind != null && kind == 3) {
            if (playRequestDTO.getClient_user_id() != null) {
                Optional<Member> memberOpt = memberRepository.findByKollusClientUserId(playRequestDTO.getClient_user_id());

                if (memberOpt.isPresent()) {
                    log.info("kind 3 콜백 응답: content_expired=0");
                    log.info("kind 3 콜백 응답 : {}", KollusPlayKind3DTO.ofKind3(0, 1, unixExp));
                    return ResponseEntity.ok(KollusPlayKind3DTO.ofKind3(0, 1, unixExp));
                } else {
                    log.warn("kind 3 콜백 - 유효하지 않은 사용자: {}", playRequestDTO.getClient_user_id());
                    log.info("kind 3 유효x 사용자 응답 : {}", KollusPlayKind3DTO.ofKind3(1, 0, unixExp));
                    return ResponseEntity.ok(KollusPlayKind3DTO.ofKind3(1, 0, unixExp));
                }
            } else {
                log.info("kind 3 콜백 응답: 사용자 ID 없음 → 기본 허용");
                log.info("kind 3 기본 허용 응답 : {}", KollusPlayKind3DTO.ofKind3(1, 0, unixExp));
                return ResponseEntity.ok(KollusPlayKind3DTO.ofKind3(1, 0, unixExp));
            }

        } else {
            log.info("kind 1 콜백 응답: expiration_date={}", unixExp);
            log.info("kind 1 기본 허용 응답 : {}", KollusPlayKind1DTO.ofKind1(unixExp, 1, unixExp));
            return ResponseEntity.ok(KollusPlayKind1DTO.ofKind1(unixExp, 1, unixExp));
        }
    }
}
