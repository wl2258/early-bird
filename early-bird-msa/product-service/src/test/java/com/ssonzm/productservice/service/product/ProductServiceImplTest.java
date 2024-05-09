package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.productservice.common.util.DummyUtil;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductRepository;
import com.ssonzm.productservice.domain.product.ProductStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;

@SpringBootTest
class ProductServiceImplTest extends DummyUtil {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedissonLockProductFacade productFacade;

    @BeforeEach
    public void before() {
        productRepository.saveAndFlush(newMockProduct(1L, "product", ProductStatus.IN_STOCK, 1L)); // 재고 만개
        productRepository.saveAndFlush(newMockProduct(2L, "product", ProductStatus.SOLD_OUT, 1L,
                0, LocalDateTime.now())); // 품절
        productRepository.saveAndFlush(newMockProduct(3L, "product", ProductStatus.SOLD_OUT, 1L,
                0, LocalDateTime.now().plusDays(3))); // 오픈 예정 상품
        productRepository.saveAndFlush(newMockProduct(4L, "product", ProductStatus.IN_STOCK, 1L));
    }

    @AfterEach
    public void after() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 100개 주문")
    public void decreaseQuantityTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Product beforeProduct = newMockProduct(1L, "product", ProductStatus.IN_STOCK, 1L);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    OrderProductUpdateReqDto orderProductDto = new OrderProductUpdateReqDto(beforeProduct.getId(), 1);
                    productFacade.decreaseProductQuantity(1L, orderProductDto);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Product afterProduct = productRepository.findById(beforeProduct.getId()).orElseThrow();
        Assertions.assertEquals(afterProduct.getQuantity(), beforeProduct.getQuantity() - 100);
    }

    @Test
    @DisplayName("품절 상품 주문 시 실패")
    public void decreaseQuantity_품절테스트() {
        OrderProductUpdateReqDto orderProductDto = new OrderProductUpdateReqDto(2L, 1);

        Assertions.assertThrows(CommonBadRequestException.class, () -> {
            productFacade.decreaseProductQuantity(1L, orderProductDto);
        }, "failOrder");
    }

    @Test
    @DisplayName("오픈 예정 상품 주문 시 실패")
    public void decreaseQuantity_날짜테스트() {
        OrderProductUpdateReqDto orderProductDto = new OrderProductUpdateReqDto(3L, 1);

        Assertions.assertThrows(CommonBadRequestException.class, () -> {
            productFacade.decreaseProductQuantity(1L, orderProductDto);
        }, "failOrder");
    }

    @Test
    @DisplayName("동시에 100개 취소 (재고 증가)")
    public void increaseTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Product beforeProduct = newMockProduct(4L, "product", ProductStatus.IN_STOCK, 1L);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productFacade.increaseProductQuantity(beforeProduct.getId(), 1);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Product afterProduct = productRepository.findById(beforeProduct.getId()).orElseThrow();
        Assertions.assertEquals(afterProduct.getQuantity(), beforeProduct.getQuantity() + 100);
    }
}