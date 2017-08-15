package eohmc.selenium.core.helperClasses;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class TestReportEmailHelper extends BaseClassHelper
{

    static String reportPath=currentPath+getConfig().getAutoReportPath();
    static String fromEmail = "mbikosiwapiwe@gmail.com";
    static String passWord = "Siwapiwe@1";
    static String port = "465";
    static String host = "smtp.gmail.com";
    static String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";

    static String[] to={"Siwapiwe.Mbiko@eoh.com"};
    //static String[] cc={"Jonathan.Tshitenda@eoh.com"};

    public static void emailReport()
    {
        //Create object of Property file
        Properties props = new Properties();

        //Set the host smtp address, port, socketFactory and authorization
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.class",socketFactoryClass);
        props.put("mail.smtp.auth", "true");

        //This session object handles the authentication
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(fromEmail, passWord);
            }
        });

        try
        {
            //Create object of MimeMessage class
            Message message = new MimeMessage(session);

            //Set the from address
            message.setFrom(new InternetAddress(fromEmail));

            //Set the recipient addresses
            for(int i=0;i<to.length;i++)//Loop through array of TO emails
            {
                message.addRecipient(Message.RecipientType.TO, new
                        InternetAddress(to[i]));
            }
//            for(int i=0;i<cc.length;i++)//Loop through array of CC emails
//            {
//                message.addRecipient(Message.RecipientType.CC, new
//                        InternetAddress(cc[i]));
//            }

            //Set the email Subject
            message.setSubject("Automation Test Suite Report");

            //Create object to add email content
            BodyPart messageBodyAddEmailText = new MimeBodyPart();//actual message body content
            MimeBodyPart messageBodyAddHtmlReport = new MimeBodyPart();//add report to email

            //Set the body of email
            messageBodyAddEmailText.setText("Hi Jonathan, \n\nThis is a test email generated by the framework.\nPlease see attached report. \n\nRegards \nSiwapiwe");

            //Create data source and pass the filename to send
            DataSource source = new FileDataSource(reportPath);

            //Set the handler
            messageBodyAddHtmlReport.setDataHandler(new DataHandler(source));

            //Set the filename for the email
            messageBodyAddHtmlReport.setFileName("Automation_Test_Suite_Report.html");

            //Add message parts using object of MimeMultipart class
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyAddHtmlReport);//add report part
            multipart.addBodyPart(messageBodyAddEmailText);//add actual message body
            message.setContent(multipart);//Set the email content

            System.out.println("=====Sending report to email======");

            //Send the email
            Transport.send(message);

            System.out.println("==========Email Sent=========");
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }
}