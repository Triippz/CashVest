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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.triippztech.cashvest.domain.enumeration.AlertMethod;
import com.triippztech.cashvest.domain.enumeration.AlertThresholdType;
import com.triippztech.cashvest.domain.enumeration.AlertType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * This class should probably be abstracted where we can create different types
 * of alerts and build them.
 *
 * @author Mark Tripoli
 */
@Entity
@EqualsAndHashCode(exclude = {"alertStock", "alertUser"})
@Table(name = "price_alert")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PriceAlert implements Serializable {

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
    @Column(name = "method", nullable = false)
    private AlertMethod method;

    @NotNull
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_threshold_type", nullable = false)
    private AlertThresholdType alertThresholdType;

    @NotNull
    @Getter
    @Setter
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.MERGE)
    @Getter
    @Setter
    @JsonIgnoreProperties("priceAlerts")
    private Stock alertStock;

    @ManyToOne(cascade = CascadeType.MERGE)
    @Getter
    @Setter
    @JsonIgnoreProperties("alerts")
    private User alertUser;

    @Getter
    @Setter
    @Transient
    private BigDecimal currentPrice;

    public PriceAlert() {
    }

    public PriceAlert(@NotNull AlertMethod method, @NotNull AlertThresholdType alertThresholdType,
                      @NotNull User alertUser, @NotNull BigDecimal price, Stock alertStock, BigDecimal currentPrice) {
        this.method = method;
        this.alertThresholdType = alertThresholdType;
        this.alertUser = alertUser;
        this.price = price;
        this.alertStock = alertStock;
        this.currentPrice = currentPrice;
    }

    public PriceAlert(@NotNull AlertMethod method, @NotNull AlertType alertType, @NotNull AlertThresholdType alertThresholdType,
                      @NotNull User alertUser, @NotNull BigDecimal price) {
        this.method = method;
        this.alertUser = alertUser;
        this.alertThresholdType = alertThresholdType;
        this.price = price;
    }

    public PriceAlert(AlertMethod defaultAlertMethod, AlertThresholdType defaultAlertTypeThresh,
                      BigDecimal defaultPrice, BigDecimal defaultCurrentPrice) {
        this.method = defaultAlertMethod;
        this.alertThresholdType = defaultAlertTypeThresh;
        this.price = defaultPrice;
        this.currentPrice = defaultCurrentPrice;

    }


    public PriceAlert method(AlertMethod method) {
        this.method = method;
        return this;
    }

    public PriceAlert alertThresholdType(AlertThresholdType alertThresholdType) {
        this.alertThresholdType = alertThresholdType;
        return this;
    }

    public PriceAlert price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public PriceAlert alertUser(User alertUser) {
        this.alertUser = alertUser;
        return this;
    }

    public PriceAlert stock(Stock stock) {
        this.alertStock = stock;
        return this;
    }

}