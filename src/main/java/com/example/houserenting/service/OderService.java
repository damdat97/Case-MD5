package com.example.houserenting.service;

import com.example.houserenting.model.Category;
import com.example.houserenting.model.Order;
import org.springframework.data.repository.query.Param;

public interface OderService extends IService<Order>{
    Iterable<Order> findAllOrder();

    Iterable<Order> findAllByCustomerId( Long id);
}
