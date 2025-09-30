package com.app.bdink.message.repository;

import com.app.bdink.message.entity.AlimtalkToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlimtalkTokenRepository extends JpaRepository<AlimtalkToken, Long> {

    Optional<AlimtalkToken> findTopByOrderByIdDesc();
}