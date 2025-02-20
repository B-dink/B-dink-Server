package com.app.bdink.nutrient.service;

import com.app.bdink.nutrient.controller.dto.request.NutrientReqDto;
import com.app.bdink.nutrient.controller.dto.response.NutrientResDto;
import com.app.bdink.nutrient.entity.Nutrient;
import com.app.bdink.nutrient.repository.NutrientRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NutrientService {

    private final NutrientRepository nutrientRepository;

    @Transactional
    public String createNutrient(NutrientReqDto nutrientReqDto) {
        Long id = nutrientRepository.save(
            nutrientReqDto.toEntity())
            .getId();
        return String.valueOf(id);
    }

    public List<NutrientResDto> getAllNutrients() {
        List<Nutrient> nutrientList = nutrientRepository.findAll();
        return nutrientList.stream()
            .map(NutrientResDto::new)
            .collect(Collectors.toList());
    }

    public NutrientResDto getNutrientDetail(Long nutrientId) {
        Nutrient nutrient = getById(nutrientId);
        return new NutrientResDto(nutrient);
    }

    @Transactional
    public void updateNutrient(Long nutrientId, NutrientReqDto nutrientReqDto) {
        Nutrient nutrient = getById(nutrientId);

        nutrient.update(nutrientReqDto.title(), nutrientReqDto.content(), nutrientReqDto.imgUrl());
    }

    @Transactional
    public void deleteNutrient(Long nutrientId) {
        Nutrient nutrient = getById(nutrientId);

        nutrientRepository.delete(nutrient);
    }

    public Nutrient getById(Long nutrientId) {
        return nutrientRepository.findById(nutrientId).orElseThrow(
            () -> new IllegalArgumentException("해당 영양을 찾지 못했습니다.")
        );
    }
}
