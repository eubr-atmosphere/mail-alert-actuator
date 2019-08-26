package eubr.atmosphere.tma.actuator;

import java.io.IOException;
import java.util.List;

import eubr.atmosphere.tma.actuator.Actuator;
import eubr.atmosphere.tma.actuator.ActuatorPayload;
import eubr.atmosphere.tma.actuator.Configuration;

import eubr.atmosphere.tma.actuator.core.EmailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mailActuator")
public class MailActuator implements Actuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailActuator.class);

    @Override
    @PostMapping(path = "/act")
    public void act(@RequestBody ActuatorPayload actuatorPayload) {
        switch (actuatorPayload.getAction()) {
        case "sendMail":
            sendMail(actuatorPayload);
            break;

        default:
            LOGGER.warn("Not defined action");
            break;
        }
    }

    private void sendMail(ActuatorPayload actuatorPayload) {
        Map<String, Configuration> confs = new HashMap<>();

        for(Configuration conf: actuatorPayload.getConfiguration()) {
            confs.put(conf.getKeyName(), conf);
        }

        String message = confs.get("message").getValue();
        String subject = confs.get("subject").getValue();
        String receivers = confs.get("receiverEmail").getValue();
        String sender = confs.get("senderEmail").getValue();
        String senderPasswd = confs.get("senderPassword").getValue();
        String smtpHost = confs.get("smtpHost") == null ? null : confs.get("smtpHost").getValue();
        int port;

        if(confs.get("port") == null) {
            port = -1;
        } else {
            port = Integer.valueOf(confs.get("port").getValue());
        }

        try {
            new EmailSender(sender, receivers, senderPasswd, subject, message, smtpHost, port).sendEmail();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
