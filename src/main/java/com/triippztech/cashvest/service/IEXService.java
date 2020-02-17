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

package com.triippztech.cashvest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.exception.IEXTradingException;
import pl.zankowski.iextrading4j.api.stocks.Company;
import pl.zankowski.iextrading4j.api.stocks.Logo;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.News;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.*;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.NewsRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IEXService {
    private final Logger log = LoggerFactory.getLogger(IEXService.class);


    private final IEXCloudClient iexTradingClient;

    public IEXService(IEXCloudClient iexTradingClient) {
        this.iexTradingClient = iexTradingClient;
    }

    public List<News> getNews() {
        log.debug("Request to get news from IEX");
        return iexTradingClient.executeRequest(new NewsRequestBuilder()
                .withWorldNews()
                .build());
    }

    public List<News> getNewsForAsset(String ticker) {
        log.debug("Request to get news for: {} - from IEX", ticker);
        return iexTradingClient.executeRequest(new NewsRequestBuilder()
                .withSymbol(ticker)
                .withWorldNews()
                .build());
    }

    public List<Quote> getTopPerformers() {
        log.debug("Request to get top performers - from IEX");
        List<Quote> posQuotes = iexTradingClient.executeRequest(new ListRequestBuilder()
                .withListType(ListType.GAINERS)
                .build());
        List<Quote> negQuotes = iexTradingClient.executeRequest(new ListRequestBuilder()
                .withListType(ListType.LOSERS)
                .build());

        List<Quote> quotes = new ArrayList<>();
        quotes.addAll(posQuotes);
        quotes.addAll(negQuotes);
        Collections.shuffle(quotes);
        return quotes;
    }

    public BigDecimal getStockPrice(String symbol) {
        log.debug("Request to get price for stock: {}", symbol);
        Quote quote = iexTradingClient.executeRequest(new QuoteRequestBuilder()
            .withSymbol(symbol)
            .build());

        if ( quote != null )
            return quote.getLatestPrice();
        return new BigDecimal(0);
    }

    public Company getAsset(String symbol) {
        log.debug("Request to get Company Data for Asset: {}", symbol);
        try {
            return iexTradingClient.executeRequest(new CompanyRequestBuilder()
                    .withSymbol(symbol)
                    .build());
        } catch (IEXTradingException e) {
            return null;
        }
    }

    public Quote getQuoteForAsset(String symbol) {
        log.debug("Request to get Quote for {}", symbol);
        try {
            return iexTradingClient.executeRequest(new QuoteRequestBuilder()
                .withSymbol(symbol)
                .build());
        } catch (IEXTradingException e) { return null; }
    }

    public Logo getCompanyLogo(String symbol) {
        log.debug("Request to get Logo for Company: {}", symbol);
        return iexTradingClient.executeRequest(new LogoRequestBuilder()
                .withSymbol("AAPL")
                .build());
    }

}
