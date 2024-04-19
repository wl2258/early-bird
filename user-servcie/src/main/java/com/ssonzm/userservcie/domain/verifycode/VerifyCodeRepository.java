package com.ssonzm.userservcie.domain.verifycode;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerifyCodeRepository extends CrudRepository<VerifyCode, Long> {

    Optional<VerifyCode> findByEmail(String email);
}