package com.ssonzm.userservcie.common.exception;

import com.ssonzm.userservcie.common.util.ResponseUtil;
import com.ssonzm.userservcie.dto.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    private final MessageSource messageSource;

    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CommonValidationException.class)
    public ResponseEntity<?> commonValidationException(CommonValidationException e) {

        log.error(e.getMessage());
        ResponseDto<Object> responseDto = ResponseUtil.setResponseDto(messageSource, false);
        responseDto.setBody(e.getErrorMap());

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonBadRequestException.class)
    public ResponseEntity<?> commonBadRequestException(CommonBadRequestException e) {

        log.error(e.getMessage());

        return new ResponseEntity<>(ResponseUtil.setResponseDto(messageSource, false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> typeMismatchApiException(HttpMessageNotReadableException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(ResponseUtil.setResponseDto(messageSource, false), HttpStatus.BAD_REQUEST);
    }
}
