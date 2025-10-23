package com.example.appjsf.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Product product;
    @Setter
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
