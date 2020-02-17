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
import java.util.HashSet;
import java.util.Set;

/**
 * A Watchlist.
 */
@Entity
@EqualsAndHashCode(exclude = {"watchlistStocks", "watchlistUser"})
@Table(name = "watchlist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Watchlist implements Serializable {

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
    @Column(name = "list_name", nullable = false)
    private String listName;

    @ManyToMany(fetch = FetchType.EAGER)
    @Getter
    @Setter
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "watchlist_stock",
            joinColumns = @JoinColumn(name = "watchlist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id", referencedColumnName = "id"))
    private Set<Stock> watchlistStocks = new HashSet<>();

    @ManyToOne
    @Getter
    @Setter
    @JsonIgnoreProperties("watchlists")
    private User watchlistUser;

    public Watchlist() {
    }

    public Watchlist(@NotNull String listName,  Set<Stock> watchlistStocks, User watchlistUser) {
        this.listName = listName;
        this.watchlistStocks = watchlistStocks;
        this.watchlistUser = watchlistUser;
    }

    public Watchlist(@NotNull String listName)
    {
        this.listName = listName;
    }
    public Watchlist(@NotNull String listName, @NotNull User watchlistUser)
    {
        this.listName = listName;
        this.watchlistUser = watchlistUser;
    }

    public Watchlist(@NotNull String listName, @NotNull User watchlistUser, Set<Stock> watchlistStocks)
    {
        this.listName = listName;
        this.watchlistUser = watchlistUser;
        this.watchlistStocks = watchlistStocks;
    }

    public Watchlist listName(String listName) {
        this.listName = listName;
        return this;
    }

    public Watchlist stocks(Set<Stock> stocks) {
        this.watchlistStocks = stocks;
        return this;
    }

    public Watchlist addStock(Stock stock) {
        this.watchlistStocks.add(stock);
        stock.getWatchlists().add(this);
        return this;
    }

    public Watchlist removeStock(Stock stock) {
        this.watchlistStocks.remove(stock);
        stock.getWatchlists().remove(this);
        return this;
    }

    @Override
    public String toString() {
        return this.listName;
    }
}
