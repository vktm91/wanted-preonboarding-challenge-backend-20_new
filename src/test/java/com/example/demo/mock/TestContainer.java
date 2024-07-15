package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.order.controller.port.OrderHistoryReadService;
import com.example.demo.order.controller.port.OrderHistoryService;
import com.example.demo.order.service.OrderHistoryReadServiceImpl;
import com.example.demo.order.service.OrderHistoryServiceImpl;
import com.example.demo.order.service.port.OrderHistoryRepository;
import com.example.demo.product.controller.ProductController;
import com.example.demo.product.controller.port.ProductService;
import com.example.demo.product.service.ProductServiceImpl;
import com.example.demo.product.service.port.ProductRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
public class TestContainer {
    public final UserRepository userRepository;
    public final ProductRepository productRepository;
    public final OrderHistoryRepository orderHistoryRepository;
    public final UserCreateController userCreateController;
    public final UserController userController;
    public final ProductController productController;
    public final UserService userService;
    public final ProductService productService;
    public final OrderHistoryReadService orderHistoryReadService;
    public final OrderHistoryService orderHistoryService;
    private UriComponentsBuilder uriComponentsBuilder;

    @Builder
    public TestContainer(ClockHolder clockHolder) {
        this.uriComponentsBuilder = UriComponentsBuilder.newInstance();

        this.userRepository = new FakeUserRepository();
        this.productRepository = new FakeProductRepository();
        this.orderHistoryRepository = new FakeOrderHistoryRepository();

//        OrderHistoryServiceImpl orderHistoryService = OrderHistoryServiceImpl.builder()
//                .orderHistoryRepository(orderHistoryRepository)
//                .userRepository(userRepository)
//                .productRepository(productRepository)
//                .build();

        OrderHistoryReadServiceImpl orderHistoryReadService = OrderHistoryReadServiceImpl.builder()
                .orderHistoryRepository(orderHistoryRepository)
                .userRepository(userRepository)
                .productRepository(productRepository)
                .clockHolder(clockHolder)
                .build();

        this.userService = UserServiceImpl.builder()
                .userRepository(this.userRepository)
                .build();

        this.productService = ProductServiceImpl.builder()
                .productRepository(this.productRepository)
                .userRepository(this.userRepository)
                .orderHistoryReadService(orderHistoryReadService)
                .clockHolder(clockHolder)
                .build();

        this.orderHistoryService = OrderHistoryServiceImpl.builder()
                .orderHistoryRepository(this.orderHistoryRepository)
                .userRepository(this.userRepository)
                .productRepository(this.productRepository)
                .clockHolder(clockHolder)
                .build();

        this.orderHistoryReadService = OrderHistoryReadServiceImpl.builder()
                .orderHistoryRepository(this.orderHistoryRepository)
                .userRepository(this.userRepository)
                .productRepository(this.productRepository)
                .clockHolder(clockHolder)
                .build();

        this.userCreateController = UserCreateController.builder()
                .userService(this.userService)
                .build();

        this.userController = UserController.builder()
                .userService(this.userService)
                .build();

        this.productController = ProductController.builder()
                .productService(productService)
                .build();
    }

    public URI createUri(String path, Long id) {
        URI uri = uriComponentsBuilder.path(path).buildAndExpand(id).toUri();
        log.info("!!!!! createUri uri: {}", uri);
        return uri;
    }

    public void initializeRequestContext(String uri, int port, String host, String scheme) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        request.setServerPort(port);
        request.setServerName(host);
        request.setScheme(scheme);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        this.uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme(scheme).host(host).port(port);
    }

    public void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }
}
