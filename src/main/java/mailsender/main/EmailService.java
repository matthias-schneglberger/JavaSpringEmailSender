package mailsender.main;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class EmailService
{
    private JavaMailSender mailSender;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;
    private final String username = "schneglberger.for.testing@gmail.com";
    private final String password = "schneglbergerfortesting";


    public EmailService(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(25);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        this.mailSender = mailSender;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.timeFormatter = DateTimeFormatter.ofPattern("hh:mm");


    }

    private boolean sendMail(MimeMessage message){
        try{
            mailSender.send(message);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    private boolean sendMail(SimpleMailMessage message){
        try{
            mailSender.send(message);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean sendCustomMail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        return sendMail(message);
    }

    public void cancellationConfirmation(String to, LocalDateTime time){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Österreich testet - Storno-Bestätigung");

            String html = getFile("cancellationConfirmation.html");

            html = html.replaceAll("@Date", dateFormatter.format(time));
            html = html.replaceAll("@Time", dateFormatter.format(time));

            message.setContent(html, "text/html; charset=UTF-8");
            sendMail(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void registrationConfirmation(String to, URL url){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Österreich testet - Storno-Bestätigung");

            String html = getFile("registrationConfirmation.html");

            html = html.replaceAll("@Link", url.toString());

            message.setContent(html, "text/html; charset=UTF-8");
            sendMail(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void appointmentConfirmation(String to, int docketNumber, LocalDateTime time, String place, URL url){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Österreich testet - Storno-Bestätigung");

            String html = getFile("appointmentConfirmation.html");

            html = html.replaceAll("@DocketNumber", String.valueOf(docketNumber));
            html = html.replaceAll("@Date", dateFormatter.format(time));
            html = html.replaceAll("@Time", timeFormatter.format(time));
            html = html.replaceAll("@Place", place);
            html = html.replaceAll("@Link", url.toString());

            message.setContent(html, "text/html; charset=UTF-8");
            sendMail(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    private String getFile(String filename){
        try {
            filename = "src/main/resources/" + filename;
            return Files.readAllLines(java.nio.file.Paths.get(filename)).stream().reduce("",(a, b) -> a + b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}