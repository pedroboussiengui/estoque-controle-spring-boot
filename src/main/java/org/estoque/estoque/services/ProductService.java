package org.estoque.estoque.services;

import org.estoque.estoque.dto.ProductDTO;
import org.estoque.estoque.exception.ProductNotFoundException;
import org.estoque.estoque.models.Product;
import org.estoque.estoque.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product add(Product product) {
        product.setCreatedAt(Timestamp.from(ZonedDateTime.now().toInstant()));
        return repository.save(product);
    }

    public Page<Product> list(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return repository.findAll(paging);
    }

    public Product find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("No product with %d was found.", id)));
    }

    public void remove(Long id) {
        Product product = find(id);
        repository.delete(product);
    }

    public Product update(Long id, ProductDTO productDTO) {
        Product product = find(id);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setUpdatedAt(Timestamp.from(ZonedDateTime.now().toInstant()));
        return repository.save(product);
    }
}
