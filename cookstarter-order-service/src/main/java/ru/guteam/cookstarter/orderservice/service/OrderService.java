package ru.guteam.cookstarter.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.guteam.cookstarter.api.dto.orderservice.OrderDto;
import ru.guteam.cookstarter.orderservice.aspect.annotation.CheckIdIsNotNull;
import ru.guteam.cookstarter.orderservice.exception.OrderProcessingException;
import ru.guteam.cookstarter.orderservice.model.Order;
import ru.guteam.cookstarter.orderservice.model.OrderItem;
import ru.guteam.cookstarter.orderservice.repository.OrderItemRepository;
import ru.guteam.cookstarter.orderservice.repository.OrderRepository;
import ru.guteam.cookstarter.orderservice.service.util.OrderMapping;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.guteam.cookstarter.api.enums.OrderStatus.SAVED;
import static ru.guteam.cookstarter.orderservice.service.util.OrderMapping.toDto;
import static ru.guteam.cookstarter.orderservice.service.util.OrderMapping.toOrder;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Long insert(OrderDto orderRequest) {
        Order order = toOrder(orderRequest);
        filterZeroQuantity(order);
        if (order.getDishes().isEmpty()) {
            throw new OrderProcessingException("Список блюд пуст или количество равно нулю");
        }
        order.setDateCreated(OffsetDateTime.now());
        order.setStatus(SAVED);
        return orderRepository.save(order).getId();
    }

    private void filterZeroQuantity(Order order) {
        order.setDishes(order.getDishes().entrySet().stream()
                .filter(entry -> entry.getValue().getQuantity() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @CheckIdIsNotNull
    @Transactional
    public void update(OrderDto orderRequest) {
        Long id = orderRequest.getId();
        Order order = getOrderByIdOrThrowException(id);
        Order orderNew = toOrder(orderRequest);
        order.setDishes(joinDishMaps(order.getDishes(), orderNew.getDishes()));
        deleteZeroQuantityItems(order);
        filterZeroQuantity(order);
        if (order.getDishes().isEmpty()) {
            deleteOrder(id);
        } else {
            order.setOrderSum(orderNew.getOrderSum());
            orderRepository.save(order);
        }
    }

    private void deleteZeroQuantityItems(Order order) {
        order.getDishes().entrySet().stream()
                .filter(entry -> entry.getValue().getId() != null && entry.getValue().getQuantity() == 0)
                .forEach(entry -> orderItemRepository.delete(entry.getValue()));
    }

    private Map<Long, OrderItem> joinDishMaps(Map<Long, OrderItem> mapOld, Map<Long, OrderItem> mapNew) {
        mapOld.forEach((key, value) -> value.setQuantity(0));
        for (Map.Entry<Long, OrderItem> entry : mapNew.entrySet()) {
            Long id = entry.getKey();
            OrderItem dish = entry.getValue();
            mapOld.compute(id, (itemId, item) -> {
                if (item == null) {
                    return dish;
                }
                item.setQuantity(dish.getQuantity());
                return item;
            });
        }
        return mapOld;
    }

    @CheckIdIsNotNull
    public OrderDto getById(Long id) {
        return toDto(getOrderByIdOrThrowException(id));
    }

    private Order getOrderByIdOrThrowException(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderProcessingException("Не найден заказ id = " + id));
    }

    @CheckIdIsNotNull
    @Transactional
    public void deleteOrder(Long id) {
        getOrderByIdOrThrowException(id);
        orderRepository.deleteById(id);
    }

    @CheckIdIsNotNull
    public void deleteItem(Long id) {
        OrderItem item = getItemByIdOrThrowException(id);
        orderItemRepository.deleteById(id);
        orderRepository.findById(item.getOrder().getId()).ifPresent(entity -> {
            if (entity.getDishes().isEmpty()) {
                orderRepository.delete(entity);
            }
        });
    }

    private OrderItem getItemByIdOrThrowException(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderProcessingException("Не найдено в заказах блюда id = " + id));
    }

    @CheckIdIsNotNull
    public List<OrderDto> getAllByCustomerId(Long id) {
        return orderRepository.findAllByCustomerId(id).stream()
                .map(OrderMapping::toDto)
                .collect(Collectors.toList());
    }

    @CheckIdIsNotNull
    public List<OrderDto> getAllByRestaurantId(Long id) {
        return orderRepository.findAllByRestaurantId(id).stream()
                .map(OrderMapping::toDto)
                .collect(Collectors.toList());
    }
}
