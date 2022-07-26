package com.example.houserenting.repository;

import com.example.houserenting.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value="select * from orderr where customer_id = :id and status = 1", nativeQuery = true)
    Iterable<Order> findAllByCustomerId(@Param("id") Long id);
}
