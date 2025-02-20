package com.app.bdink.nutrient.repository;

import com.app.bdink.nutrient.entity.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutrientRepository extends JpaRepository<Nutrient, Long> {

}
