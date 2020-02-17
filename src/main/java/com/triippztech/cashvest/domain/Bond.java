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

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bond extends Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private BigDecimal couponRate;

    @Getter
    @Setter
    private LocalDate maturityDate;

    @Getter
    @Setter
    private BigDecimal price;

    @Getter
    @Setter
    private BigDecimal changePercent;

    public Bond(String name, String symbol) {
        super(name, symbol);
    }

    @Override
    public String getSymbolTag() {
        return this.symbol;
    }

    @Override
    public List<String> getNews() {
        List<String> news = new ArrayList<>();
        news.add("News 1 for Bonds");
        news.add("News 2 for Bonds");
        return news;
    }

}
