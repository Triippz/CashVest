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

package com.triippztech.cashvest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.triippztech.cashvest.domain.enumeration.TransactionType;


/**
 * A Transaction.
 */
@Entity
@EqualsAndHashCode
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @NotNull
    @Getter
    @Setter
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @NotNull
    @Getter
    @Setter
    @Column(name = "price_per", nullable = false)
    private BigDecimal pricePer;

    @NotNull
    @Getter
    @Setter
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @Getter
    @Setter
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @Getter
    @Setter
    @JsonIgnoreProperties("transactionStock")
    private Stock transactionStock;

    @ManyToOne
    @Getter
    @Setter
    @JsonIgnoreProperties("userTransactions")
    private User transactionUser;

    public Transaction() {
    }

    public Transaction(@NotNull TransactionType type, @NotNull Long quantity, BigDecimal pricePer,
                       BigDecimal totalPrice, LocalDate date, Stock transactionStock, User transactionUser) {
        this.type = type;
        this.quantity = quantity;
        this.pricePer = pricePer;
        this.totalPrice = totalPrice;
        this.date = date;
        this.transactionStock = transactionStock;
        this.transactionUser = transactionUser;
    }

    public Transaction(@NotNull TransactionType type, @NotNull Long quantity, BigDecimal pricePer, BigDecimal totalPrice,
                       LocalDate date, Stock transactionStock) {
        this.type = type;
        this.quantity = quantity;
        this.pricePer = pricePer;
        this.totalPrice = totalPrice;
        this.date = date;
        this.transactionStock = transactionStock;
//        this.transactionPortfolio = transactionPortfolio;
    }

    public Transaction(TransactionType type, long quantity, BigDecimal pricePer,
                       BigDecimal totalPrice, LocalDate localDate) {
        this.type = type;
        this.quantity = quantity;
        this.pricePer = pricePer;
        this.totalPrice = totalPrice;
        this.date = localDate;

    }

    public Transaction type(TransactionType type) {
        this.type = type;
        return this;
    }

    public Transaction quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public Transaction pricePer(BigDecimal pricePer) {
        this.pricePer = pricePer;
        return this;
    }

    public Transaction totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Transaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public Transaction stock(Stock stock) {
        this.transactionStock = stock;
        return this;
    }

    public Transaction transactionUser(User user) {
        this.transactionUser = user;
        return this;
    }
}
