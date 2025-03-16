package com.app.bdink.external.coolsms.repository;

import com.app.bdink.external.coolsms.entity.PhoneVerify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository<PhoneVerify, UUID> {
}
