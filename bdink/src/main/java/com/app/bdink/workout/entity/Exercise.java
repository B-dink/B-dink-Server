package com.app.bdink.workout.entity;

import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.controller.dto.request.ExerciseReqDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    private ExercisePart part;

    @Builder
    public Exercise(String name, String description, String videoUrl, String pictureUrl, ExercisePart part) {
        this.name = name;
        this.description = description;
        this.videoUrl = videoUrl;
        this.pictureUrl = pictureUrl;
        this.part = part;
    }

    public void modifyExercise(final ExerciseReqDto exerciseReqDto, final String videoKey, final String pictureKey){
        this.name = updateName(exerciseReqDto.ExerciseName());
        this.description = updateDescription(exerciseReqDto.ExerciseDescription());
        this.videoUrl = updateVideoUrl(videoKey);
        this.pictureUrl = updatePictureUrl(pictureKey);
        this.part = updatePart(exerciseReqDto.ExercisePart());
    }

    public String updateName(final String name){
        if(name.isBlank() || name == null){
            return this.name;
        }
        this.name = name;
        return this.name;
    }

    public String updateDescription(final String description){
        if(description.isBlank() || description == null){
            return this.description;
        }
        this.description = description;
        return this.description;
    }

    public String updateVideoUrl(final String videoUrl){
        if(videoUrl.isBlank() || videoUrl == null){
            return this.videoUrl;
        }
        this.videoUrl = videoUrl;
        return this.videoUrl;
    }

    public String updatePictureUrl(final String pictureUrl){
        if(pictureUrl.isBlank() || pictureUrl == null){
            return this.pictureUrl;
        }
        this.pictureUrl = pictureUrl;
        return this.pictureUrl;
    }

    public ExercisePart updatePart(final ExercisePart part){
        if(part == null){
            return this.part;
        }
        this.part = part;
        return this.part;
    }
}
