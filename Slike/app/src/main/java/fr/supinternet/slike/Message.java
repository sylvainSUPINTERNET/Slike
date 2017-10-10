package fr.supinternet.slike;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guillaume on 26/09/2017.
 */

@IgnoreExtraProperties
public class Message {

    public String id;
    public String user;
    public String message;

    @Override
    public String toString() {
        return "Message{" +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String id, String user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("message", message);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (id != null ? !id.equals(message1.id) : message1.id != null) return false;
        if (user != null ? !user.equals(message1.user) : message1.user != null) return false;
        return message != null ? message.equals(message1.message) : message1.message == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}