/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.robertduda.whatsapp;

import java.util.ArrayList;

/**
 *
 * @author Robert Duda, Margaruga
 *
 */
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
