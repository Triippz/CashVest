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

import com.triippztech.cashvest.domain.PriceAlert;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Creates a new price alert
     * @param priceAlert alert to save
     * @return Updated price alert
     */
    public PriceAlert createAlert(PriceAlert priceAlert) {
        log.debug("Request to create new PriceAlert: {}", priceAlert);
        alertRepository.save(priceAlert);
        return priceAlert;
    }

    /**
     * Deletes a price alert
     * @param priceAlert price alert to delete
     */
    public void delete(PriceAlert priceAlert) {
        log.debug("Request to delete PriceAlert:{}", priceAlert);
        alertRepository.deleteById(priceAlert.getId());
    }

    /**
     * Gets all alerts for a User
     * @param user User to get alerts for
     * @return List of alerts
     */
    public List<PriceAlert> getAlertsForUser(User user) {
        log.debug("Request to get price alerts for User:{}", user);
        return alertRepository.findAllByAlertUser(user);
    }

    /**
     * Used to save an Alert
     * @param priceAlert Alert to save
     * @return Updated Alert
     */
    public PriceAlert save(PriceAlert priceAlert) {
        log.debug("Request to save PriceAlert={}", priceAlert);
        return alertRepository.save(priceAlert);
    }

    public List<PriceAlert> getAll() {
        log.debug("Request to get all Price Alerts");
        return alertRepository.findAll();
    }
}
