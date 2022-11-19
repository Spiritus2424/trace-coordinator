package org.eclipse.trace.coordinator;

import java.io.Serializable;

public class HelloWorldMessage implements Serializable {

    private String message;

    public static HelloWorldMessage of(String s) {
        final var message = new HelloWorldMessage();
        message.setMessage(s);
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
