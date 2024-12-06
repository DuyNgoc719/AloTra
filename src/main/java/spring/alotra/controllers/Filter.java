package spring.alotra.controllers;

import lombok.Data;

@Data
public class Filter {
    private double price;

    public Filter(double price) {
        this.price = price;
    }

}
