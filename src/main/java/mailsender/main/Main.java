package mailsender.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws MalformedURLException {
        EmailService emailService = new EmailService();
//        emailService.registrationConfirmation("matthias.schneglberger@gmx.at", new URL("http://www.google.at"));
//        String mail = "heissenberger.benjamin@sus.htl-grieskirchen.at";
        String mail = "schneglberger.matthias@gmail.com";
        emailService.appointmentConfirmation(mail, 15, LocalDateTime.now(), "Taiskirchen", new URL("http://www.google.at"));
        emailService.cancellationConfirmation(mail, LocalDateTime.now());
        emailService.registrationConfirmation(mail, new URL("http://www.google.at"));


    }


}
