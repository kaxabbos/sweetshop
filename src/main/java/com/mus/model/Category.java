package com.mus.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Products> products;

    public Category(String name) {
        this.name = name;
        products = new ArrayList<>();
    }

    public void addProduct(Products product) {
        this.products.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Products product) {
        products.remove(product);
        product.setCategory(null);
    }
}
