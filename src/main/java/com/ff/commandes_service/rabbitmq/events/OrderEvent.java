package com.ff.commandes_service.rabbitmq.events;

import com.ff.commandes_service.entity.OrderStatus;
import com.ff.commandes_service.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private int quantity;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime pendingDate;
    private LocalDateTime validatedDate;
    private LocalDateTime cancelledDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private LocalDateTime returnedDate;
    private LocalDateTime refundedDate;
    private LocalDateTime orderDate;

    public OrderEvent(Orders orderToSave) {
        this.orderId = orderToSave.getId();
        this.userId = orderToSave.getUserId();
        this.quantity = orderToSave.getQuantity();
        this.totalPrice = orderToSave.getTotalPrice();
        this.orderStatus = orderToSave.getOrderStatus();
        this.pendingDate = orderToSave.getPendingDate();
        this.validatedDate = orderToSave.getValidatedDate();
        this.cancelledDate = orderToSave.getCancelledDate();
        this.shippedDate = orderToSave.getShippedDate();
        this.deliveredDate = orderToSave.getDeliveredDate();
        this.returnedDate = orderToSave.getReturnedDate();
        this.refundedDate = orderToSave.getRefundedDate();
        this.orderDate = orderToSave.getOrderDate();
    }
}
