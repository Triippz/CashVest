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

import com.triippztech.cashvest.domain.enumeration.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    public static long DEFAULT_ID = 1L;
    public static TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.BUY;
    public static long DEFAULT_QUANTITY = 1L;
    public static BigDecimal DEFAULT_PRICE_PER = new BigDecimal("1.00");
    public static BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal("2.00");
    public static LocalDate DEFAULT_LOCAL_DATE = LocalDate.of(2020, 1, 1);

    private static Stock stock;
    private static User user;
    private static Transaction transaction;

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

        transaction = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                DEFAULT_QUANTITY,
                DEFAULT_PRICE_PER,
                DEFAULT_TOTAL_PRICE,
                DEFAULT_LOCAL_DATE
        );
        transaction.setId(DEFAULT_ID);
        transaction.setTransactionUser(user);
        transaction.setTransactionStock(stock);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void type() {
        Transaction instance = transaction.type(DEFAULT_TRANSACTION_TYPE);
        assertEquals(instance.getType(), DEFAULT_TRANSACTION_TYPE);
    }

    @Test
    void quantity() {
        Transaction instance = transaction.quantity(DEFAULT_QUANTITY);
        assertEquals(DEFAULT_QUANTITY, instance.getQuantity());
    }

    @Test
    void pricePer() {
        Transaction instance = transaction.pricePer(DEFAULT_PRICE_PER);
        assertEquals(DEFAULT_PRICE_PER, instance.getPricePer());
    }

    @Test
    void totalPrice() {
        Transaction instance = transaction.totalPrice(DEFAULT_TOTAL_PRICE);
        assertEquals(DEFAULT_TOTAL_PRICE, instance.getTotalPrice());
    }

    @Test
    void date() {
        Transaction instance = transaction.date(DEFAULT_LOCAL_DATE);
        assertEquals(DEFAULT_LOCAL_DATE, instance.getDate());
    }

    @Test
    void stock() {
        Transaction instance = transaction.stock(stock);
        assertEquals(stock.getId(), instance.getTransactionStock().getId());
    }

    @Test
    void transactionUser() {
        Transaction instance = transaction.transactionUser(user);
        assertEquals(user.getId(), instance.getTransactionUser().getId());
    }

    @Test
    void setId() {
        transaction = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                DEFAULT_QUANTITY,
                DEFAULT_PRICE_PER,
                DEFAULT_TOTAL_PRICE,
                DEFAULT_LOCAL_DATE
        );
        transaction.setId(DEFAULT_ID);
        assertEquals(DEFAULT_ID, transaction.getId());
    }

    @Test
    void setType() {
        Transaction instance = new Transaction(
                TransactionType.SELL,
                DEFAULT_QUANTITY,
                DEFAULT_PRICE_PER,
                DEFAULT_TOTAL_PRICE,
                DEFAULT_LOCAL_DATE
        );
        instance.setType(DEFAULT_TRANSACTION_TYPE);
        assertEquals(DEFAULT_TRANSACTION_TYPE, instance.getType());
    }

    @Test
    void setQuantity() {
        Transaction instance = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                2L,
                DEFAULT_PRICE_PER,
                DEFAULT_TOTAL_PRICE,
                DEFAULT_LOCAL_DATE
        );
        instance.setQuantity(DEFAULT_QUANTITY);
        assertEquals(DEFAULT_QUANTITY, instance.getQuantity());
    }

    @Test
    void setPricePer() {
        Transaction instance = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                DEFAULT_QUANTITY,
                new BigDecimal("21.00"),
                DEFAULT_TOTAL_PRICE,
                DEFAULT_LOCAL_DATE
        );
        instance.setPricePer(DEFAULT_PRICE_PER);
        assertEquals(DEFAULT_PRICE_PER, instance.getPricePer());
    }

    @Test
    void setTotalPrice() {
        Transaction instance = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                DEFAULT_QUANTITY,
                DEFAULT_PRICE_PER,
                new BigDecimal("21.00"),
                DEFAULT_LOCAL_DATE
        );
        instance.setTotalPrice(DEFAULT_TOTAL_PRICE);
        assertEquals(DEFAULT_TOTAL_PRICE, instance.getTotalPrice());
    }

    @Test
    void setDate() {
        Transaction instance = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                DEFAULT_QUANTITY,
                DEFAULT_PRICE_PER,
                DEFAULT_TOTAL_PRICE,
                LocalDate.now()
        );
        instance.setDate(DEFAULT_LOCAL_DATE);
        assertEquals(DEFAULT_LOCAL_DATE, instance.getDate());
    }

    @Test
    void setTransactionStock() {
        Transaction instance = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                DEFAULT_QUANTITY,
                DEFAULT_PRICE_PER,
                DEFAULT_TOTAL_PRICE,
                DEFAULT_LOCAL_DATE
        );
        instance.setTransactionStock(stock);
        assertEquals(stock.getId(), instance.getTransactionStock().getId());
    }

    @Test
    void setTransactionUser() {
        Transaction instance = new Transaction(
                DEFAULT_TRANSACTION_TYPE,
                DEFAULT_QUANTITY,
                DEFAULT_PRICE_PER,
                DEFAULT_TOTAL_PRICE,
                DEFAULT_LOCAL_DATE
        );
        instance.setTransactionUser(user);
        assertEquals(user.getId(), instance.getTransactionUser().getId());
    }

    @Test
    void getId() {
        assertEquals(DEFAULT_ID, transaction.getId());
    }

    @Test
    void getType() {
        assertEquals(DEFAULT_TRANSACTION_TYPE, transaction.getType());
    }

    @Test
    void getQuantity() {
        assertEquals(DEFAULT_QUANTITY, transaction.getQuantity());
    }

    @Test
    void getPricePer() {
        assertEquals(DEFAULT_PRICE_PER, transaction.getPricePer());
    }

    @Test
    void getTotalPrice() {
        assertEquals(DEFAULT_TOTAL_PRICE, transaction.getTotalPrice());
    }

    @Test
    void getDate() {
        assertEquals(DEFAULT_LOCAL_DATE, transaction.getDate());
    }

    @Test
    void getTransactionStock() {
        assertEquals(stock.getId(), transaction.getTransactionStock().getId());
    }

    @Test
    void getTransactionUser() {
        assertEquals(user.getId(), transaction.getTransactionUser().getId());
    }
}