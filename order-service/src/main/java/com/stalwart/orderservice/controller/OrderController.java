package com.stalwart.orderservice.controller;

import com.stalwart.orderservice.dto.OrderRequest;
import com.stalwart.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/order")
public class OrderController {

    public final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest request){

        return CompletableFuture.supplyAsync(()->orderService.placeOrder(request));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest request, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Something went wrong. Please try after sometime!");
    }
}
