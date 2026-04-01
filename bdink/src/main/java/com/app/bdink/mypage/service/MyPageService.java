package com.app.bdink.mypage.service;

import com.app.bdink.centerowner.entity.CenterOwner;
import com.app.bdink.centerowner.repository.CenterOwnerRepository;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.instructor.repository.InstructorRepository;
import com.app.bdink.member.entity.Member;
import com.app.bdink.mypage.dto.response.MyPageResponse;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.entity.TrainerStatus;
import com.app.bdink.trainer.repository.TrainerRepository;
import com.app.bdink.trainermembership.entity.TrainerMembershipStatus;
import com.app.bdink.trainermembership.repository.TrainerMembershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final InstructorRepository instructorRepository;
    private final TrainerRepository trainerRepository;
    private final CenterOwnerRepository centerOwnerRepository;
    private final TrainerMembershipRepository trainerMembershipRepository;

    public MyPageResponse getMemberInfo(Member member) {
        boolean isInstructor = false;
        boolean isTrainer = false;
        boolean isTrainerMembership = false;
        boolean isUser = false;
        Optional<Instructor> instructor = instructorRepository.findByMemberId(member.getId());
        Optional<Trainer> trainer =  trainerRepository.findByMemberId(member.getId());
        Optional<CenterOwner> centerOwner = centerOwnerRepository.findByMemberId(member.getId());

        Optional<Trainer> activeTrainer = trainerRepository.findByMemberIdAndStatus(member.getId(), TrainerStatus.ACTIVE);
        if (activeTrainer.isPresent()) {
            // 활성 트레이너가 있고, 그 트레이너에 대한 활성 멤버십이 존재할 때만 true로 본다.
            isTrainerMembership = trainerMembershipRepository
                    .findByTrainerAndTrainerMembershipStatus(activeTrainer.get(), TrainerMembershipStatus.ACTIVE)
                    .isPresent();
        }

        /***
         * 현재 강사와 트레이너가 분리되어있다고 가정 후 진행
         */
        if (instructor.isPresent()) {
            isInstructor = true;
        }
        //trainer와 centerOwner는 같은 역할로 취급
        else if (trainer.isPresent() || centerOwner.isPresent()) {
            isTrainer = true;
        }else
            isUser = true;

        return MyPageResponse.of(member, isInstructor, isTrainer, isTrainerMembership, isUser);
    }
}
