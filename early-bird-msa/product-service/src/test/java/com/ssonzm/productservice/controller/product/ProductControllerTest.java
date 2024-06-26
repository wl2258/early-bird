package com.ssonzm.productservice.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import com.ssonzm.productservice.common.util.DummyUtil;
import com.ssonzm.productservice.domain.product.ProductRepository;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql("classpath:teardown.sql")
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WithUserDetails(value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class ProductControllerTest extends DummyUtil {

    @Autowired
    EntityManager em;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Mock
    private MessageSource messageSource;
/*    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;*/
    @Autowired
    private ProductRepository productRepository;
/*    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;*/

    @BeforeEach
    void init() {
        dataSetting();
        when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("1234");
    }

    @Test
    @DisplayName("1. 상품 등록")
    void saveProductTest() throws Exception {
        // given
        ProductSaveReqDto productSaveReqDto = new ProductSaveReqDto(
                        "product1", "FASHION", "description", 1000, 20000,
                LocalDateTime.now());
        String requestBody = om.writeValueAsString(productSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/authz/products")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("2. 상품 조회")
    void getProductListTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("3. 아이디로 상품 상세 조회")
    void getProductDetailsTest() throws Exception {
        // given
        Long productId = 1L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
/*        User user = userRepository.save(newUser("신짱구", "test@naver.com"));

        Product product = productRepository.save(
                newMockProduct(1L, "pants", ProductStatus.IN_STOCK, user.getId()));

        Order order = orderRepository.save(newMockOrder(1L, user.getId()));

        OrderProduct orderProduct1 = orderProductRepository.save(
                newMockOrderProduct(1L, user.getId(), product.getId(), order, OrderStatus.CREATED));
        OrderProduct orderProduct2 = orderProductRepository.save(
                newMockOrderProduct(2L, user.getId(), product.getId(), order, OrderStatus.CREATED));

        deliveryRepository.save(newMockDelivery(1L, orderProduct1.getId(), DeliveryStatus.READY_FOR_SHIPMENT));
        deliveryRepository.save(newMockDelivery(2L, orderProduct2.getId(), DeliveryStatus.SHIPPED));

        em.clear();*/
    }
}