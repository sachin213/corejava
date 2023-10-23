package com.example.Java8practice;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    @With
    private Double price;


    @ManyToMany(mappedBy = "products")
    @ToString.Exclude
    private Set<Order> orders;


}