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

class PortfolioTest {

    public static long DEFAULT_ID = 1L;
    public static String DEFAULT_NAME = "My Portfolio";
    public static String DEFAULT_DESCRIPTION = "Some description";
    public static BigDecimal DEFAULT_TOTAL_VALUE = new BigDecimal("100.00");

    private User user;
    private Portfolio portfolio;
    private Stock stock;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );

        user = new User(
                UserTest.DEFAULT_LOGIN,
                UserTest.DEFAULT_PASSWORD,
                UserTest.DEFAULT_FIRST_NAME,
                UserTest.DEFAULT_LAST_NAME,
                UserTest.DEFAULT_EMAIL
        );

        stock = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL
        );
        portfolio.addStock(stock);
        portfolio.setPortfolioUser(user);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void name() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );

        Portfolio newInstance = instance.name(DEFAULT_NAME);
        assertEquals(DEFAULT_NAME, newInstance.getName());
    }

    @Test
    void description() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );

        Portfolio newInstance = instance.description(DEFAULT_DESCRIPTION);
        assertEquals(DEFAULT_DESCRIPTION, newInstance.getDescription());
    }

    @Test
    void portfolioStocks() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );

        Portfolio newInstance = instance.portfolioStocks(portfolio.getPortfolioStocks());
        Boolean stocksExist = !newInstance.getPortfolioStocks().isEmpty();

        assertEquals(true, stocksExist);
    }

    @Test
    void addStock() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );

        instance.addStock(stock);
        assertFalse(instance.getPortfolioStocks().isEmpty());
    }

    @Test
    void removeStock() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );

        instance.addStock(stock);
        instance.removeStock(stock);
        assertTrue(instance.getPortfolioStocks().isEmpty());
    }

    @Test
    void setId() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );
        instance.setId(DEFAULT_ID);
        assertEquals(DEFAULT_ID, instance.getId());
    }

    @Test
    void setName() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                DEFAULT_DESCRIPTION,
                DEFAULT_TOTAL_VALUE
        );
        instance.setName(DEFAULT_NAME);
        assertEquals(DEFAULT_NAME, instance.getName());
    }

    @Test
    void setDescription() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                "",
                DEFAULT_TOTAL_VALUE
        );
        instance.setDescription(DEFAULT_DESCRIPTION);
        assertEquals(DEFAULT_DESCRIPTION, instance.getDescription());
    }

    @Test
    void setPortfolioStocks() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                "",
                DEFAULT_TOTAL_VALUE
        );
        instance.setPortfolioStocks(portfolio.getPortfolioStocks());
        assertFalse(instance.getPortfolioStocks().isEmpty());
    }

    @Test
    void setPortfolioUser() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                "",
                DEFAULT_TOTAL_VALUE
        );
        instance.setPortfolioUser(user);
        assertEquals(portfolio.getPortfolioUser().getId(), instance.getPortfolioUser().getId());
    }

    @Test
    void setTotalValue() {
        Portfolio instance = new Portfolio(
                DEFAULT_ID,
                "",
                "",
                DEFAULT_TOTAL_VALUE
        );
        instance.setTotalValue(DEFAULT_TOTAL_VALUE);
        assertEquals(DEFAULT_TOTAL_VALUE, instance.getTotalValue());
    }

    @Test
    void getId() {
        assertEquals(DEFAULT_ID, portfolio.getId());
    }

    @Test
    void getName() {
        assertEquals(DEFAULT_NAME, portfolio.getName());
    }

    @Test
    void getDescription() {
        assertEquals(DEFAULT_DESCRIPTION, portfolio.getDescription());
    }

    @Test
    void getPortfolioStocks() {
        boolean isEmpty = portfolio.getPortfolioStocks().isEmpty();
        assertFalse(isEmpty);
    }

    @Test
    void getPortfolioUser() {
        assertNotNull(portfolio.getPortfolioUser());
    }

    @Test
    void getTotalValue() {
        assertEquals(DEFAULT_TOTAL_VALUE, portfolio.getTotalValue());
    }
}