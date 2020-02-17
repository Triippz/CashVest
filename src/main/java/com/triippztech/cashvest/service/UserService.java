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

package com.triippztech.cashvest.service;

import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.triippztech.cashvest.config.Constants.EMAIL_REGEX;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public User save(User user) {
        log.debug("Request to save User: {}", user);
        return userRepository.save(user);
    }
    /**
     * Deletes a user
     * @param user User to delete
     */
    public void deleteUser(User user) {
        log.debug("Request to delete user: {}", user.getLogin());
        this.userRepository.findById(user.getId()).ifPresent( foundUser -> {
            this.userRepository.delete(foundUser);
            log.debug("Deleted user: {}", foundUser);
        });
    }

    /**
     * Updates a users current information.
     * Upon setting, the users information will be
     * flushed to the database
     * @param user updated user
     */
    public void updateUser(User user) {
        log.debug("Updating user: {}", user.getLogin());
        this.userRepository.findById(user.getId())
                .ifPresent( foundUser -> {
                    foundUser.setEmail(user.getEmail());
                    foundUser.setFirstName(user.getFirstName());
                    foundUser.setLastName(user.getLastName());
                    foundUser.setImageUrl(user.getImageUrl());
                    this.userRepository.save(foundUser);
                    log.debug("Changed information for User: {}", foundUser);
                });
    }

    /**
     * Authenticates a user based on username and password
     * @param user The user to persist
     * @param onSuccess Executes an action if user was authenticated successfully
     * @param onExistingUserName Executes an action if the user chose an existing username
     * @param onExistingEmail Executes an action if the user chose an existing email
     */
    public void registerAndAuthenticateUser(User user, Runnable onSuccess, Runnable onExistingUserName,
                                            Runnable onExistingEmail) {
        log.debug("Request to Create and Login user: {}", user);
        user.setLogin(user.getLogin().trim().toLowerCase());
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        this.userRepository.findByLogin(user.getLogin())
                .ifPresentOrElse( founderUser -> {
                    founderUser.setIsSignedIn(false);
                    this.userRepository.save(founderUser);
                    log.debug("Existing User found: {}", founderUser);
                    onExistingUserName.run();
                }, () -> this.userRepository.findByEmail(user.getEmail())
                        .ifPresentOrElse( foundUser -> {
                            foundUser.setIsSignedIn(false);
                            this.userRepository.save(foundUser);
                            log.debug("Existing User found: {}", foundUser);
                            onExistingEmail.run();
                        }, () -> {
                            user.setIsSignedIn(true);
                            this.userRepository.save(user);
                            log.debug("New User created: {}", user);
                            onSuccess.run();
                        }));
    }

    /**
     * Authenticates a user based on username and password
     * @param login Username
     * @param password Password
     * @param onSuccess Executes an action if user was authenticated successfully
     * @param wrongPassword Executes an action if the user was not authenticated successfully
     * @param wrongUsername
     */
    public void authenticateUserByLogin(String login, String password, Runnable onSuccess, Runnable wrongUsername, Runnable wrongPassword) {
        log.debug("Request to log in user by username: {}", login);
        this.userRepository.findByLogin(login.trim().toLowerCase())
                .ifPresentOrElse( foundUser -> {
                    if ( !bCryptPasswordEncoder.matches(password, foundUser.getPassword()) )
                        wrongPassword.run();
                    else
                    {
                        foundUser.setIsSignedIn(true);
                        userRepository.save(foundUser);
                        log.debug("Logged in User: {}", foundUser);
                        onSuccess.run();
                    }
                }, wrongUsername);
    }

    /**
     * Authenticates a user based on username and password
     * @param email Email
     * @param password Password
     * @param onSuccess Executes an action if user was authenticated successfully
     * @param wrongPassword Executes an action if the user was not authenticated successfully
     * @param wrongUsername
     */
    public void authenticateUserByEmail(String email, String password, Runnable onSuccess, Runnable wrongUsername, Runnable wrongPassword) {
        log.debug("Request to log in user by email: {}", email);
        this.userRepository.findByEmail(email.trim().toLowerCase())
                .ifPresentOrElse( foundUser -> {
                    if ( !bCryptPasswordEncoder.matches(password, foundUser.getPassword()) )
                        wrongPassword.run();
                    else
                    {
                        foundUser.setIsSignedIn(true);
                        userRepository.save(foundUser);
                        log.debug("Logged in User: {}", foundUser);
                        onSuccess.run();
                    }
                }, wrongUsername);
    }

    /**
     * Authenticates a user based on username and password
     * @param login Username
     * @param password Password
     * @param onSuccess Executes an action if user was authenticated successfully
     * @param wrongPassword Executes an action if the user was not authenticated successfully
     * @param wrongUsername
     */
    public void authenticateUser(String login, String password, Runnable onSuccess, Runnable wrongUsername, Runnable wrongPassword) {
        log.debug("Request to log in user: {}", login);
        if ( login.matches(EMAIL_REGEX ) )
            authenticateUserByEmail(login, password, onSuccess, wrongUsername, wrongPassword);
        else
            authenticateUserByLogin(login, password, onSuccess, wrongUsername, wrongPassword);
    }


    public User getSignedInUser() {
        log.debug("Request to get signed in User");
        Optional<User> user = userRepository.findUserByIsSignedInIsTrue();
        return user.orElse(null);
    }

    /**
     * Signs out the current user, if applicable
     * @param user User to sign out
     * @param onSuccess Executes an action if user was signed out successfully
     * @param onFailure Executes an action if the user was not signed out successfully
     */
    public void signOutUser(User user, Runnable onSuccess, Runnable onFailure) {
        log.debug("Request to sign out user: {}", user);
        this.userRepository.findById(user.getId())
                .ifPresentOrElse( founderUser -> {
                    founderUser.setIsSignedIn(false);
                    userRepository.save(founderUser);
                    log.debug("Logged out User: {}", founderUser);

                    onSuccess.run();
                }, onFailure);
    }

    public void userLoggedIn(Runnable foundUser, Runnable userNotFound){
        log.debug("Request to find logged in user");
        this.userRepository.findByIsSignedInIsTrue()
                .ifPresentOrElse( user -> {
                    log.debug("Logged in User found: {}", user);
                    foundUser.run();
                }, userNotFound);
    }
}
