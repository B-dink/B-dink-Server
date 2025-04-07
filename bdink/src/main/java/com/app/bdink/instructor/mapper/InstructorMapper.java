package com.app.bdink.instructor.mapper;

import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.instructor.domain.InstructorDomain;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;
import com.app.bdink.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class InstructorMapper {
    public InstructorDomain commandToDomain(CreateClassRoomCommand command){
        return InstructorDomain.builder()
                .id(command.instructor().getId())
                .career(command.instructor().getCareer())
                .marketingImage(command.instructor().getMarketingImage())
                .marketingSites(command.instructor().getMarketingSites())
                .marketingText(command.instructor().getMarketingText())
                .build();
    }

    public Instructor toEntity(InstructorDomain instructorDomain, Member member){
        return Instructor.builder()
                .member(member)
                .career(instructorDomain.getCareer())
                .marketingImage(instructorDomain.getMarketingImage())
                .marketingSites(instructorDomain.getMarketingSites())
                .marketingText(instructorDomain.getMarketingText())
                .build();
    }

    public Instructor commandToEntity(CreateClassRoomCommand command){
        return command.instructor();
    }
}
