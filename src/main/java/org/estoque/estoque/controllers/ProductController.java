package org.estoque.estoque.controllers;

import jakarta.validation.Valid;
import org.estoque.estoque.dto.ProductDTO;
import org.estoque.estoque.models.Product;
import org.estoque.estoque.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    private final ModelMapper mapper;

    public ProductController(ProductService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public Product register(@RequestBody @Valid ProductDTO productDTO) {
        Product productEntity = this.mapper.map(productDTO, Product.class);
        return service.add(productEntity);
    }
}
