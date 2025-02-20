package com.app.bdink.nutrient.controller;

import com.app.bdink.nutrient.controller.dto.request.NutrientReqDto;
import com.app.bdink.nutrient.service.NutrientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/nutrients")
@Tag(name = "nutrients API", description = "영양과 관련된 API들입니다. 영양은 관리자만 생성할 수 있습니다.")
public class NutrientController {

    private final NutrientService nutrientService;

    @Operation(method = "POST", description = "영양을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createNutrient(@RequestBody NutrientReqDto nutrientReqDto) {
        String id = nutrientService.createNutrient(nutrientReqDto);
        return ResponseEntity.created(
                URI.create(id))
            .build();
    }

    @Operation(method = "GET", description = "모든 영양을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getAllNutrients() {
        return ResponseEntity.ok().body(nutrientService.getAllNutrients());
    }

    @Operation(method = "GET", description = "영양 상세를 조회합니다.")
    @GetMapping("/detail")
    public ResponseEntity<?> getNutrientDetail(@RequestParam Long id) {
        return ResponseEntity.ok().body(nutrientService.getNutrientDetail(id));
    }

    @Operation(method = "PUT", description = "영양을 수정합니다.")
    @PutMapping
    public ResponseEntity<?> updateNutrient(@RequestParam Long id, @RequestBody NutrientReqDto nutrientReqDto) {
        nutrientService.updateNutrient(id, nutrientReqDto);
        return ResponseEntity.ok().build();
    }

    @Operation(method = "DELETE", description = "영양을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteNutrient(@RequestParam Long id) {
        nutrientService.deleteNutrient(id);
        return ResponseEntity.ok().build();
    }
}
