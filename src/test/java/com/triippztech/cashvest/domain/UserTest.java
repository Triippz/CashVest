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

public class UserTest {

    public static long DEFAULT_ID = 1L;
    public static String DEFAULT_LOGIN = "johndoe";
    public static String DEFAULT_PASSWORD = "test";
    public static String DEFAULT_FIRST_NAME = "John";
    public static String DEFAULT_LAST_NAME = "Doe";
    public static String DEFAULT_EMAIL = "John.Doe@example.com";
    public static String DEFAULT_PHONE_NUMBER = "+18888888888";
    public static String DEFAULT_IMAGE_URL = "";
    public static Boolean DEFAULT_IS_SIGNED_IN = true;

    private static User user;
    private static Stock stock;
    private static Portfolio portfolio;
    private static PriceAlert priceAlert;
    private static Watchlist watchlist;
    private static Transaction transaction;

    @BeforeEach
    void setUp() {
        user = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        user.setId(DEFAULT_ID);
        user.setIsSignedIn(DEFAULT_IS_SIGNED_IN);

        stock = new Stock(
                StockTest.DEFAULT_NAME,
                StockTest.DEFAULT_SYMBOL,
                StockTest.DEFAULT_CURRENCY,
                StockTest.DEFAULT_EXCHANGE,
                StockTest.DEFAULT_VALUE,
                StockTest.DEFAULT_PERCENT_CHANGE
        );
        stock.setId(DEFAULT_ID);

        portfolio = new Portfolio(
                PortfolioTest.DEFAULT_ID,
                PortfolioTest.DEFAULT_NAME,
                PortfolioTest.DEFAULT_DESCRIPTION,
                PortfolioTest.DEFAULT_TOTAL_VALUE
        );

        priceAlert = new PriceAlert(
                PriceAlertTest.DEFAULT_ALERT_METHOD,
                PriceAlertTest.DEFAULT_ALERT_TYPE_THRESH,
                user,
                PriceAlertTest.DEFAULT_PRICE,
                stock,
                PriceAlertTest.DEFAULT_CURRENT_PRICE
        );
        priceAlert.setId(DEFAULT_ID);

        watchlist = new Watchlist(
                WatchlistTest.DEFAULT_LIST_NAME
        );
        watchlist.setId(WatchlistTest.DEFAULT_ID);
        watchlist.addStock(stock);
        watchlist.setWatchlistUser(user);

        transaction = new Transaction(
                TransactionTest.DEFAULT_TRANSACTION_TYPE,
                TransactionTest.DEFAULT_QUANTITY,
                TransactionTest.DEFAULT_PRICE_PER,
                TransactionTest.DEFAULT_TOTAL_PRICE,
                TransactionTest.DEFAULT_LOCAL_DATE
        );
        transaction.setId(TransactionTest.DEFAULT_ID);
        transaction.setTransactionUser(user);
        transaction.setTransactionStock(stock);

        user.addPriceAlert(priceAlert);
        user.addPortfolio(portfolio);
        user.addTransaction(transaction);
        user.addWatchlist(watchlist);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setLogin() {
        User instance = new User(
                "",
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setLogin(DEFAULT_LOGIN);
        assertEquals(DEFAULT_LOGIN, instance.getLogin());
    }

    @Test
    void addPortfolio() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.addPortfolio(portfolio);
        assertFalse(instance.getPortfolios().isEmpty());
    }

    @Test
    void removePortfolio() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.addPortfolio(portfolio);
        instance.removePortfolio(portfolio);
        assertTrue(instance.getPortfolios().isEmpty());
    }

    @Test
    void addPriceAlert() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.addPriceAlert(priceAlert);
        assertFalse(instance.getPriceAlerts().isEmpty());
    }

    @Test
    void removePriceAlert() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.addPriceAlert(priceAlert);
        instance.removePriceAlert(priceAlert);
        assertTrue(instance.getPriceAlerts().isEmpty());
    }

    @Test
    void addWatchlist() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.addWatchlist(watchlist);
        assertFalse(instance.getWatchlists().isEmpty());
    }

    @Test
    void removeWatchlist() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.addWatchlist(watchlist);
        instance.removeWatchlist(watchlist);
        assertTrue(instance.getWatchlists().isEmpty());
    }

    @Test
    void addTransaction() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.addTransaction(transaction);
        assertFalse(instance.getTransactions().isEmpty());
    }

//    @Test
//    void removeTransaction() {
//        User instance = new User(
//                DEFAULT_LOGIN,
//                DEFAULT_PASSWORD,
//                DEFAULT_FIRST_NAME,
//                DEFAULT_LAST_NAME,
//                DEFAULT_EMAIL,
//                DEFAULT_PHONE_NUMBER,
//                DEFAULT_IMAGE_URL
//        );
//        instance.addTransaction(transaction);
//        instance.removeTransaction(transaction);
//        assertTrue(instance.getTransactions().isEmpty());
//    }

    @Test
    void getFullName() {
        assertEquals(DEFAULT_FIRST_NAME + " " + DEFAULT_LAST_NAME, user.getFullName());
    }

    @Test
    void setId() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setId(DEFAULT_ID);
        assertEquals(DEFAULT_ID, instance.getId());
    }

    @Test
    void setPassword() {
        User instance = new User(
                DEFAULT_LOGIN,
                "",
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setPassword(DEFAULT_PASSWORD);
        assertEquals(DEFAULT_PASSWORD, instance.getPassword());
    }

    @Test
    void setFirstName() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                "",
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setFirstName(DEFAULT_FIRST_NAME);
        assertEquals(DEFAULT_FIRST_NAME, instance.getFirstName());
    }

    @Test
    void setLastName() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                "",
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setLastName(DEFAULT_LAST_NAME);
        assertEquals(DEFAULT_LAST_NAME, instance.getLastName());
    }

    @Test
    void setEmail() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                "",
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setEmail(DEFAULT_EMAIL);
        assertEquals(DEFAULT_EMAIL, instance.getEmail());
    }

    @Test
    void setPhoneNumber() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                "",
                DEFAULT_IMAGE_URL
        );
        instance.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        assertEquals(DEFAULT_PHONE_NUMBER, instance.getPhoneNumber());
    }

    @Test
    void setImageUrl() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setImageUrl(DEFAULT_IMAGE_URL);
        assertEquals(DEFAULT_IMAGE_URL, instance.getImageUrl());
    }

    @Test
    void setIsSignedIn() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        instance.setIsSignedIn(DEFAULT_IS_SIGNED_IN);
        assertTrue(instance.getIsSignedIn());
    }

    @Test
    void setPortfolios() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        Set<Portfolio> portfolioSet = new HashSet<>();
        portfolioSet.add(portfolio);
        instance.setPortfolios(portfolioSet);
        assertFalse(instance.getPortfolios().isEmpty());
    }

    @Test
    void setPriceAlerts() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        Set<PriceAlert> priceAlerts = new HashSet<>();
        priceAlerts.add(priceAlert);
        instance.setPriceAlerts(priceAlerts);
        assertFalse(instance.getPriceAlerts().isEmpty());
    }

    @Test
    void setWatchlists() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        Set<Watchlist> watchlists = new HashSet<>();
        watchlists.add(watchlist);
        instance.setWatchlists(watchlists);
        assertFalse(instance.getWatchlists().isEmpty());
    }

    @Test
    void setTransactions() {
        User instance = new User(
                DEFAULT_LOGIN,
                DEFAULT_PASSWORD,
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_EMAIL,
                DEFAULT_PHONE_NUMBER,
                DEFAULT_IMAGE_URL
        );
        Set<Watchlist> watchlists = new HashSet<>();
        watchlists.add(watchlist);
        instance.setWatchlists(watchlists);
        assertFalse(instance.getWatchlists().isEmpty());
    }

    @Test
    void getId() {
        assertEquals(DEFAULT_ID, user.getId());
    }

    @Test
    void getLogin() {
        assertEquals(DEFAULT_LOGIN, user.getLogin());
    }

    @Test
    void getPassword() {
        assertEquals(DEFAULT_PASSWORD, user.getPassword());
    }

    @Test
    void getFirstName() {
        assertEquals(DEFAULT_FIRST_NAME, user.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals(DEFAULT_LAST_NAME, user.getLastName());
    }

    @Test
    void getEmail() {
        assertEquals(DEFAULT_EMAIL, user.getEmail());
    }

    @Test
    void getPhoneNumber() {
        assertEquals(DEFAULT_PHONE_NUMBER, user.getPhoneNumber());
    }

    @Test
    void getImageUrl() {
        assertEquals(DEFAULT_IMAGE_URL, user.getImageUrl());
    }

    @Test
    void getIsSignedIn() {
        assertTrue(user.getIsSignedIn());
    }

    @Test
    void getPortfolios() {
        assertFalse(user.getPortfolios().isEmpty());
    }

    @Test
    void getPriceAlerts() {
        assertFalse(user.getPriceAlerts().isEmpty());
    }

    @Test
    void getWatchlists() {
        assertFalse(user.getWatchlists().isEmpty());
    }

    @Test
    void getTransactions() {
        assertFalse(user.getTransactions().isEmpty());
    }
}