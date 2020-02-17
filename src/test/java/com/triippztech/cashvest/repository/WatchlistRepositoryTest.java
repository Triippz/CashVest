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

package com.triippztech.cashvest.repository;

import com.triippztech.cashvest.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class WatchlistRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Watchlist watchlist;

    @BeforeEach
    void setUp () {
        user = new User(
                UserTest.DEFAULT_LOGIN,
                UserTest.DEFAULT_PASSWORD,
                UserTest.DEFAULT_FIRST_NAME,
                UserTest.DEFAULT_LAST_NAME,
                UserTest.DEFAULT_EMAIL
        );
        user.setId(UserTest.DEFAULT_ID);

        watchlist = new Watchlist(
                WatchlistTest.DEFAULT_LIST_NAME
        );
        watchlist.setId(WatchlistTest.DEFAULT_ID);
        watchlist.setWatchlistUser(user);

    }

    @Test
    void findByWatchlistUser() {
    }

    @Test
    void findByListNameAndWatchlistUser() {
    }
}