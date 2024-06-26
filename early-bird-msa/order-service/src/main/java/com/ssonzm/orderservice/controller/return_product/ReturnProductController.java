package com.ssonzm.orderservice.controller.return_product;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.coremodule.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;
import com.ssonzm.orderservice.service.return_product.ReturnProductService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> saveReturns(@RequestBody @Valid ReturnProductSaveReqDto returnProductSaveReqDto,
                                         BindingResult bindingResult,
                                         @RequestHeader("x_user_id") Long userId) {
        Long returnProductId =
                returnProductService.saveReturn(userId, returnProductSaveReqDto);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(returnProductId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
