package com.ssonzm.userservcie.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.userservcie.dto.common.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class ResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    public static <T> ResponseDto<T> setResponseDto(MessageSource messageSource, boolean isSuccess) {
        ResponseDto<T> responseDto = new ResponseDto<>();
        if (isSuccess) {
            responseDto.setCode(Integer.parseInt(messageSource.getMessage("successResult.code", null, LocaleContextHolder.getLocale())));
            responseDto.setMsg(messageSource.getMessage("successResult.msg", null, LocaleContextHolder.getLocale()));
        } else {
            responseDto.setCode(Integer.parseInt(messageSource.getMessage("failResult.code", null, LocaleContextHolder.getLocale())));
            responseDto.setMsg(messageSource.getMessage("failResult.msg", null, LocaleContextHolder.getLocale()));
        }
        return responseDto;
    }

    public static ResponseDto<String> setResponseDto(MessageSource messageSource, String exception) {
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setCode(Integer.parseInt(messageSource.getMessage(exception + ".code", null, LocaleContextHolder.getLocale())));
        responseDto.setMsg(messageSource.getMessage(exception + ".msg", null, LocaleContextHolder.getLocale()));
        return responseDto;
    }

    public static void success(HttpServletResponse response, MessageSource messageSource, Object dto) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<Object> responseDto = setResponseDto(messageSource, true);
            responseDto.setBody(dto);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }

    public static void fail(HttpServletResponse response, MessageSource messageSource, HttpStatus httpStatus, String exception) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<String> responseDto = setResponseDto(messageSource, exception);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }
}
