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

import com.triippztech.cashvest.config.ApplicationProperties;
import com.triippztech.cashvest.domain.PriceAlert;
import com.triippztech.cashvest.domain.User;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.application.Platform;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final UserService userService;
    private final AlertService alertService;
    private final IEXService iexService;
    private final ApplicationProperties applicationProperties;
    private final JavaMailSender javaMailSender;

    public NotificationService(UserService userService, AlertService alertService, IEXService iexService,
                               ApplicationProperties applicationProperties, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.alertService = alertService;
        this.iexService = iexService;
        this.applicationProperties = applicationProperties;
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(initialDelay = 60, fixedDelay = 60000)
    public void scan() {
        List<PriceAlert> alerts = alertService.getAll();
        if ( alerts.isEmpty() ) {
            log.info("No PriceAlerts saved at this time");
            return;
        }

        for ( PriceAlert priceAlert : alerts ) {
            if ( isAlertTriggered ( priceAlert ) )
                sendAlert ( priceAlert );
        }

    }

    private Message createMessage(PriceAlert alert, String message) {
        Twilio.init(applicationProperties.getTwilioConfig().getSid(), applicationProperties.getTwilioConfig().getAuthToken());

        return Message.creator(
                new PhoneNumber(alert.getAlertUser().getPhoneNumber()),
                new PhoneNumber(applicationProperties.getTwilioConfig().getFromNumber()),
                message)
                .create();
    }

    private boolean isAlertTriggered(PriceAlert alert) {
        BigDecimal currentPrice = iexService.getStockPrice( alert.getAlertStock().getSymbol() );
        alert.setCurrentPrice(currentPrice);

        switch ( alert.getAlertThresholdType() ) {
            case ABOVE:
                return currentPrice.compareTo(alert.getPrice()) > 0;
            case BELOW:
                return currentPrice.compareTo(alert.getPrice()) < 0;
            case EQUALS:
                return currentPrice.setScale(0, RoundingMode.UP)
                        .compareTo(alert.getPrice().setScale(0, RoundingMode.UP)) == 0;
            default:
                return false;
        }
    }

    private void sendAlert(PriceAlert priceAlert) {
        switch ( priceAlert.getMethod() ) {
            case PUSH_NOTIFICATION:
                sendPushNotification ( priceAlert );
                break;
            case SMS:
                sendSMS ( priceAlert );
                break;
            case ALL_METHODS:
                sendAll ( priceAlert );
                break;
            default:
                sendEmail ( priceAlert );
                break;
        }
        deleteAlert(priceAlert);
    }

    private void deleteAlert(PriceAlert priceAlert)
    {
        log.debug("Deleting PriceAlert: {}", priceAlert);
        User user = priceAlert.getAlertUser();
        user.removePriceAlert(priceAlert);
        userService.save(user);
        alertService.delete(priceAlert);
    }
    private void sendSMS(PriceAlert priceAlert)
    {
        log.info("Sending PriceAlert SMS for Stock:{} to User:{}",
                priceAlert.getAlertStock().getSymbol(),
                priceAlert.getAlertUser().getFullName());

        String alertText = priceAlert.getAlertStock().getSymbol()
                + " is "
                + priceAlert.getAlertThresholdType().toString()
                + " your Price Alert of $"
                + priceAlert.getPrice()
                + ".\n\nCurrent Price: $"
                + priceAlert.getCurrentPrice();

        createMessage(priceAlert, alertText);

    }
    private void sendEmail(PriceAlert priceAlert)
    {
        log.info("Sending PriceAlert Email for Stock:{} to User:{}",
                priceAlert.getAlertStock().getSymbol(),
                priceAlert.getAlertUser().getFullName());

        String alertText = priceAlert.getAlertStock().getSymbol()
                + " is "
                + priceAlert.getAlertThresholdType().toString()
                + " your Price Alert of $"
                + priceAlert.getPrice()
                + ".\n\nCurrent Price: $"
                + priceAlert.getCurrentPrice();

        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(priceAlert.getAlertUser().getEmail());
        mailMessage.setSubject("CashVest Price Alert!!");
        mailMessage.setText(alertText);
        mailMessage.setFrom("CashVest@triippztech.com");

        javaMailSender.send(mailMessage);
    }
    private void sendPushNotification(PriceAlert priceAlert)
    {
        log.info("Sending PriceAlert Push Notification for Stock:{} to User:{}",
                priceAlert.getAlertStock().getSymbol(),
                priceAlert.getAlertUser().getFullName());

        String alertText = priceAlert.getAlertStock().getSymbol()
                + " is "
                + priceAlert.getAlertThresholdType().toString()
                + " your Price Alert of $"
                + priceAlert.getPrice()
                + ".\n\nCurrent Price: $"
                + priceAlert.getCurrentPrice();

        Platform.runLater( () -> {
            Notifications.create()
                    .title("CashVest - Price Alert!")
                    .text(alertText)
                    .darkStyle()
                    .hideAfter(Duration.seconds(5))
                    .showInformation();
        });

    }
    private void sendAll(PriceAlert priceAlert)
    {
        sendPushNotification(priceAlert);
        sendEmail(priceAlert);
        sendSMS(priceAlert);
    }
}
