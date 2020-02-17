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
import com.triippztech.cashvest.config.Constants;
import com.triippztech.cashvest.domain.annotations.PhoneNumberConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
@Entity
@EqualsAndHashCode(exclude = {"portfolios", "priceAlerts", "watchlists", "transactions"})
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Getter
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Getter
    @Setter
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Getter
    @Setter
    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Getter
    @Setter
    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Getter
    @Setter
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @Getter
    @Setter
//    @PhoneNumberConstraint
    @Size(min = 5, max = 254)
    @Column(name = "phone_number", length = 254)
    private String phoneNumber;

    @Getter
    @Setter
    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @NotNull
    @Getter
    @Setter
    @Column(name = "is_signed_in", columnDefinition="BOOLEAN DEFAULT false", nullable = false)
    private Boolean isSignedIn;

    @Getter
    @Setter
    @OneToMany(mappedBy = "portfolioUser",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Portfolio> portfolios = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "alertUser",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PriceAlert> priceAlerts = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "watchlistUser",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Watchlist> watchlists = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "transactionUser",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transaction> transactions = new HashSet<>();

    public User() {
    }

    public User(@NotNull String login, @NotNull String password, String firstName, String lastName,
                @NotNull String email) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(@NotNull String login, @NotNull String password, String firstName, String lastName,
                @NotNull String email, String phoneNumber, String imageUrl, Set<Portfolio> portfolios,
                Set<PriceAlert> priceAlerts, Set<Watchlist> watchlists, Set<Transaction> transactions) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.portfolios = portfolios;
        this.priceAlerts = priceAlerts;
        this.watchlists = watchlists;
        this.transactions = transactions;
    }

    public User(@NotNull String login, @NotNull String password, String firstName, String lastName,
                @NotNull String email, String phoneNumber, String imageUrl)
    {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.isSignedIn = false;
    }

    public User(@NotNull String login, @NotNull String password, String firstName, String lastName,
                @NotNull String email, String phoneNumber, String imageUrl, Set<Portfolio> portfolios, Set<PriceAlert> alerts,
                Set<Watchlist> watchlists)
    {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.portfolios = portfolios;
        this.priceAlerts = alerts;
        this.watchlists = watchlists;
        this.isSignedIn = false;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public User addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
        portfolio.setPortfolioUser(this);
        return this;
    }

    public User removePortfolio(Portfolio portfolio) {
        this.portfolios.remove(portfolio);
        portfolio.setPortfolioUser(null);
        return this;
    }

    public User addPriceAlert(PriceAlert priceAlert) {
        this.priceAlerts.add(priceAlert);
        priceAlert.setAlertUser(this);
        return this;
    }

    public User removePriceAlert(PriceAlert priceAlert) {
        this.priceAlerts.remove(priceAlert);
        priceAlert.setAlertUser(null);
        return this;
    }

    public User addWatchlist(Watchlist watchlist) {
        this.watchlists.add(watchlist);
        watchlist.setWatchlistUser(this);
        return this;
    }

    public User removeWatchlist(Watchlist watchlist) {
        this.watchlists.remove(watchlist);
        watchlist.setWatchlistUser(null);
        return this;
    }

    public User addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setTransactionUser(this);
        return this;
    }

    public User removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setTransactionUser(null);
        return this;
    }

    public String getFullName() {
        if ( this.firstName != null && this.lastName != null )
            return this.firstName + " " + this.lastName;
        else
            return this.email;
    }

}
