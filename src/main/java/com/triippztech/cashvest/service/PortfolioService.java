/*
 * Copyright 2019 Mark Tripoli
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

import com.triippztech.cashvest.domain.Portfolio;
import com.triippztech.cashvest.domain.Stock;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PortfolioService {
    private final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final PortfolioRepository portfolioRepository;
    private final IEXService iexService;

    public PortfolioService(PortfolioRepository portfolioRepository, IEXService iexService) {
        this.portfolioRepository = portfolioRepository;
        this.iexService = iexService;
    }

    public Portfolio save (Portfolio portfolio) {
        log.debug("Request to save Portfolio: {}", portfolio);
        return portfolioRepository.save(portfolio);
    }

    public Portfolio createPortfolio(Portfolio portfolio) throws Exception {
        log.debug("Request to create Portfolio: {}", portfolio);
        Optional<Portfolio> foundPortfolio =
                portfolioRepository.findByNameAndPortfolioUser(portfolio.getName(), portfolio.getPortfolioUser());
        if ( foundPortfolio.isPresent() )
            throw new Exception("Portfolio with that name already exists");

        portfolioRepository.save(portfolio);
        return portfolio;
    }

    public void delete(Portfolio portfolio) {
        log.debug("Request to delete Portfolio:{}", portfolio);
        portfolioRepository.deleteById(portfolio.getId());
    }

    public List<Portfolio> getUserPortfolios(User user) {
        log.debug("Request to get Portfolios for User:{} ", user);
        return portfolioRepository.findAllByPortfolioUser(user);
    }

    public BigDecimal getPortfolioValue(Portfolio portfolio) {
        log.debug("Request to get total value of Portfolio: {}", portfolio.getName());

        BigDecimal totalValue = new BigDecimal(0).setScale(2, RoundingMode.CEILING);
        for ( Stock stock : portfolio.getPortfolioStocks() ) {
            BigDecimal stockPrice = iexService.getStockPrice(stock.getSymbol());
            totalValue = totalValue.add(stockPrice);
        }
        return totalValue;
    }

    public BigDecimal getAllPortfolioValue(Set<Portfolio> portfolios) {
        log.debug("Request to get Total value of all portfolios");
        BigDecimal totalValue = new BigDecimal(0).setScale(2, RoundingMode.CEILING);

        for ( Portfolio portfolio : portfolios ) {
            BigDecimal value = getPortfolioValue(portfolio);
            totalValue = totalValue.add(value);
        }
        return totalValue;
    }
}
