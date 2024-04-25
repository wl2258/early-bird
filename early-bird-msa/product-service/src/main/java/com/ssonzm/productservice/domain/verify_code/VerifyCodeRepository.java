package com.ssonzm.productservice.domain.verify_code;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerifyCodeRepository extends CrudRepository<VerifyCode, Long> {

    Optional<VerifyCode> findByEmail(String email);
}