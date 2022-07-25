package com.example.houserenting.controller;

import com.example.houserenting.model.House;
import com.example.houserenting.model.Order;
import com.example.houserenting.service.HouseService;
import com.example.houserenting.service.impl.HouseServiceImpl;
import com.example.houserenting.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Optional<Order> orderOptional = orderService.findById(id);
        if (!orderOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Date date1 = simpleDateFormat.parse(String.valueOf(orderOptional.get().getStartTime()));
        Date date2 = simpleDateFormat.parse(String.valueOf(LocalDate.now()));

        long getDiff = date1.getTime() - date2.getTime();
        long getDaysDiff = TimeUnit.DAYS.toDays(getDiff);
        if (getDaysDiff == 1 || getDaysDiff == 0) {
            orderOptional.get().setStatus(1);
        }
        if (getDaysDiff > 1) {
            orderOptional.get().setStatus(2);
            House house = houseService.findById(orderOptional.get().getHouse().getId()).get();
            house.setStatus(1);
            houseService.save(house);
        }
        orderService.save(orderOptional.get());
        return new ResponseEntity<>(orderOptional.get(), HttpStatus.NO_CONTENT);
    }
}
