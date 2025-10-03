package com.example.backendbeetrack.services;

import com.example.backendbeetrack.entities.Stock;
import java.util.List;
import java.util.Optional;

public interface StockService {
    List<Stock> getAllStocks();
    Optional<Stock> getStockById(Long id);
    Stock createStock(Stock stock);
    Stock updateStock(Long id, Stock stockDetails);
    void deleteStock(Long id);
}