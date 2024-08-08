package com.todobuddy.backend.service;

import com.todobuddy.backend.entity.VerificationCode;
import com.todobuddy.backend.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    // redis에 인증 코드 저장
    @Transactional
    public boolean saveVerificationCode(String email, String verificationCode) {
        try {
            VerificationCode createVerificationCode = VerificationCode.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();

            verificationCodeRepository.save(createVerificationCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
