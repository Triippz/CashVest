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
import java.util.HashSet;
import java.util.Set;

/**
 * A Portfolio.
 */
@Entity
@EqualsAndHashCode(exclude = {"portfolioStocks", "portfolioUser"})
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Portfolio implements Serializable {

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
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "description")
    private String description;


    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "portfolio_stock",
            joinColumns = @JoinColumn(name = "portfolio_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id", referencedColumnName = "id"))
    private Set<Stock> portfolioStocks = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @Getter
    @Setter
    @JsonIgnoreProperties("portfolios")
    private User portfolioUser;

    /**
     * Ideally this would be it's own table
     * which would be updated periodically.
     * Butttttt, for the sake of time, we will
     * make this a non-persisted value and
     * set it as needed
     */
    @Getter
    @Setter
    @Transient
    private BigDecimal totalValue;


    public Portfolio() {
    }

    public Portfolio(Long id, @NotNull String name, String description, BigDecimal totalValue)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalValue = totalValue;
    }

    public Portfolio(@NotNull String name, String description, User user, Stock stock) {
        this.name = name;
        this.description = description;
        this.portfolioUser = user;
        this.addStock(stock);
    }

    public Portfolio(@NotNull String name, String description, Set<Stock> portfolioStocks,
                     User portfolioUser, BigDecimal totalValue) {
        this.name = name;
        this.description = description;
        this.portfolioStocks = portfolioStocks;
        this.portfolioUser = portfolioUser;
        this.totalValue = totalValue;
    }

    public Portfolio(@NotNull String name, String description, Set<Stock> portfolioStocks, User portfolioUser) {
        this.name = name;
        this.description = description;
        this.portfolioStocks = portfolioStocks;
        this.portfolioUser = portfolioUser;
    }

    public Portfolio name(String name) {
        this.name = name;
        return this;
    }

    public Portfolio description(String description) {
        this.description = description;
        return this;
    }

    public Portfolio portfolioStocks(Set<Stock> portfolioStocks) {
        this.portfolioStocks = portfolioStocks;
        return this;
    }

    public Portfolio addStock(Stock stock) {
        this.portfolioStocks.add(stock);
        stock.getPortfoliosIn().add(this);
        return this;
    }

    public Portfolio removeStock(Stock stock) {
        this.portfolioStocks.remove(stock);
        stock.getPortfoliosIn().remove(this);
        return this;
    }

    @Override
    public String toString() {
        return name;
    }


}
