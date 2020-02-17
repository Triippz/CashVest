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

import com.triippztech.cashvest.interfaces.TestingInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TestHarness {
    private final Logger log = LoggerFactory.getLogger(TestHarness.class);


    private User user;
    private Portfolio portfolio;
    private Stock stock;
    private ArrayList<Asset> arrayList;

    public TestHarness() {
        this.user = new User(
                "tester",
                "testpass",
                "JohnTester",
                "DoeTester",
                "john.doe@test.com",
                "+18888888888",
                "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        this.portfolio = new Portfolio(
                "My cool portfolio!",
                "Some random description, I guess",
                user,
                new Stock("Apple Inc.", "AAPL"));
        this.stock = new Stock(
                "Apple Inc.",
                "AAPL"
        );

        this.arrayList = new ArrayList<>();
        arrayList.add(
                new Bond("U.S. 10 Year Treasury Note", "TMUBMUSD10Y")
        );
        arrayList.add(
                new Stock("Tesla Inc.", "TSLA")
        );
        arrayList.add(
                new Bond("Australia 9 Year Government Bond", "TMBMKAU-09Y")
        );
        arrayList.add(
                new Stock("Apple Inc.", "AAPL")
        );

        setUser();
        testPortfolio();
        testClassHierarchy();
        testInterface();
    }

    private void setUser() {
        this.user.setLogin("tester");
        this.user.setPassword("testpass");
        this.user.setFirstName("JohnTester");
        this.user.setLastName("DoeTester");
        this.user.setEmail("john.doe@test.com");
        this.user.setPhoneNumber("+18888888888");
        this.user.setImageUrl("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        this.user.setId(1L);
        this.user.setIsSignedIn(true);
        this.user.addPortfolio(this.portfolio);

        log.info("Login: {}\n" +
                 "Password: {}\n" +
                 "First Name: {}\n" +
                "Last Name: {}\n" +
                "Email: {}\n" +
                "Phone Number: {}\n" +
                "Image URL: {}\n" +
                "ID: {}\n" +
                "IsSignedIn: {}\n" +
                "Portfolio: {}",
                this.user.getLogin(),
                this.user.getPassword(),
                this.user.getFirstName(),
                this.user.getLastName(),
                this.user.getEmail(),
                this.user.getPhoneNumber(),
                this.user.getImageUrl(),
                this.user.getId(),
                this.user.getIsSignedIn(),
                this.user.getPortfolios()
        );
    }

    private void testPortfolio() {
        this.portfolio.setName("LOL");
        this.portfolio.setDescription("Coolllll");
        this.portfolio.setId(1L);
        this.portfolio.setPortfolioUser(this.user);
        this.portfolio.setTotalValue(new BigDecimal("00002"));
        this.portfolio.addStock(this.stock);

        log.info("ID: {}\n" +
                "Name: {}\n" +
                "Description: {}\n" +
                "Total Value: {}\n" +
                "User: {}\n" +
                "Stock: {}",
                this.portfolio.getId(),
                this.portfolio.getName(),
                this.portfolio.getDescription(),
                this.portfolio.getTotalValue(),
                this.portfolio.getPortfolioUser(),
                this.portfolio.getPortfolioStocks());
    }

    private void testClassHierarchy() {
        arrayList.add(
                new Bond("U.S. 10 Year Treasury Note", "TMUBMUSD10Y")
        );
        arrayList.add(
                new Stock("Tesla Inc.", "TSLA")
        );
        arrayList.add(
                new Bond("Australia 9 Year Government Bond", "TMBMKAU-09Y")
        );
        arrayList.add(
                new Stock("Apple Inc.", "AAPL")
        );

        for ( Asset asset : arrayList )
            log.warn(asset.getSymbolTag());
    }

    private void testInterface() {
        ArrayList<Bond> bonds = new ArrayList<>();
        bonds.add(
                new Bond("U.S. 10 Year Treasury Note", "TMUBMUSD10Y")
        );
        bonds.add(
                new Bond("Australia 9 Year Government Bond", "TMBMKAU-09Y")
        );

        for ( Bond bond : bonds ) {
            log.warn(bond.assetRating());
            log.warn(bond.assetBuyRating());
        }
    }
}
