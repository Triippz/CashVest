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

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Random;

/**
 * Asset is an abstract class, because we may in the future support
 * other "assets" such as Cryptocurrencies which will have similar
 * attributes to stocks
 *
 * @author Mark Tripoli
 */
@MappedSuperclass
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @Getter
    @Setter
    protected String name;

    @NotNull
    @Column(name = "symbol", nullable = false, unique = true)
    @Getter
    @Setter
    protected String symbol;

    public Asset(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    protected abstract String getSymbolTag();

    protected abstract List<String> getNews();

    protected String getStarRating() {
        String[] stars = new String[]{
                "⭐",
                "⭐⭐",
                "⭐⭐⭐",
                "⭐⭐⭐⭐",
                "⭐⭐⭐⭐⭐"
        };
        return stars [ new Random().nextInt( stars.length ) ];
    }
}
