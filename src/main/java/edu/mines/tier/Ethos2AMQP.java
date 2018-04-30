package edu.mines.tier;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import jdk.nashorn.internal.runtime.JSONFunctions;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Ethos2AMQP {
    public static void main(String[] args) {
        //readParseJson("test.json");
        //System.exit(0);

        KeyMaster keyMaster = new KeyMaster();
        String b = keyMaster.getBearer();
        System.out.println("Bearer: " + b);

        Consumer c = new Consumer();
        String message = c.consume(keyMaster);

        // save a copy for debgging
        File out = new File("test.json");
        try {
            FileUtils.writeStringToFile(out, message, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseJson(message);

        System.exit(0);
    }

    // read json from file, useful for testing
    public static void readParseJson(String filename) {
        /*https://www.codevoila.com/post/65/java-json-tutorial-and-example-json-java-orgjson*/
        File file = new File(filename);
        String message = null;
        try {
            message = FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //JSONObject jsonMessage = new JSONObject(message);
        parseJson(message);
    }

    public static void parseJson(String message) {
        // Ethose consumer sends JSON that starts with an Array
        JSONArray m = new JSONArray(message);
        // Loop over the identity records that come from Banner.
        for (int i = 0; i < m.length(); i++) {
            Person person = new Person();

            JSONObject individual = m.getJSONObject(i);
            System.out.println(m.get(i));
            /*  Test glop used while learning JSON API.
            System.out.println("TOSTRING: " + individual.toString());
            System.out.println("Lengh of individual: " + individual.length() + " Class type: " + individual.getClass());
            for (Iterator<String> it = individual.keys(); it.hasNext(); ) {
                String keys = it.next();
                System.out.println(keys);
            }
            */
            // Look at resource to make sure this is a persons record
            JSONObject resource = individual.getJSONObject("resource");
            if (!resource.getString("name").equals("persons")) {
                System.out.println("Not Persons, skipping " + individual.getString("id"));
                continue;
            }

            // look for content
            if (individual.has("content")) {
                JSONObject content = individual.getJSONObject("content");

                // Look for names
                if (content.has("names")) {
                    JSONObject names = content.getJSONArray("names").optJSONObject(0);
                    person.setFirstName(names.getString("firstName"));
                    person.setLastName(names.getString("lastName"));
                    person.setFormatted(names.getString("fullName"));
                } else {  // skip records that do not have names
                    continue;
                }

                // Get phone numbers
                if (content.has("phones")) {
                    JSONArray phones = content.getJSONArray("phones");
                    for (int c = 0; c < phones.length(); c++) {
                        Phone phone = new Phone();
                        phone.setNumber(phones.getJSONObject(c).getString("number"));
                        if (phones.getJSONObject(c).has("preference"))
                            phone.setType((phones.getJSONObject(c).getString("preference")));
                        else if (phones.getJSONObject(c).has("phoneType"))
                            phone.setType(phones.getJSONObject(c).getString("phoneType"));
                        if (person.phone == null) System.out.println("PHONE is NULL");
                        person.phone.add(phone);
                    }
                }

                // Get Email addresses
                if (content.has("emails")) {
                    JSONArray inEmail = content.getJSONArray("emails");
                    for (int c = 0; c < inEmail.length(); c++) {
                        EMail mail = new EMail();
                        mail.setEmail(inEmail.getJSONObject(c).getString("address"));
                        mail.setType(inEmail.getJSONObject(c).getJSONObject("type").getString("emailType"));
                        person.email.add(mail);
                    }
                }

                // get id numbers
                if (content.has("credentials")) {
                    JSONArray credentials = content.getJSONArray("credentials");
                    for (int c = 0; c < credentials.length(); c++) {
                        String t = credentials.getJSONObject(c).getString("type");
                        if (t.equals("bannerSourcedId"))
                            person.setPidm(credentials.getJSONObject(c).getString("value"));
                        if (t.equals("bannerUdcId"))
                            person.setUDCID(credentials.getJSONObject(c).getString("value"));
                        if (t.equals("bannerId"))
                            person.setId(credentials.getJSONObject(c).getString("value"));
                    }
                }
            }
            makeJson(person);
        }
    }

    // make json and send it to Rabbit MQ
    private static void makeJson(Person person) {
        JSONObject p = new JSONObject();

        // ID number
        p.put("id", person.getId());

        // Names wrapped with an array
        JSONArray names = new JSONArray();
        JSONObject name = new JSONObject();
        name.put("givenName", person.getFirstName());
        name.put("familyName", person.getLastName());
        name.put("formatted", person.getFormatted());
        names.put(name);
        p.put("names", names);

        // email addresses
        if (!person.email.isEmpty()) {
            JSONArray email = new JSONArray();
            for (EMail e : person.email) {
                JSONObject o = new JSONObject();
                o.put("value", e.getEmail());
                o.put("type", e.getType());
                email.put(o);
            }
            p.put("emails", email);
        }
        // Phone numbers
        if (!person.phone.isEmpty()) {
            JSONArray phone = new JSONArray();
            for (Phone ph : person.phone) {
                JSONObject o = new JSONObject();
                o.put("value", ph.getNumber());
                phone.put(o);
            }
            p.put("phoneNumbers", phone);
        }

        System.out.println(p.toString(4));
    }
}

