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

import com.triippztech.cashvest.CashvestApplication;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CashvestApplication.class)
class UserServiceTest {

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final Boolean DEFAULT_IS_SIGNED_IN = true;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User user;


    @BeforeEach
    public void init() {
        user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(RandomStringUtils.random(60));
        user.setIsSignedIn(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
    }

    @Test
    @Transactional
    public void whenSaveUserReturnUserTest() {
        user = userService.save(user);
        assertThat(user.getLogin()).isSameAs(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isSameAs(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isSameAs(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isSameAs(DEFAULT_EMAIL);
        assertThat(user.getImageUrl()).isSameAs(DEFAULT_IMAGEURL);
        assertThat(user.getIsSignedIn()).isSameAs(DEFAULT_IS_SIGNED_IN);
    }

    @Test
    public void testGetId() {
        String fName = this.user.getFirstName();
        assertEquals(fName, DEFAULT_FIRSTNAME);


    }

    @Test
    @Transactional
    public void updateUserTest() {
        String updatedFirstName = "Mark";
        String upgdatedEmail = "test@test.com";

        user.setFirstName(updatedFirstName);
        user.setEmail(upgdatedEmail);

        user = userService.save(user);
        assertThat(updatedFirstName).isNotSameAs(DEFAULT_FIRSTNAME);
        assertThat(upgdatedEmail).isNotSameAs(DEFAULT_EMAIL);
    }


    @Test
    void save() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void registerAndAuthenticateUser() {
    }

    @Test
    void authenticateUserByLogin() {
    }

    @Test
    void authenticateUserByEmail() {
    }

    @Test
    void authenticateUser() {
    }

    @Test
    void getSignedInUser() {
    }
}