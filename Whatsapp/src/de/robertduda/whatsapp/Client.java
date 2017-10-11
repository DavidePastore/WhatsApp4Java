package de.robertduda.whatsapp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.robertduda.whatsapp.ClientIsNotInit;
import de.robertduda.whatsapp.Contact;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private boolean isInit = false;
    private Process yowsup = null;
    private BufferedReader input;
    private BufferedWriter output;
    private final HashMap<String, Contact> CONTACTS = new HashMap<>();
    private final String GETJSON = "{\"action\":\"get\"}";
    private final String KILLJSON = "{\"action\":\"kill\"}";

    public Client(String pythonPath, String yowsupPath) {
        try {
            yowsup = Runtime.getRuntime().exec(pythonPath + " " + yowsupPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        input  = new BufferedReader(new InputStreamReader(yowsup.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(yowsup.getOutputStream()));
        isInit = true;
    }

    /**
     * Get the new messages from the listening process
     *
     * @throws ClientIsNotInit
     */
    public void listenIncomingMessages() throws ClientIsNotInit {

        if (isInit) {
            String answer = pipe(GETJSON);
            parseMessages(answer);
        } else {
            throw new ClientIsNotInit("You have not done WhatsApp.init()");
        }

    }

    /**
     *
     * @param to the number of the contact you are sending a message.
     * @param message the message itself.
     * @throws de.robertduda.whatsapp.ClientIsNotInit
     */
    public void sendMessage(String to, String message) throws ClientIsNotInit {

        if (isInit) {
            String sendJSON = "{\"action\":\"send\",\"to\":\"" + to + "\",\"message\":\"" + message + "\"}";
            pipe(sendJSON);
        } else {
            throw new ClientIsNotInit("You have not done WhatsApp.init()");
        }
    }

    /**
     * Kill the backgrounds processes
     */
    public void killit() {
        sendKill();
        yowsup.destroy();
        isInit = false;
    }

    /**
     *
     * @return a list filled with contacts that have unreaden messages
     */
    public ArrayList<Contact> getUnseenContacts() {

        ArrayList<Contact> contacts = new ArrayList<>();

        for (Contact c : CONTACTS.values()) {
            if (c.AreThereNewMessages()) {
                contacts.add(c);
            }
        }
        return contacts;
    }

    private void saveMessage(JsonObject message) {

        String from = message.get("from").toString().split("@")[0];
        from = from.replace("\"", "");
        String content = message.get("message").toString().replace("\"", "");

        if (CONTACTS.containsKey(from)) {

            CONTACTS.get(from).addIncomingMessage(content);

        } else {

            Contact newcontacto = new Contact(from);
            newcontacto.addIncomingMessage(content);
            CONTACTS.put(from, newcontacto);

        }
    }

    private void parseMessages(String messages) {

        JsonElement json = new JsonParser().parse(messages);
        JsonObject jobject = json.getAsJsonObject();
        int i = 0;

        while (true) {

            json = jobject.get(Integer.toString(i));

            if (json != null) {

                JsonObject message = json.getAsJsonObject();
                saveMessage(message);
                i++;

            } else {

                break;
            }
        }
    }

    private void sendKill() {
        pipe(KILLJSON);
    }

    private String pipe(String message) {

        String rcv;
        String tosend = message + "\n";
        try {
            output.write(tosend);
            output.flush();
            rcv = input.readLine();
            return rcv;
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        return "";
    }








}
