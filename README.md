

# WhatsApp4Java

Send and listen messages through WhatsApp from a Java Program.

This is and updated Version of JavaWhatsApp.
This is done thanks to Margaruga: https://github.com/Margaruga/JavaWhatsApp

This is done thanks to Yowsup: https://github.com/tgalal/yowsup

# Steps

  * Register your phone number in yowsup, follow these instructions.
  https://github.com/tgalal/yowsup/wiki/yowsup-cli-2.0

  * Fill the fields in yowsup/yowsup-cli.config
- cc=
- phone=
- id=
- password=

  * The Whatsapp folder is a Netbeans Maven project that contains a package with all the
  necessary. It also has a demo to show how it works.

    ```java
    package com.nqysit.whatsapp;

    import java.util.ArrayList;


    public class EchoDemo {

        public static void main(String[] args) throws ClientIsNotInit {

            boolean run = true;
            Client client = new Client("/usr/bin/python", "Java4Whatsapp/yowsup/yowsupclient.py");
            client.init();
            while (run) {
                client.listenIncomingMessages();
                ArrayList<Contact> unreaden = client.getUnseenContacts();
                if (!unreaden.isEmpty()) {
                    for (Contact ur : unreaden) {
                        String from = ur.getNumber();
                        String text = ur.getUnseenMessage();
                        System.out.println(from + "-" + text);
                        if (text.equals("stop")) {
                            client.killit();
                            run = false;
                            break;
                        }
                        client.sendMessage(from, text);
                    }
                } else {
                    System.out.println("There are not new messages");
                }
            }
        }
    }


# Updates

- 2.0 : Cleaned up the Code. The Client now has to be initialized.  