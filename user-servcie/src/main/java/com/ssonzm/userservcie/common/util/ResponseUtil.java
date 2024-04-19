package com.ssonzm.userservcie.common.util;

import com.ssonzm.userservcie.dto.common.ResponseDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class ResponseUtil {
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
}
