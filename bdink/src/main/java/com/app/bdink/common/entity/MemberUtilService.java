package com.app.bdink.common.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MemberUtilService {
    public Long getMemberId(Principal principal){
        return Long.parseLong(principal.getName());
    }
}
