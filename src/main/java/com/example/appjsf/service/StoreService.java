package com.example.appjsf.service;

import com.example.appjsf.model.Product;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StoreService {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();

    public Product save(Product candidate) {
        if (candidate == null || candidate.getId() == null) {
            throw new IllegalArgumentException("El producto debe tener un identificador");
        }
        Product stored = copy(candidate);
        products.put(stored.getId(), stored);
        return copy(stored);
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id)).map(this::copy);
    }

    public List<Product> findAll() {
    return products.values().stream()
        .sorted(Comparator.comparing(Product::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
        .map(this::copy)
        .toList();
    }

    public void delete(Long id) {
        products.remove(id);
    }

    private Product copy(Product source) {
        if (source == null) {
            return null;
        }
        return new Product(source.getId(), source.getName());
    }
}
