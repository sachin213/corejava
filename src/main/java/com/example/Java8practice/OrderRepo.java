package com.example.Java8practice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
}
