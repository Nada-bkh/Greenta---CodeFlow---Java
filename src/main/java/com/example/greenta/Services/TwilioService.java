package com.example.greenta.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {
    public static final String ACCOUNT_SID = "AC793093cbcaeb2c5b55cee45eaeb038a9";
    public static final String AUTH_TOKEN = "ecb5fb5565909f97a94aac6370e85f03";
    public void sendSms(String to, String verificationCode) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        PhoneNumber toNumber = new PhoneNumber(to);
        PhoneNumber fromNumber = new PhoneNumber("+16562282650");
        String body = "Your verification code is: " + verificationCode
                + "Please ignore this message if you haven't requested a password change.";
        Message.creator(toNumber, fromNumber, body).create();
    }
}
