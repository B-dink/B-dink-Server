package com.app.bdink.lecture.controller.dto.response;

import com.app.bdink.member.entity.Member;

public record InstructorInfoDto(

        String career,

        String name

) {
    //OneToOne 필드는 기본적으로 Eager로딩으로 가져와지기 때문에 굳이 Instructor를 인자로 또 추가해주지않았습니다.
    public static InstructorInfoDto from(final Member member){
        return new InstructorInfoDto(
                member.getInstructor().getCareer().toString(),
                member.getName()
        );
    }
}
