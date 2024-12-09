package spring.alotra.entity;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Long orderDetailId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String productName;

    public OrderDetailDto(OrderDetail orderDetail, String productName) {
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.productId = orderDetail.getProductId();
        this.quantity = orderDetail.getQuantity();
        this.unitPrice = orderDetail.getUnitPrice();
        this.totalPrice = orderDetail.getTotalPrice();
        this.productName = productName;
    }
}
