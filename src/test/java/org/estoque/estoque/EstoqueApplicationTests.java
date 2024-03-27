package org.estoque.estoque;

import org.estoque.estoque.dto.ProductDTO;
import org.estoque.estoque.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EstoqueApplicationTests {

    ModelMapper mapper;

    @BeforeEach
    public void setup() {
        this.mapper = new ModelMapper();
    }


    @Test
    void contextLoads() {
    }

    @Test
    void whenMapProductDto_thenConvertToEntity() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Samsung S24");
        productDTO.setPrice(5000.0);
        productDTO.setQuantity(50);

        Product productEntity = this.mapper.map(productDTO, Product.class);

        System.out.println(productEntity.toString());

        assertEquals(productEntity.getName(), productDTO.getName());
        assertEquals(productEntity.getPrice(), productDTO.getPrice());
        assertEquals(productEntity.getQuantity(), productDTO.getQuantity());
    }
}
