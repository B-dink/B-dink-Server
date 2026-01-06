package com.app.bdink.centerowner.service;

import com.app.bdink.center.entity.Center;
import com.app.bdink.center.service.CenterService;
import com.app.bdink.centerowner.controller.dto.request.CenterOwnerCreateRequest;
import com.app.bdink.centerowner.controller.dto.request.CenterOwnerUpdateRequest;
import com.app.bdink.centerowner.controller.dto.response.CenterOwnerResponse;
import com.app.bdink.centerowner.entity.CenterOwner;
import com.app.bdink.centerowner.entity.CenterOwnerStatus;
import com.app.bdink.centerowner.repository.CenterOwnerRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 센터장 도메인 비즈니스 로직을 담당한다.
 * 생성 시 기존 비활성 센터장이 있으면 재활성화한다.
 */
@Service
@RequiredArgsConstructor
public class CenterOwnerService {

    private final CenterOwnerRepository centerOwnerRepository;
    private final CenterService centerService;

    /**
     * 센터장을 생성한다.
     * 이미 비활성 센터장이 존재하면 해당 엔티티를 갱신 후 활성화한다.
     */
    @Transactional
    public String createCenterOwner(Member member, CenterOwnerCreateRequest request) {
        Center center = centerService.findById(request.centerId());

        CenterOwner existingOwner = centerOwnerRepository.findByCenterIdAndMemberId(request.centerId(), member.getId()).orElse(null);
        if (existingOwner != null) {
            if (existingOwner.getStatus() == CenterOwnerStatus.ACTIVE) {
                throw new CustomException(Error.EXIST_CENTER_OWNER, Error.EXIST_CENTER_OWNER.getMessage());
            }
            existingOwner.updateCenter(center);
            existingOwner.activate();
            return existingOwner.getId().toString();
        }

        CenterOwner centerOwner = CenterOwner.builder()
                .center(center)
                .member(member)
                .build();

        return centerOwnerRepository.save(centerOwner).getId().toString();
    }

    /**
     * 활성 상태 센터장만 조회한다.
     */
    @Transactional(readOnly = true)
    public CenterOwner getActiveCenterOwner(Long id) {
        return centerOwnerRepository.findByIdAndStatus(id, CenterOwnerStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CENTER_OWNER, Error.NOT_FOUND_CENTER_OWNER.getMessage()));
    }

    /**
     * 센터 기준 활성 센터장 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CenterOwner> getActiveCenterOwnersByCenter(Long centerId) {
        return centerOwnerRepository.findAllByCenterIdAndStatus(centerId, CenterOwnerStatus.ACTIVE);
    }

    /**
     * 센터장 소속 센터를 수정한다.
     */
    @Transactional
    public CenterOwnerResponse updateCenterOwner(Long id, CenterOwnerUpdateRequest request) {
        CenterOwner centerOwner = getActiveCenterOwner(id);
        Center center = centerService.findById(request.centerId());
        centerOwner.updateCenter(center);
        return CenterOwnerResponse.from(centerOwner);
    }

    /**
     * 센터장을 soft delete 처리한다.
     */
    @Transactional
    public void deleteCenterOwner(Long id) {
        CenterOwner centerOwner = getActiveCenterOwner(id);
        centerOwner.deactivate();
    }

    /**
     * 특정 센터에 대한 센터장 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public boolean isCenterOwner(Long centerId, Long memberId) {
        return centerOwnerRepository.existsByCenterIdAndMemberIdAndStatus(centerId, memberId, CenterOwnerStatus.ACTIVE);
    }
}
