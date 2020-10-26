package com.project.springcsvdatabase.service;

import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;
import java.util.Properties;

@Service
public class EmailAttachmentService {

    public void downloadEmailAttachments(String username, String password, String directory) throws MessagingException, IOException {

        Properties properties = new Properties();
        setEmailProperties(properties);
        Session session = Session.getDefaultInstance(properties);

        Store store = session.getStore("imaps");
        store.connect(username, password);

        Folder folderInbox = store.getFolder("INBOX");
        folderInbox.open(Folder.READ_ONLY);

        Message[] arrayMessages = folderInbox.getMessages();
        System.out.println("Downloading...");
        for (int i = arrayMessages.length - 1; i >= 0; i--) {

            Message message = arrayMessages[i];
            Address[] fromAddress = message.getFrom();
            String from = fromAddress[0].toString();

            String contentType = message.getContentType();
            String attachFiles = "";

            if (contentType.contains("multipart")) {

                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {

                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);

                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {

                        String fileName = part.getFileName();
                        attachFiles += fileName + ", ";
                        if (fileName.contains(".csv"))
                        {
                            String csvFile = directory + fileName;
                            part.saveFile(csvFile);
                        }
                    }
                }

                if (attachFiles.length() > 1) {
                    attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                }
            }

            System.out.println("Message #" + (i + 1) + ":");
            System.out.println("\t From: " + from);
            System.out.println("\t Attachments: " + attachFiles + "\n");
        }

        folderInbox.close(false);
        store.close();

        System.out.println("Successfully downloaded");
    }

    private void setEmailProperties(Properties properties){
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");

        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");
        properties.setProperty("mail.imaps.socketFactory.port", "993");
    }

}
