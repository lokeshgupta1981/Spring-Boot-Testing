package com.example.springmongodbtestcontainers.repository;

import com.example.springmongodbtestcontainers.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
