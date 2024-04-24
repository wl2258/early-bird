package com.ssonzm.userservcie.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.userservcie.common.util.DummyUtil;
import com.ssonzm.userservcie.domain.order.Order;
import com.ssonzm.userservcie.domain.order.OrderRepository;
import com.ssonzm.userservcie.domain.order_product.OrderProductRepository;
import com.ssonzm.userservcie.domain.order_product.OrderStatus;
import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.domain.product.ProductRepository;
import com.ssonzm.userservcie.domain.product.ProductStatus;
import com.ssonzm.userservcie.domain.user.User;
import com.ssonzm.userservcie.domain.user.UserRepository;
import com.ssonzm.userservcie.dto.order.OrderRequestDto;
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

import java.util.ArrayList;
import java.util.List;

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
class OrderControllerTest extends DummyUtil {

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
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @BeforeEach
    void init() {
        dataSetting();
        when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("1234");
    }

    @Test
    @DisplayName("1. 주문 정보 조회")
    void getOrderDetailsTest() throws Exception {
        // given
        Long orderId = 1L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/authz/orders/" + orderId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("2. 주문하기")
    void saveOrderTest() throws Exception {
        // given
        List<OrderRequestDto.OrderSaveReqDto> orderList = new ArrayList<>();
        Long productId = 1L;
        orderList.add(new OrderRequestDto.OrderSaveReqDto(productId, 1));
        String requestBody = om.writeValueAsString(orderList);

        // when
        ResultActions resultActions = mvc.perform(post("/api/authz/orders")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        User user = userRepository.save(newUser("신짱구", "test@naver.com"));
        Product product = productRepository.save(newMockProduct(1L, "pants", ProductStatus.IN_STOCK, user.getId()));
        Order order = orderRepository.save(newMockOrder(1L, user.getId()));
        orderProductRepository.save(newMockOrderProduct(1L, user.getId(), product.getId(), order, OrderStatus.CREATED));

        em.clear();
    }
}