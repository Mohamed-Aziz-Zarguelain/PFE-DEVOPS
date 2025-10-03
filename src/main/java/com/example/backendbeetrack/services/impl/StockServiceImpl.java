package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Stock;
import com.example.backendbeetrack.repositories.StockRepository;
import com.example.backendbeetrack.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    @Override
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(Long id, Stock stockDetails) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stock.setProduit(stockDetails.getProduit());
        stock.setQuantite(stockDetails.getQuantite());
        stock.setUnite(stockDetails.getUnite());
        stock.setUser(stockDetails.getUser());
        return stockRepository.save(stock);
    }

    @Override
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}