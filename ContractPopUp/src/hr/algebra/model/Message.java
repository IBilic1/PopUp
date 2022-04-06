/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 *
 * @author HT-ICT
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private int idMessage;
    private LocalDateTime time;
    private User sender;
    private User receiver;
    private byte[] message;
    private MessageType type;

    public Message(Integer idMessage, LocalDateTime time, User sender, User receiver, byte[] message, MessageType type) {
        this.idMessage = idMessage;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
    }

    public Message(LocalDateTime time, User sender, User receiver, byte[] message, MessageType type) {
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
    }

    public Integer getIdMessage() {
        return idMessage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return this.getMessage().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message m = (Message) obj;
            return Arrays.equals(m.getMessage(), this.getMessage());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Message{" + "message=" + Arrays.toString(message) + '}';
    }

}
