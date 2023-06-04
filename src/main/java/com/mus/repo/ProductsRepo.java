package com.mus.repo;

import com.mus.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {
    List<Products> findAllByNameContainingAndCategory_Name(String name, String categoryName);
}
