package org.estoque.estoque.services;

import org.estoque.estoque.dto.ProductRequestDTO;
import org.estoque.estoque.exception.MessagesException;
import org.estoque.estoque.exception.ProductNotFoundException;
import org.estoque.estoque.models.Product;
import org.estoque.estoque.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Objects;

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

    public Page<Product> list(int page, int size, String sort) {
        String[] sortingFields = sort.split(" ");

        if (sortingFields.length != 2) {
            throw new InvalidParameterException(MessagesException.INVALID_PARAMETERS);
        }

        String field = sortingFields[0];
        String dir = sortingFields[1];

        Sort.Direction direction;
        if (Objects.equals(dir, "DESC")) {
            direction = Sort.Direction.DESC;
        } else if (Objects.equals(dir, "ASC")) {
            direction = Sort.Direction.ASC;
        } else {
            throw new InvalidParameterException(MessagesException.INVALID_PARAMETERS);
        }

        Sort sorting = Sort.by(direction, field);

        Pageable paging = PageRequest.of(page, size, sorting);
        return repository.findAll(paging);
    }

    public Product find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(MessagesException.PRODUCT_NOT_FOUND));
    }

    public void remove(Long id) {
        Product product = find(id);
        repository.delete(product);
    }

    public Product update(Long id, ProductRequestDTO productDTO) {
        Product product = find(id);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setUpdatedAt(Timestamp.from(ZonedDateTime.now().toInstant()));
        return repository.save(product);
    }
}
