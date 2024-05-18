package com.ssonzm.controller.return_product;

import com.ssonzm.common.util.ResponseUtil;
import com.ssonzm.config.security.PrincipalDetails;
import com.ssonzm.dto.common.ResponseDto;
import com.ssonzm.dto.return_product.ReturnProductRequestDto;
import com.ssonzm.service.return_product.ReturnProductService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReturnProductController {

    private final MessageSource messageSource;

    private final ReturnProductService returnProductService;

    public ReturnProductController(MessageSource messageSource, ReturnProductService returnProductService) {
        this.messageSource = messageSource;
        this.returnProductService = returnProductService;
    }

    @PostMapping("/authz/returns")
    public ResponseEntity<?> saveReturns(@RequestBody @Valid ReturnProductRequestDto.ReturnProductSaveReqDto returnProductSaveReqDto,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long returnProductId =
                returnProductService.saveReturn(principalDetails.getUser().getId(), returnProductSaveReqDto);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(returnProductId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
