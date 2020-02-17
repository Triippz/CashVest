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

package com.triippztech.cashvest.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    public static long DEFAULT_ID = 1L;
    public static String DEFAULT_NAME = "Tesla Inc.";
    public static String DEFAULT_SYMBOL = "TSLA";
    public static String DEFAULT_CURRENCY = "USD";
    public static String DEFAULT_EXCHANGE = "NYSE";
    public static BigDecimal DEFAULT_VALUE = new BigDecimal("1.00");
    public static BigDecimal DEFAULT_PERCENT_CHANGE = new BigDecimal(".05");

    private Transaction transaction;
    private Watchlist watchlist;
    private Portfolio portfolio;
    private PriceAlert priceAlert;
    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock(
                DEFAULT_NAME,
                DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        stock.setId(DEFAULT_ID);

        transaction = new Transaction(
               TransactionTest.DEFAULT_TRANSACTION_TYPE,
               TransactionTest.DEFAULT_QUANTITY,
               TransactionTest.DEFAULT_PRICE_PER,
               TransactionTest.DEFAULT_TOTAL_PRICE,
               TransactionTest.DEFAULT_LOCAL_DATE
        );
        transaction.setId(TransactionTest.DEFAULT_ID);

        watchlist = new Watchlist(
                WatchlistTest.DEFAULT_LIST_NAME
        );
        watchlist.setId(DEFAULT_ID);

        portfolio = new Portfolio(
                PortfolioTest.DEFAULT_ID,
                PortfolioTest.DEFAULT_NAME,
                PortfolioTest.DEFAULT_DESCRIPTION,
                PortfolioTest.DEFAULT_TOTAL_VALUE
        );
        portfolio.addStock(stock);

        priceAlert = new PriceAlert(
                PriceAlertTest.DEFAULT_ALERT_METHOD,
                PriceAlertTest.DEFAULT_ALERT_TYPE_THRESH,
                PriceAlertTest.DEFAULT_PRICE,
                PriceAlertTest.DEFAULT_CURRENT_PRICE
        );
        priceAlert.setId(DEFAULT_ID);

        stock.addWatchlist(watchlist);
        stock.addTransaction(transaction);
        stock.addPortfolio(portfolio);
        stock.addPriceAlert(priceAlert);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void name() {
        Stock instance = stock.name(DEFAULT_NAME);
        assertEquals(DEFAULT_NAME, instance.getName());
    }

    @Test
    void symbol() {
        Stock instance = stock.symbol(DEFAULT_SYMBOL);
        assertEquals(DEFAULT_SYMBOL, instance.getSymbol());
    }

    @Test
    void currency() {
        Stock instance = stock.currency(DEFAULT_CURRENCY);
        assertEquals(DEFAULT_CURRENCY, instance.getCurrency());
    }

    @Test
    void exchange() {
        Stock instance = stock.exchange(DEFAULT_EXCHANGE);
        assertEquals(DEFAULT_EXCHANGE, instance.getExchange());
    }

    @Test
    void transactions() {
        Set<Transaction> transactions = new HashSet<>();
        transactions.add(transaction);
        Stock instance = stock.transactions(transactions);
        assertFalse(instance.getTransactions().isEmpty());
    }

    @Test
    void addTransaction() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.addTransaction(transaction);
        assertFalse(instance.getTransactions().isEmpty());
    }

//    @Test
//    void removeTransaction() {
//        Stock instance = new Stock(
//                StockTest.DEFAULT_NAME,
//                StockTest.DEFAULT_SYMBOL,
//                DEFAULT_CURRENCY,
//                DEFAULT_EXCHANGE,
//                DEFAULT_VALUE,
//                DEFAULT_PERCENT_CHANGE
//        );
//        instance.addTransaction(transaction);
//        instance.removeTransaction(transaction);
//        assertTrue(instance.getTransactions().isEmpty());
//    }

    @Test
    void watchlists() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        Set<Watchlist> watchlists = new HashSet<>();
        watchlists.add(watchlist);
        Stock newInstance = instance.watchlists(watchlists);
        assertFalse(newInstance.getWatchlists().isEmpty());
    }

    @Test
    void portfoliosIn() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        Set<Portfolio> portfolios = new HashSet<>();
        portfolios.add(portfolio);
        Stock newInstance = instance.portfoliosIn(portfolios);
        assertFalse(newInstance.getPortfoliosIn().isEmpty());
    }

    @Test
    void addPortfolio() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.addPortfolio(portfolio);
        assertFalse(instance.getPortfoliosIn().isEmpty());
    }

    @Test
    void removePortfolio() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.addPortfolio(portfolio);
        instance.removePortfolio(portfolio);
        assertTrue(instance.getPortfoliosIn().isEmpty());
    }

    @Test
    void priceAlerts() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        Set<PriceAlert> priceAlerts = new HashSet<>();
        priceAlerts.add(priceAlert);
        instance.priceAlerts(priceAlerts);
        assertFalse(instance.getPriceAlerts().isEmpty());
    }

    @Test
    void testAddPriceAlert() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.addPriceAlert(priceAlert);
        assertFalse(instance.getPriceAlerts().isEmpty());
    }

    @Test
    void testRemovePriceAlert() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.addPriceAlert(priceAlert);
        instance.removePriceAlert(priceAlert);
        assertTrue(instance.getPriceAlerts().isEmpty());
    }

    @Test
    void addWatchlist() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.addWatchlist(watchlist);
        assertFalse(instance.getWatchlists().isEmpty());
    }

    @Test
    void removeWatchlist() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.addWatchlist(watchlist);
        instance.removeWatchlist(watchlist);
        assertTrue(instance.getWatchlists().isEmpty());
    }

    @Test
    void setId() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.setId(DEFAULT_ID);
        assertEquals(DEFAULT_ID, instance.getId());
    }

    @Test
    void setCurrency() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                "",
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.setCurrency(DEFAULT_CURRENCY);
        assertEquals(DEFAULT_CURRENCY, instance.getCurrency());
    }

    @Test
    void setExchange() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                "",
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );
        instance.setExchange(DEFAULT_EXCHANGE);
        assertEquals(DEFAULT_EXCHANGE, instance.getExchange());
    }

    @Test
    void setValue() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                new BigDecimal("3393.00"),
                DEFAULT_PERCENT_CHANGE
        );
        instance.setValue(DEFAULT_VALUE);
        assertEquals(DEFAULT_VALUE, instance.getValue());
    }

    @Test
    void setPercentChange() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                new BigDecimal("3393.00")
        );
        instance.setPercentChange(DEFAULT_PERCENT_CHANGE);
        assertEquals(DEFAULT_PERCENT_CHANGE, instance.getPercentChange());
    }

    @Test
    void setTransactions() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );

        Set<Transaction> transactions = new HashSet<>();
        transactions.add(transaction);
        instance.setTransactions(transactions);

        assertFalse(instance.getTransactions().isEmpty());
    }

    @Test
    void setWatchlists() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );

        Set<Watchlist> watchlists = new HashSet<>();
        watchlists.add(watchlist);
        instance.setWatchlists(watchlists);

        assertFalse(instance.getWatchlists().isEmpty());
    }

    @Test
    void setPortfoliosIn() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );

        Set<Portfolio> portfoliosIn = new HashSet<>();
        portfoliosIn.add(portfolio);
        instance.setPortfoliosIn(portfoliosIn);

        assertFalse(instance.getPortfoliosIn().isEmpty());
    }

    @Test
    void setPriceAlerts() {
        Stock instance = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                DEFAULT_CURRENCY,
                DEFAULT_EXCHANGE,
                DEFAULT_VALUE,
                DEFAULT_PERCENT_CHANGE
        );

        Set<PriceAlert> priceAlerts = new HashSet<>();
        priceAlerts.add(priceAlert);
        instance.setPriceAlerts(priceAlerts);

        assertFalse(instance.getPriceAlerts().isEmpty());
    }

    @Test
    void getId() {
        assertEquals(DEFAULT_ID, stock.getId());
    }

    @Test
    void getCurrency() {
        assertEquals(DEFAULT_CURRENCY, stock.getCurrency());
    }

    @Test
    void getExchange() {
        assertEquals(DEFAULT_EXCHANGE, stock.getExchange());
    }

    @Test
    void getValue() {
        assertEquals(DEFAULT_VALUE, stock.getValue());
    }

    @Test
    void getPercentChange() {
        assertEquals(DEFAULT_PERCENT_CHANGE, stock.getPercentChange());
    }

    @Test
    void getTransactions() {
        assertFalse(stock.getTransactions().isEmpty());
    }

    @Test
    void getWatchlists() {
        assertFalse(stock.getWatchlists().isEmpty());
    }

    @Test
    void getPortfoliosIn() {
        assertFalse(stock.getPortfoliosIn().isEmpty());
    }

    @Test
    void getPriceAlerts() {
        assertFalse(stock.getPriceAlerts().isEmpty());
    }
}