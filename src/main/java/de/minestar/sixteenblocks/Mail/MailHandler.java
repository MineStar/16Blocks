package de.minestar.sixteenblocks.Mail;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;

public class MailHandler {

    private Session session;
    private InternetAddress from;
    private InternetAddress to;

    public MailHandler(File dataFolder) {
        loadSettings(dataFolder);
    }

    private void loadSettings(File dataFolder) {
        File configFile = new File(dataFolder, "mailConfig.yml");
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(configFile);
            // Standardized Properties to talk with SMTP Server
            Properties properties = System.getProperties();
            // HOST
            properties.setProperty("mail.smtp.host", config.getString("smtp.host"));
            // PORT
            properties.setProperty("mail.smtp.port", config.getString("smtp.port"));
            // Has an Authentifaction
            properties.setProperty("mail.smtp.auth", "true");
            // Create session
            session = Session.getDefaultInstance(properties, new MailAuthenticator(config.getString("smtp.username"), config.getString("smtp.password")));
            // Get Addresses
            from = new InternetAddress(config.getString("smtp.from"));
            to = new InternetAddress(config.getString("smtp.to"));
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't initiate E-Mail settings!");
        }
    }

    // private class to hold password and user
    private class MailAuthenticator extends Authenticator {
        private String user;
        private String password;

        public MailAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password);
        }
    }

    public boolean sendTicket(Player from, String message) {
        // Send E-Mail with header
        // BUGREPORT - PlayerName
        // and the message plus additional information
        return sendMail("SERVERREPORT - " + from.getName(), addInformation(from, message));
    }

    // add additional information to the message for debugging
    private String addInformation(Player from, String message) {
        StringBuilder sBuilder = new StringBuilder(message);
        sBuilder.append('\n');
        sBuilder.append('\n');
        sBuilder.append("Addional Information about the Player:");
        sBuilder.append('\n');

        // Location information
        sBuilder.append("X = ");
        sBuilder.append(from.getLocation().getX());
        sBuilder.append("Y = ");
        sBuilder.append(from.getLocation().getY());
        sBuilder.append("Z = ");
        sBuilder.append(from.getLocation().getZ());

        return sBuilder.toString();
    }

    // sending the mail
    private boolean sendMail(String subject, String message) {
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(from);
            msg.setSubject(subject);
            msg.setText(message);
            msg.setRecipient(Message.RecipientType.TO, to);
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't send mail!");
            return false;
        }
    }
}
