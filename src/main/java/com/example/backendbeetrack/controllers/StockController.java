package com.example.backendbeetrack.controllers;

import com.example.backendbeetrack.entities.Stock;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.services.StockService;
import com.example.backendbeetrack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
@PreAuthorize("hasAnyRole('ADMIN', 'APICULTEUR')")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Optional<Stock> stock = stockService.getStockById(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);

        stock.setUser(user.get());
        return ResponseEntity.ok(stockService.createStock(stock));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock stockDetails) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(email);
        stockDetails.setUser(user.get());
        Stock updatedStock = stockService.updateStock(id, stockDetails);
        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}