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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WatchlistTest {

    public static long DEFAULT_ID = 1L;
    public static String DEFAULT_LIST_NAME = "My Watchlist";

    private static Watchlist watchlist;
    private static Stock stock;
    private static User user;


    @BeforeEach
    void setUp() {
        user = new User(
                UserTest.DEFAULT_LOGIN,
                UserTest.DEFAULT_PASSWORD,
                UserTest.DEFAULT_FIRST_NAME,
                UserTest.DEFAULT_LAST_NAME,
                UserTest.DEFAULT_EMAIL
        );
        user.setId(UserTest.DEFAULT_ID);

        stock = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL
        );
        stock.setId(StockTest.DEFAULT_ID);

        watchlist = new Watchlist(
                DEFAULT_LIST_NAME
        );
        watchlist.setId(DEFAULT_ID);
        watchlist.addStock(stock);
        watchlist.setWatchlistUser(user);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void listName() {
        Watchlist instance = watchlist.listName(DEFAULT_LIST_NAME);
        assertEquals(DEFAULT_LIST_NAME, instance.getListName());
    }

    @Test
    void stocks() {
        Watchlist instance = new Watchlist(
                DEFAULT_LIST_NAME
        );
        Set<Stock> stocks = new HashSet<>();
        stocks.add(stock);

        Watchlist newInstance = instance.stocks(stocks);
        assertFalse(newInstance.getWatchlistStocks().isEmpty());
    }

    @Test
    void addStock() {
        Watchlist instance = new Watchlist(
                DEFAULT_LIST_NAME
        );
        instance.addStock(stock);
        assertFalse(instance.getWatchlistStocks().isEmpty());
    }

    @Test
    void removeStock() {
        Watchlist instance = new Watchlist(
                DEFAULT_LIST_NAME
        );
        instance.addStock(stock);
        instance.removeStock(stock);
        assertTrue(instance.getWatchlistStocks().isEmpty());
    }

    @Test
    void setId() {
        Watchlist instance = new Watchlist(
                DEFAULT_LIST_NAME
        );
        instance.setId(DEFAULT_ID);
        assertEquals(DEFAULT_ID, instance.getId());
    }

    @Test
    void setListName() {
        Watchlist instance = new Watchlist(
                ""
        );
        instance.setListName(DEFAULT_LIST_NAME);
        assertEquals(DEFAULT_LIST_NAME, instance.getListName());
    }

    @Test
    void setWatchlistStocks() {
        Watchlist instance = new Watchlist(
                DEFAULT_LIST_NAME
        );
        Set<Stock> stocks = new HashSet<>();
        stocks.add(stock);

        instance.setWatchlistStocks(stocks);
        assertFalse(instance.getWatchlistStocks().isEmpty());
    }

    @Test
    void setWatchlistUser() {
        Watchlist instance = new Watchlist(
                DEFAULT_LIST_NAME
        );
        instance.setWatchlistUser(user);
        assertNotNull(instance.getWatchlistUser());
    }

    @Test
    void getId() {
        assertEquals(DEFAULT_ID, watchlist.getId());
    }

    @Test
    void getListName() {
        assertEquals(DEFAULT_LIST_NAME, watchlist.getListName());
    }

    @Test
    void getWatchlistStocks() {
        assertFalse(watchlist.getWatchlistStocks().isEmpty());
    }

    @Test
    void getWatchlistUser() {
        assertNotNull(watchlist.getWatchlistUser());
    }
}