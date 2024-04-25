package com.ssonzm.productservice.controller.wish_product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.productservice.common.util.DummyUtil;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductRepository;
import com.ssonzm.productservice.domain.product.ProductStatus;
import com.ssonzm.productservice.domain.user.User;
import com.ssonzm.productservice.domain.user.UserRepository;
import com.ssonzm.productservice.domain.wish_product.WishProductRepository;
import com.ssonzm.productservice.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;
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

import static com.ssonzm.productservice.dto.wish_product.WishProductRequestDto.WishProductUpdateReqDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql("classpath:teardown.sql")
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WithUserDetails(value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class WishProductControllerTest extends DummyUtil {

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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WishProductRepository wishProductRepository;

    @BeforeEach
    void init() {
        dataSetting();
        when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("1234");
    }

    @Test
    @DisplayName("1. 관심 상품 등록")
    void saveProductTest() throws Exception {
        // given
        Long productId = 1L;
        WishProductSaveReqDto wishProductSaveReqDto = new WishProductSaveReqDto(productId, 5);
        String requestBody = om.writeValueAsString(wishProductSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/authz/wish-products")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("2. 관신 상품 조회")
    void getWishProductListTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/authz/wish-products")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("3. 관심 상품 수량 변경")
    void updateQuantityTest() throws Exception {
        // given
        Long wishProductId = 1L;
        Long productId = 1L;
        WishProductUpdateReqDto wishProductUpdateReqDto = new WishProductUpdateReqDto(wishProductId, productId, 5);
        String requestBody = om.writeValueAsString(wishProductUpdateReqDto);

        // when
        ResultActions resultActions = mvc.perform(patch("/api/authz/wish-products")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("3. 관심 상품 삭제")
    void deleteTest() throws Exception {
        // given
        Long wishProductId = 1L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/authz/wish-products/" + wishProductId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        User user = userRepository.save(newUser("신짱구", "test@naver.com"));

        Product product = productRepository.save(
                newMockProduct(1L, "pants", ProductStatus.IN_STOCK, user.getId()));

        wishProductRepository.save(newMockWishProduct(1L, user.getId(), product));

        em.clear();
    }
}