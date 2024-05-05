package com.ssonzm.productservice.service.product;

import com.ssonzm.productservice.common.util.DummyUtil;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductRepository;
import com.ssonzm.productservice.domain.product.ProductStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeEach
    public void before() {
        productRepository.saveAndFlush(newMockProduct(1L, "product", ProductStatus.IN_STOCK, 1L)); // 재고 만개
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
                    productService.decreaseProductQuantity(beforeProduct, orderProductDto);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Product afterProduct = productRepository.findById(1L).orElseThrow();
        Assertions.assertEquals(afterProduct.getQuantity(), beforeProduct.getQuantity() - 100);
    }
}