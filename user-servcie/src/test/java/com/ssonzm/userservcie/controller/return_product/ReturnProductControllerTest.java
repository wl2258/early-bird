package com.ssonzm.userservcie.controller.return_product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.userservcie.common.util.DummyUtil;
import com.ssonzm.userservcie.domain.delivery.DeliveryRepository;
import com.ssonzm.userservcie.domain.delivery.DeliveryStatus;
import com.ssonzm.userservcie.domain.order.Order;
import com.ssonzm.userservcie.domain.order.OrderRepository;
import com.ssonzm.userservcie.domain.order_product.OrderProduct;
import com.ssonzm.userservcie.domain.order_product.OrderProductRepository;
import com.ssonzm.userservcie.domain.order_product.OrderStatus;
import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.domain.product.ProductRepository;
import com.ssonzm.userservcie.domain.product.ProductStatus;
import com.ssonzm.userservcie.domain.return_product.ReturnProductRepository;
import com.ssonzm.userservcie.domain.user.User;
import com.ssonzm.userservcie.domain.user.UserRepository;
import com.ssonzm.userservcie.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;
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
class ReturnProductControllerTest extends DummyUtil {

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
    private DeliveryRepository deliveryRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @BeforeEach
    void init() {
        dataSetting();
        when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("1234");
    }

    @Test
    @DisplayName("1. 반품 등록")
    void saveReturnTest() throws Exception {
        // given
        Long orderProductId = 2L;
        String reason = "Size miss";
        ReturnProductSaveReqDto returnProductSaveReqDto = new ReturnProductSaveReqDto(orderProductId, reason);
        String requestBody = om.writeValueAsString(returnProductSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/authz/returns")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("2. 반품 등록 실패 - 배송 완료되지 않은 상품")
    void saveReturnFailTest() throws Exception {
        // given
        Long orderProductId = 1L;
        String reason = "Size miss";
        ReturnProductSaveReqDto returnProductSaveReqDto = new ReturnProductSaveReqDto(orderProductId, reason);
        String requestBody = om.writeValueAsString(returnProductSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/authz/returns")
                .content(requestBody)
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
        deliveryRepository.save(newMockDelivery(2L, orderProduct2.getId(), DeliveryStatus.DELIVERED));

        em.clear();
    }
}