package com.app.bdink.instructor.domain;

import com.app.bdink.classroom.domain.Career;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstructorDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Career career;

    private String marketingImage;

    private String marketingText;

    private String marketingSites;

    public void modify(final Career career){
        this.career = career;
    }

    public void softDelete(){
        this.career = null;
    }

}
