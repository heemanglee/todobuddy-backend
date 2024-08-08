package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.VerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {

}