package com.finddonor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.finddonor.request.DonorRequest;
import com.finddonor.request.SMSRequest;
import com.finddonor.response.DonorResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class SMSService {

    @Value("${twilio.account.sid}")
    private String accountSID;

    @Value("${twilio.auth.token}")
    private String authToken;
    
    public void sendSMS(DonorResponse donorResponse, SMSRequest smsRequest) {
        List<DonorRequest> donars = donorResponse.getDonars();

        String messageBody = String.format(
            "URGENT BLOOD REQUIREMENT!\n\n" +
            "Blood Group: %s\n" +
            "Units Required: %s\n" +
            "Patient: %s\n\n" +
            "Hospital Details:\n" +
            "Hospital Name: %s\n" +
            "Hospital Address:  %s\n\n" +
            "Contact Details:\n" +
            "Relative Name:  %s\n" +
            "Relative Number:  %s\n\n" +
            "Urgency: %s\n\n" +
            "Your donation can save a life! Please respond ASAP if you can help.",
            smsRequest.getBloodGroup(),
            smsRequest.getUnitsNeeded(),
            smsRequest.getPatientName(),
            smsRequest.getHospitalName(),
            smsRequest.getHospitalAddress(),
            smsRequest.getContactPerson(),
            smsRequest.getContactNumber(),
            smsRequest.getUrgencyLevel()
        );
        for (DonorRequest donar : donars) {
            sendSMS(donar.getMobile(), messageBody);
        }
    }

    public String sendSMS(long number,String messageBody) {
        Twilio.init(accountSID,authToken);

        System.out.println(messageBody);
        Message message = Message.creator(
          new com.twilio.type.PhoneNumber("+91"+number),
          new com.twilio.type.PhoneNumber("+14343943293"),
          messageBody)
        .create();
        System.out.println(message.getSid());
        return message.getSid();
    }
}
