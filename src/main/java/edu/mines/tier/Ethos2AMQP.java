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
        parseJson("test.json");
        System.exit(0);
        KeyMaster keyMaster = new KeyMaster();
        String b = keyMaster.getBearer();
        System.out.println("Bearer: " + b);

        Consumer c = new Consumer();
        String message = c.consume(keyMaster);
        File out = new File ("test.json");
        try {
            FileUtils.writeStringToFile(out, message,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
    public static void parseJson(String filename) {
        /*https://www.codevoila.com/post/65/java-json-tutorial-and-example-json-java-orgjson*/
        File file = new File (filename);
        String message = null;
        try {
            message = FileUtils.readFileToString(file,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //JSONObject jsonMessage = new JSONObject(message);
        JSONArray m = new JSONArray(message);

        Person person = new Person();
        // Loop over the identity records that come from Banner.
        for (int i = 0 ; i < m.length(); i++) {
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
            // Look at resource to make sure this is a perons record
            JSONObject resource = individual.getJSONObject("resource");
            if (!resource.getString("name").equals("persons")) {
                System.out.println("Not Persons, skipping " + individual.getString("id"));
                continue;
            }
            // get Names
            JSONObject content = individual.getJSONObject("content");
            JSONObject names = content.getJSONArray("names").optJSONObject(0);
            if (names==null)continue;
            person.setFirstName(names.getString("firstName"));
            person.setLastName(names.getString("lastName"));
            person.setFormatted(names.getString("fullName"));
            System.out.println(person.getFirstName());
            // Get phone numbers
            JSONArray phones = content.getJSONArray("phones");
            for (int c = 0 ; c < phones.length(); c++) {
                Phone phone = new Phone();
                phone.setNumber(phones.getJSONObject(c).getString("number"));
                phone.setType((phones.getJSONObject(c).getString("preference")));
                if (person.phone == null) System.out.println("PHONE is NULL");
                person.phone.add(phone);
            }
            // Get Email addresses
            JSONArray inEmail = content.getJSONArray("emails");
            for (int c = 0 ; c < inEmail.length(); c++) {
                EMail mail = new EMail();
                mail.setEmail(inEmail.getJSONObject(c).getString("address"));
                mail.setType(inEmail.getJSONObject(c).getJSONObject("type").getString("emailType"));
            }
        }
    }
}

