package com.app.bdink.center.service;

import com.app.bdink.center.controller.dto.CenterStatus;
import com.app.bdink.center.controller.dto.request.CenterInfoDto;
import com.app.bdink.center.controller.dto.response.CenterAllListDto;
import com.app.bdink.center.controller.dto.response.CenterResponseDto;
import com.app.bdink.center.entity.Center;
import com.app.bdink.center.repository.CenterRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CenterService {

    private final CenterRepository centerRepository;

    public Center findById(Long id) {
        return centerRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CENTER, Error.NOT_FOUND_CENTER.getMessage()));
    }

    @Transactional
    public String saveCenter(CenterInfoDto dto) {
        Long id = centerRepository.save(
                Center.builder()
                        .name(dto.centerName())
                        .address(dto.centerAddress())
                        .qrToken(dto.centerQrToken())
                        .qrTokenExpiredAt(dto.centerQrTokenExpiredAt())
                        .build()
        ).getId();
        return id.toString();
    }

    @Transactional
    public CenterInfoDto updateCenter(Long id, CenterInfoDto dto) {
        Center center = findById(id);
        center.updateName(dto.centerName());
        center.updateAddress(dto.centerAddress());
        center.updateQrToken(dto.centerQrToken());
        center.updateQrTokenExpiredAt(dto.centerQrTokenExpiredAt());
        center = centerRepository.save(center);
        return CenterInfoDto.of(center);
    }

    @Transactional
    public CenterResponseDto getCenterInfo(Long id) {
        Center center = findById(id);
        return CenterResponseDto.of(center);
    }

    @Transactional
    public CenterResponseDto deleteCenter(Long id) {
        Center center = findById(id);
        center.updateStatus(CenterStatus.TERMINATED);
        centerRepository.save(center);
        return CenterResponseDto.of(center);
    }

    public List<CenterAllListDto> getAllInProgressCenters() {
        List<Center> inProgressCenters = centerRepository.findByStatus(CenterStatus.IN_PROGRESS);

        // 엔티티 목록을 DTO 목록으로 변환하여 반환
        return inProgressCenters.stream()
                .map(CenterAllListDto::of)
                .toList();
    }
}
