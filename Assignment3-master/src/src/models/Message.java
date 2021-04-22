package models;

import java.io.Serializable;

public class Message<T> implements Serializable {
    private final MessageType type;
    private final T content;

    public Message(MessageType type, T content) {
        this.type = type;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public T getContent() {
        return content;
    }

    public enum MessageType {
        STRING, EXIT, ERROR, STATUS, TICKERS, TRADES, START_TIMER, LOG
    }
}