package org.estoque.estoque.services;

import org.estoque.estoque.exception.ProductNotFoundException;
import org.estoque.estoque.models.Product;
import org.estoque.estoque.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product add(Product product) {
        return repository.save(product);
    }

    public List<Product> list() {
        return repository.findAll();
    }

    public Product find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("No product with %d was found.", id)));
    }

    public void remove(Long id) {
        Product product = find(id);
        repository.delete(product);
    }

}
