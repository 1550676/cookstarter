package ru.guteam.cookstarter.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.guteam.cookstarter.api.dto.StatusResponse;
import ru.guteam.cookstarter.api.dto.orderservice.OrderDto;
import ru.guteam.cookstarter.orderservice.controller.util.StatusResponseBuilder;
import ru.guteam.cookstarter.orderservice.service.OrderService;

import javax.validation.Valid;
import java.util.List;

import static ru.guteam.cookstarter.api.dto.RequestMessageHeaders.JWT_TOKEN_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<StatusResponse> addOrder(@Valid @RequestBody OrderDto orderRequest,
                                                   @RequestHeader(JWT_TOKEN_HEADER) String token) {
        Long id = orderService.insert(orderRequest);
        return StatusResponseBuilder.okWithId(String.valueOf(id));
    }

    @CrossOrigin
    @PostMapping("/update")
    public ResponseEntity<StatusResponse> updateOrder(@Valid @RequestBody OrderDto orderRequest,
                                                      @RequestHeader(JWT_TOKEN_HEADER) String token) {
        orderService.update(orderRequest);
        return StatusResponseBuilder.ok();
    }

    @CrossOrigin
    @GetMapping("/delete/{id}")
    public ResponseEntity<StatusResponse> deleteOrder(@PathVariable("id") Long id,
                                                      @RequestHeader(JWT_TOKEN_HEADER) String token) {
        orderService.deleteOrder(id);
        return StatusResponseBuilder.ok();
    }

    @CrossOrigin
    @GetMapping("/delete/item/{id}")
    public ResponseEntity<StatusResponse> deleteItem(@PathVariable("id") Long id,
                                                     @RequestHeader(JWT_TOKEN_HEADER) String token) {
        orderService.deleteItem(id);
        return StatusResponseBuilder.ok();
    }

    @CrossOrigin
    @GetMapping("/get/{id}")
    public OrderDto getById(@PathVariable("id") Long id,
                            @RequestHeader(JWT_TOKEN_HEADER) String token) {
        return orderService.getById(id);
    }

    @CrossOrigin
    @GetMapping("/get/customer/{id}")
    public List<OrderDto> getAllByCustomerId(@PathVariable("id") Long id,
                                             @RequestHeader(JWT_TOKEN_HEADER) String token) {
        return orderService.getAllByCustomerId(id);
    }

    @CrossOrigin
    @GetMapping("/get/restaurant/{id}")
    public List<OrderDto> getAllByRestaurantId(@PathVariable("id") Long id,
                                               @RequestHeader(JWT_TOKEN_HEADER) String token) {
        return orderService.getAllByRestaurantId(id);
    }
}