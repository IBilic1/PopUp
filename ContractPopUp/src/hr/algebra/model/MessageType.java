/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.utils.ImageUtils;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author HT-ICT
 */
public enum MessageType {
    PICTURE("picture"), MESSAGE("message"),VIDEO("video");

    private final String name;

    public String getName() {
        return name;
    }

    private MessageType(String name) {
        this.name = name;
    }

    public static Optional<MessageType> from(String name) {
        for (MessageType value : values()) {
            if (value.getName().equals(name)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static Optional<Object> byteArrayToObject(byte[] array, MessageType messageType) throws IOException {
        switch (messageType) {
            case MESSAGE:
                return Optional.of(new String(array));
            case PICTURE:
                return Optional.of(ImageUtils.ByteArrayToBufferedImage(array));
            case VIDEO:
                return Optional.of(hr.algebra.utils.MediaUtils.byteArrayToFile(array));
        }
        return Optional.empty();
    }
}
