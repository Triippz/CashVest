/*
 * Copyright 2020 Mark Tripoli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.triippztech.cashvest.service;

import com.triippztech.cashvest.domain.Stock;
import com.triippztech.cashvest.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {
    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock create(Stock stock) {
        log.debug("Request to create new Stock: {}", stock);

        Optional<Stock> foundStock = stockRepository.findBySymbol(stock.getSymbol());
        return foundStock.orElseGet(() -> stockRepository.save(stock));
    }

    public Stock getStock(Stock stock) {
        log.debug("Request to get Stock: {}", stock.getSymbol());

        Optional<Stock> foundStock = stockRepository.findBySymbol(stock.getSymbol());
        return foundStock.orElseGet(() -> create(stock));
    }

    public Stock save(Stock stock) {
        log.debug("Request to save Stock: {}", stock);
        return stockRepository.save(stock);
    }

    public void delete(Stock stock) {
        log.debug("Request to delete Stock:{}", stock);
        stockRepository.deleteById(stock.getId());
    }
}
