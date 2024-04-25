package com.ssonzm.productservice.controller.verify_code;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.productservice.service.verify_code.VerifyCodeService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.ssonzm.productservice.dto.verify_code.VerifyCodeDto.SendEmailCodeReqDto;

@RestController
@RequestMapping("/api/verify-code")
public class VerifyCodeController {

    private final MessageSource messageSource;
    private final VerifyCodeService verifyCodeService;

    public VerifyCodeController(MessageSource messageSource, VerifyCodeService verifyCodeService) {
        this.messageSource = messageSource;
        this.verifyCodeService = verifyCodeService;
    }

    @PostMapping
    public ResponseEntity<?> sendVerifyCode(@RequestBody @Valid SendEmailCodeReqDto sendEmailCodeReqDto,
                                            BindingResult bindingResult) {
        String verifyCode = verifyCodeService.sendVerifyCode(sendEmailCodeReqDto.getEmail());

        ResponseDto<String> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(verifyCode);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String verifyCode) {
        boolean isEqual = verifyCodeService.checkVerifyCode(email, verifyCode);

        ResponseDto<Boolean> responseDto = ResponseUtil.setResponseDto(messageSource, isEqual);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
