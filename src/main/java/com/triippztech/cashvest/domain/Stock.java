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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Stock.
 */
@Entity
@EqualsAndHashCode(callSuper = false, exclude = {"transactions", "watchlists", "portfoliosIn", "priceAlerts"})
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stock extends Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "currency", nullable = false)
    @Getter
    @Setter
    private String currency;

    @Column(name = "exchange", nullable = false)
    @Getter
    @Setter
    private String exchange;

    @Getter
    @Setter
    @Transient
    private BigDecimal value;

    @Getter
    @Setter
    @Transient
    private BigDecimal percentChange;

    @OneToMany(mappedBy = "transactionStock", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Getter
    @Setter
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany(mappedBy = "watchlistStocks",
                fetch = FetchType.EAGER,
                targetEntity = Watchlist.class,
                cascade = {
                        CascadeType.MERGE,
                        CascadeType.PERSIST,
                        CascadeType.REFRESH,
                        CascadeType.DETACH
                })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    @Getter
    @Setter
    private Set<Watchlist> watchlists = new HashSet<>();

    @ManyToMany(
            mappedBy = "portfolioStocks",
            fetch = FetchType.EAGER,
            targetEntity = Portfolio.class,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    @Getter
    @Setter
    private Set<Portfolio> portfoliosIn = new HashSet<>();

    @OneToMany(mappedBy = "alertStock", fetch = FetchType.EAGER, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Getter
    @Setter
    private Set<PriceAlert> priceAlerts = new HashSet<>();

    public Stock() {
        super("", "");
    }

    public Stock(@NotNull String name, @NotNull String symbol) {
        super(name, symbol);
    }

    public Stock(@NotNull String name, @NotNull String symbol, String currency, String exchange,
                 BigDecimal value, BigDecimal percentChange) {
        super(name, symbol);
        this.currency = currency;
        this.exchange = exchange;
        this.value = value;
        this.percentChange = percentChange;
    }

    public Stock(@NotNull String name, @NotNull String symbol, String currency, String exchange,
                 Set<Transaction> transactions, Set<Watchlist> watchlists, BigDecimal value,
                 BigDecimal percentChange, Set<Portfolio> portfoliosIn, Set<PriceAlert> priceAlerts) {
        super(name, symbol);
        this.currency = currency;
        this.exchange = exchange;
        this.transactions = transactions;
        this.watchlists = watchlists;
        this.value = value;
        this.percentChange = percentChange;
        this.portfoliosIn = portfoliosIn;
        this.priceAlerts = priceAlerts;
    }

    public Stock(@NotNull String name, @NotNull String symbol, String currency, String exchange,
                 Set<Transaction> transactions, Set<Watchlist> watchlists) {
        super(name, symbol);
        this.currency = currency;
        this.exchange = exchange;
        this.transactions = transactions;
        this.watchlists = watchlists;
    }

    public Stock name(String name) {
        this.name = name;
        return this;
    }

    public Stock symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Stock currency(String currency) {
        this.currency = currency;
        return this;
    }

    public Stock exchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public Stock transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Stock addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.stock(this);
        return this;
    }

    public Stock removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.stock(null);
        return this;
    }

    public Stock watchlists(Set<Watchlist> watchlists) {
        this.watchlists = watchlists;
        return this;
    }

    public Stock portfoliosIn(Set<Portfolio> portfolios) {
        this.portfoliosIn = portfolios;
        return this;
    }

    public Stock addPortfolio(Portfolio portfolio) {
        this.portfoliosIn.add(portfolio);
        portfolio.getPortfolioStocks().add(this);
        return this;
    }

    public Stock removePortfolio(Portfolio portfolio) {
        this.portfoliosIn.remove(portfolio);
        portfolio.getPortfolioStocks().remove(this);
        return this;
    }

    public Stock priceAlerts(Set<PriceAlert> priceAlerts) {
        this.priceAlerts = priceAlerts;
        return this;
    }

    public Stock addPriceAlert(PriceAlert priceAlert) {
        this.priceAlerts.add(priceAlert);
        priceAlert.stock(this);
        return this;
    }

    public Stock removePriceAlert(PriceAlert priceAlert) {
        this.priceAlerts.remove(priceAlert);
        priceAlert.stock(null);
        return this;
    }

    public Stock addWatchlist(Watchlist watchlist) {
        this.watchlists.add(watchlist);
        watchlist.getWatchlistStocks().add(this);
        return this;
    }

    public Stock removeWatchlist(Watchlist watchlist) {
        this.watchlists.remove(watchlist);
        watchlist.getWatchlistStocks().remove(this);
        return this;
    }

    @Override
    public String getSymbolTag() {
        return "$" + this.symbol;
    }

    @Override
    public List<String> getNews() {
        List<String> news = new ArrayList<>();
        news.add("News 1 for Stocks");
        news.add("News 2 for Stocks");
        return news;
    }
}
