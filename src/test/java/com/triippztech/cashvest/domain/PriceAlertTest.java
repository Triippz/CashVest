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

import com.triippztech.cashvest.domain.enumeration.AlertMethod;
import com.triippztech.cashvest.domain.enumeration.AlertThresholdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceAlertTest {

    public static long DEFAULT_ID = 1L;
    public static AlertMethod DEFAULT_ALERT_METHOD = AlertMethod.ALL_METHODS;
    public static AlertThresholdType DEFAULT_ALERT_TYPE_THRESH = AlertThresholdType.ABOVE;
    public static BigDecimal DEFAULT_PRICE = new BigDecimal("23.00");
    public static BigDecimal DEFAULT_CURRENT_PRICE = new BigDecimal("24.00");

    private Stock stock;
    private User user;
    private PriceAlert priceAlert;

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
        priceAlert = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                DEFAULT_ALERT_TYPE_THRESH,
                user,
                DEFAULT_PRICE,
                stock,
                DEFAULT_CURRENT_PRICE
        );
        priceAlert.setId(DEFAULT_ID);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void alertThresholdType() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                DEFAULT_ALERT_TYPE_THRESH,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );

        PriceAlert newInstance = instance.alertThresholdType(DEFAULT_ALERT_TYPE_THRESH);
        assertEquals(DEFAULT_ALERT_TYPE_THRESH, newInstance.getAlertThresholdType());
    }

    @Test
    void price() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                DEFAULT_ALERT_TYPE_THRESH,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );

        PriceAlert newInstance = instance.price(DEFAULT_PRICE);
        assertEquals(DEFAULT_PRICE, newInstance.getPrice());
    }

    @Test
    void alertUser() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                DEFAULT_ALERT_TYPE_THRESH,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );

        PriceAlert newInstance = instance.alertUser(user);
        assertEquals(user.getId(), newInstance.getAlertUser().getId());
    }

    @Test
    void stock() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                DEFAULT_ALERT_TYPE_THRESH,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );

        PriceAlert newInstance = instance.stock(stock);
        assertNotNull(newInstance.getAlertStock());
    }

    @Test
    void setId() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                DEFAULT_ALERT_TYPE_THRESH,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );
        instance.setId(DEFAULT_ID);
        assertEquals(DEFAULT_ID, instance.getId());
    }

    @Test
    void setMethod() {
        PriceAlert instance = new PriceAlert(
                AlertMethod.SMS,
                DEFAULT_ALERT_TYPE_THRESH,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );
        instance.setMethod(DEFAULT_ALERT_METHOD);
        assertEquals(DEFAULT_ALERT_METHOD, instance.getMethod());
    }

    @Test
    void setAlertThresholdType() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                AlertThresholdType.BELOW,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );
        instance.setAlertThresholdType(DEFAULT_ALERT_TYPE_THRESH);
        assertEquals(DEFAULT_ALERT_TYPE_THRESH, instance.getAlertThresholdType());
    }

    @Test
    void setPrice() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                AlertThresholdType.BELOW,
                user,
                DEFAULT_CURRENT_PRICE,
                stock,
                null
        );
        instance.setPrice(DEFAULT_PRICE);
        assertEquals(DEFAULT_PRICE, instance.getPrice());
    }

    @Test
    void setAlertStock() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                AlertThresholdType.BELOW,
                user,
                DEFAULT_CURRENT_PRICE,
                null,
                DEFAULT_PRICE
        );
        instance.setAlertStock(stock);
        assertNotNull(instance.getAlertStock());
    }

    @Test
    void setAlertUser() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                AlertThresholdType.BELOW,
                null,
                DEFAULT_CURRENT_PRICE,
                stock,
                DEFAULT_PRICE
        );
        instance.setAlertUser(user);
        assertNotNull(instance.getAlertUser());
    }

    @Test
    void setCurrentPrice() {
        PriceAlert instance = new PriceAlert(
                DEFAULT_ALERT_METHOD,
                AlertThresholdType.BELOW,
                user,
                null,
                stock,
                DEFAULT_PRICE
        );
        instance.setCurrentPrice(DEFAULT_CURRENT_PRICE);
        assertEquals(DEFAULT_CURRENT_PRICE, instance.getCurrentPrice());
    }

    @Test
    void getId() {
        assertEquals(DEFAULT_ID, priceAlert.getId());
    }

    @Test
    void getMethod() {
        assertEquals(DEFAULT_ALERT_METHOD, priceAlert.getMethod());
    }

    @Test
    void getAlertThresholdType() {
        assertEquals(DEFAULT_ALERT_TYPE_THRESH, priceAlert.getAlertThresholdType());
    }

    @Test
    void getPrice() {
        assertEquals(DEFAULT_PRICE, priceAlert.getPrice());
    }

    @Test
    void getAlertStock() {
        assertEquals(StockTest.DEFAULT_ID, priceAlert.getAlertStock().getId());
    }

    @Test
    void getAlertUser() {
        assertEquals(UserTest.DEFAULT_ID, priceAlert.getAlertUser().getId());
    }

    @Test
    void getCurrentPrice() {
        assertEquals(DEFAULT_CURRENT_PRICE, priceAlert.getCurrentPrice());
    }
}