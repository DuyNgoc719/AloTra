package spring.alotra.entity;

import lombok.Data;

@Data
public class CartDTO {
    private Long id;
    private Product product;
    private int quantity;

    public Double getTotalPrice() {
        return product.getPrices() * quantity;
    }
}
