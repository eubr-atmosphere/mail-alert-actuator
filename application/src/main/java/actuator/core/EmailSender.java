package actuator.core;

import actuator.constants.Constants;
import actuator.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    private String sender;
    private String receivers;
    private String password;
    private String subject;
    private String body;
    private String smtpHost;

    public EmailSender(String sender, String receivers, String password, String subject, String body, String smtpHost) throws Exception {
        Properties properties = PropertiesUtil.readProperties(
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + "private/" + Constants.CONF_FILE_NAME);
        this.sender = sender;
        this.receivers = receivers;
        this.password = password;
        this.subject = subject;
        this.body = body;
        this.smtpHost = smtpHost == null ? properties.getProperty("host") : smtpHost;
    }

    private static Session createSessionMail(String sender, String password, String host) {
        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.port", 465);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        return session;
    }

    public void sendEmail() {
        Session session = EmailSender.createSessionMail(sender, password, smtpHost);
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(sender));

            Address[] toUser = InternetAddress.parse(receivers);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(subject);
            message.setContent(body, "text/html");

            Transport.send(message);
            LOGGER.info("Email sent successfully");
        } catch (MessagingException m){
            LOGGER.error("Error while sending email");
            LOGGER.error(m.getMessage());
        }

    }
}
