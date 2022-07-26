package com.example.houserenting.controller;

import com.example.houserenting.model.House;
import com.example.houserenting.model.Order;
import com.example.houserenting.service.HouseService;
import com.example.houserenting.service.impl.HouseServiceImpl;
import com.example.houserenting.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin("*")
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    HouseServiceImpl houseService;

    @GetMapping
    public ResponseEntity<Iterable<Order>> findAllOrder() {
        return new ResponseEntity<>(orderService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (!orderOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> save(@RequestBody Order order) {
        orderService.save(order);
        House house = houseService.findById(order.getHouse().getId()).get();
        house.setStatus(2);
        houseService.save(house);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/find-by-customer/{id}")
    public ResponseEntity<Iterable<Order>> findAllByCustomerId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderService.findAllByCustomerId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order) {
        Optional<Order> optionalOrder = orderService.findById(id);
        if (!optionalOrder.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        order.setId(optionalOrder.get().getId());
        orderService.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
