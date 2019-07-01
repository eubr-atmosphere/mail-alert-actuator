package actuator.core;

import actuator.DemoActuatorApplication;
import eubr.atmosphere.tma.actuator.Actuator;
import eubr.atmosphere.tma.actuator.ActuatorPayload;
import eubr.atmosphere.tma.actuator.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mailActuator")
public class MailActuator implements Actuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailActuator.class);

    @RequestMapping("/initialize")
    public void initialize() {
        // This is for the conceptual model
        LOGGER.info("Initialize :)");
    }

    @RequestMapping(value = "/act", method = RequestMethod.POST)
    public void act(@RequestBody ActuatorPayload actuatorPayload) {
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

        try {
            new EmailSender(sender, receivers, senderPasswd, subject, message, smtpHost).sendEmail();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }


    }

    @RequestMapping("/register")
    protected void register(Actuator callback) {
        // This is for the conceptual model
        LOGGER.info("Register!");
    }
}
