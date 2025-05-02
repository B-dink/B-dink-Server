package com.app.bdink.classroom.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomDetailImage;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.repository.ClassRoomDetailImageRepository;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassRoomDetailPageImageService {
    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomDetailImageRepository classRoomDetailImageRepository;

    @Transactional
    public void saveImages(Long classRoomId, List<String> imageUrls) {

        ClassRoomEntity classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow( () -> new CustomException(Error.NOT_FOUND_CLASSROOM, Error.NOT_FOUND_CLASSROOM.getMessage()));

        int sortOrder = 1;

        for (String url : imageUrls) {
            ClassRoomDetailImage image = ClassRoomDetailImage.builder()
                    .imageUrl(url)
                    .classRoomEntity(classRoom)
                    .sortOrder(sortOrder)
                    .build();
            classRoomDetailImageRepository.save(image);
        }
    }
}
