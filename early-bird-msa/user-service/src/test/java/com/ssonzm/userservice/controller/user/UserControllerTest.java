package com.ssonzm.userservice.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.userservice.common.util.DummyUtil;
import com.ssonzm.userservice.domain.user.UserRepository;
import com.ssonzm.coremodule.dto.user.UserRequestDto.UserSignUpReqDto;
import com.ssonzm.coremodule.dto.user.UserRequestDto.UserUpdatePwReqDto;
import com.ssonzm.coremodule.dto.user.UserRequestDto.UserUpdateReqDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql("classpath:teardown.sql")
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WithUserDetails(value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class UserControllerTest extends DummyUtil {

    @Autowired
    EntityManager em;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Mock
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        dataSetting();
        when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("1234");
    }

    @Test
    @DisplayName("1. 회원가입")
    void sinUpTest() throws Exception {
        // given
        String name = "김철수";
        String email = "steelwater@naver.com";
        String pw = "qwe123!!";
        String phoneNumber = "010-1234-5678";
        String address = "서울특별시 강북구";
        UserSignUpReqDto userSignUpReqDto = new UserSignUpReqDto(name, email, pw, phoneNumber, address);
        String requestBody = om.writeValueAsString(userSignUpReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/users")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("2. 회원가입 실패 - validation phoneNumber")
    void sinUpFailTest1() throws Exception {
        // given
        String name = "김철수";
        String email = "steelwater@naver.com";
        String pw = "qwe123!!";
        String phoneNumber = "01012345678";
        String address = "서울특별시 강북구";
        UserSignUpReqDto userSignUpReqDto = new UserSignUpReqDto(name, email, pw, phoneNumber, address);
        String requestBody = om.writeValueAsString(userSignUpReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/users")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("3. 회원가입 실패 - validation password")
    void sinUpFailTest2() throws Exception {
        // given
        String name = "김철수";
        String email = "steelwater@naver.com";
        String pw = "qwe123!!";
        String phoneNumber = "01012345678";
        String address = "서울특별시 강북구";
        UserSignUpReqDto userSignUpReqDto = new UserSignUpReqDto(name, email, pw, phoneNumber, address);
        String requestBody = om.writeValueAsString(userSignUpReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/users")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("4. 비밀번호 변경")
    void updatePw() throws Exception {
        // given
        String newPw = "iloveearlybird";
        UserUpdatePwReqDto userUpdatePwReqDto = new UserUpdatePwReqDto(newPw);
        String requestBody = om.writeValueAsString(userUpdatePwReqDto);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/authz/users/pw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("5. 전화번호 주소 변경")
    void updateUserInfo() throws Exception {
        // given
        UserUpdateReqDto userUpdatePwReqDto = new UserUpdateReqDto("010-7777-7777", "서울광역시 중구");
        String requestBody = om.writeValueAsString(userUpdatePwReqDto);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/authz/users")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("6. 사용자 상세 정보 조회")
    void getUserDetailsTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/authz/users")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        userRepository.save(newUser("신짱구", "test@naver.com"));

        em.clear();
    }
}