package com.ssonzm.orderservice.controller.order_product;

import com.ssonzm.orderservice.common.util.DummyUtil;
import com.ssonzm.orderservice.domain.delivery.DeliveryRepository;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order.OrderRepository;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderProductRepository;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;
import com.ssonzm.orderservice.domain.product.Product;
import com.ssonzm.orderservice.domain.product.ProductRepository;
import com.ssonzm.orderservice.domain.product.ProductStatus;
import com.ssonzm.orderservice.domain.user.User;
import com.ssonzm.orderservice.domain.user.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql("classpath:teardown.sql")
@TestMethodOrder(value = MethodOrderer.DisplayName.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WithUserDetails(value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class OrderProductControllerTest extends DummyUtil {

    @Autowired
    EntityManager em;
    @Autowired
    private MockMvc mvc;
    @Mock
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @BeforeEach
    void init() {
        dataSetting();
        when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("1234");
    }

    @Test
    @DisplayName("1. 주문 취소")
    void cancelOrderTest() throws Exception {
        // given
        Long orderProductId = 1L;

        // when
        ResultActions resultActions = mvc.perform(post("/api/authz/orders/" + orderProductId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("2. 배송 중인 상품은 주문 취소 불가")
    void cancelOrderFailTest() throws Exception {
        // given
        Long orderProductId = 2L;

        // when
        ResultActions resultActions = mvc.perform(post("/api/authz/orders/" + orderProductId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        User user = userRepository.save(newUser("신짱구", "test@naver.com"));

        Product product = productRepository.save(
                newMockProduct(1L, "pants", ProductStatus.IN_STOCK, user.getId()));

        Order order = orderRepository.save(newMockOrder(1L, user.getId()));

        OrderProduct orderProduct1 = orderProductRepository.save(
                newMockOrderProduct(1L, user.getId(), product.getId(), order, OrderStatus.CREATED));
        OrderProduct orderProduct2 = orderProductRepository.save(
                newMockOrderProduct(2L, user.getId(), product.getId(), order, OrderStatus.CREATED));

        deliveryRepository.save(newMockDelivery(1L, orderProduct1.getId(), DeliveryStatus.READY_FOR_SHIPMENT));
        deliveryRepository.save(newMockDelivery(2L, orderProduct2.getId(), DeliveryStatus.SHIPPED));

        em.clear();
    }
}