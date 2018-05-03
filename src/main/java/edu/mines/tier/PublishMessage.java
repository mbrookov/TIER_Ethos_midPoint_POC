package edu.mines.tier;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
  publish a message to RabbitMQ
 */
public class PublishMessage {
    private ConnectionFactory factory;
    private Connection conn;
    private Channel channel;
    private String username = "guest";
    private String password = "guest";
    private String hostname = "localhost";
    private String virtualHost = "/";
    private String exchangeName = "midpoint";
    private String routingKey = "midpoint_hr";

    public PublishMessage() {
        factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostname);

        try {
            conn = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            channel = conn.createChannel();
            boolean connected = channel.isOpen();
            if (connected) {
                System.out.println("Testing connection succeeded");
            } else {
                System.out.println("Testing connection failed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] messageBodyBytes = "Hello, world!".getBytes();
        try {
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
            System.out.println("Sent! " + exchangeName + " " + routingKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
