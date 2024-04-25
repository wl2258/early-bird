package com.ssonzm.orderservice.common.exception;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.coremodule.exception.CommonValidationException;
import com.ssonzm.coremodule.util.ResponseUtil;
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
        ResponseDto<String> responseDto = ResponseUtil.setResponseDto(messageSource, e.getMessage());
        responseDto.setBody(e.getErrorMap().toString());

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonBadRequestException.class)
    public ResponseEntity<?> commonBadRequestException(CommonBadRequestException e) {

        log.error(e.getMessage());

        return new ResponseEntity<>(ResponseUtil.setResponseDto(messageSource, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> typeMismatchApiException(HttpMessageNotReadableException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(ResponseUtil.setResponseDto(messageSource, false), HttpStatus.BAD_REQUEST);
    }
}
