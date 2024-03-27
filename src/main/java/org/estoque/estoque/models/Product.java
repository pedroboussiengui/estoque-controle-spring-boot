package org.estoque.estoque.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private Integer quantity;
}
