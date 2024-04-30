package com.ssonzm.userservice.service.user;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.userservice.config.security.PrincipalDetails;
import com.ssonzm.userservice.domain.user.User;
import com.ssonzm.userservice.domain.user.UserRepository;
import com.ssonzm.userservice.domain.user.UserRole;
import com.ssonzm.userservice.service.client.ProductServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.ssonzm.coremodule.dto.user.UserRequestDto.*;
import static com.ssonzm.coremodule.dto.user.UserResponseDto.UserDetailsDto;
import static com.ssonzm.coremodule.dto.user.UserResponseDto.UserMyPageRespDto;
import static com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;
import static com.ssonzm.coremodule.vo.wish_product.WishProductResponseVo.WishProductListRespVo;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final ProductServiceClient productServiceClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public UserServiceImpl(MessageSource messageSource, UserRepository userRepository,
                           ProductServiceClient productServiceClient,
                           BCryptPasswordEncoder bCryptPasswordEncoder, CircuitBreakerFactory circuitBreakerFactory) {
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.productServiceClient = productServiceClient;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("notFoundUser"));
        return new PrincipalDetails(findUser);
    }

    @Override
    @Transactional
    public void signUp(UserSignUpReqDto userSignUpReqDto) {
        if (userRepository.existsByEmail(userSignUpReqDto.getEmail()))
            throw new CommonBadRequestException("duplicationUser");

        User user = User.builder()
                .username(userSignUpReqDto.getName())
                .email(userSignUpReqDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userSignUpReqDto.getPassword()))
                .phoneNumber(userSignUpReqDto.getPhoneNumber())
                .address(userSignUpReqDto.getAddress())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UserUpdatePwReqDto userUpdatePwReqDto) {
        User findUser = findByIdOrElseThrow(userId);

        String encodedPw = bCryptPasswordEncoder.encode(userUpdatePwReqDto.getPassword());
        findUser.updatePassword(encodedPw);
    }

    @Override
    @Transactional
    public void updateUserInfo(Long userId, UserUpdateReqDto userUpdateReqDto) {
        User findUser = findByIdOrElseThrow(userId);

        findUser.updateUserInfo(userUpdateReqDto);
    }

    @Override
    public UserDetailsDto getUserDetails(Long userId) {
        User findUser = findByIdOrElseThrow(userId);

        return new ModelMapper().map(findUser, UserDetailsDto.class);
    }

    @Override
    public UserMyPageRespDto getMyPage(Long userId) {

        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("product-circuit-breaker");

        Page<ProductListRespVo> productList = circuitbreaker
                .run(() -> productServiceClient.getProductListSavedByUser(userId).getBody().getBody(),
                        throwable -> new PageImpl<>(Collections.emptyList()));

        Page<WishProductListRespVo> wishProductList = circuitbreaker
                .run(() -> productServiceClient.getWishProductList(userId).getBody().getBody(),
                        throwable -> new PageImpl<>(Collections.emptyList()));

        return new UserMyPageRespDto(productList, wishProductList);
    }

    @Override
    public User findByIdOrElseThrow(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> {
            String messageSource = this.messageSource.getMessage("notFoundUser.msg", null, LocaleContextHolder.getLocale());
            log.error(messageSource);
            return new CommonBadRequestException(messageSource);
        });
    }

    private User findByEmailOrElseThrow(String email, String msg) {

        return userRepository.findByEmail(email).orElseThrow(() -> {
            String messageSource = this.messageSource.getMessage(msg + ".msg", null, LocaleContextHolder.getLocale());
            log.error(messageSource);
            return new CommonBadRequestException(messageSource);
        });
    }
}
