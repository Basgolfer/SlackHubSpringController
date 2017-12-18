package slackhubapi.demo.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;
    private String message;
    private Date messageTimeStamp;
    private Long userId; //Id of the user that created the message.

    public Message(Long messageId, String message, Date messageTimeStamp, Long userId) {
        this.messageId = messageId;
        this.message = message;
        this.messageTimeStamp = messageTimeStamp;
        this.userId = userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageTimeStamp() {
        return messageTimeStamp;
    }

    public void setMessageTimeStamp(Date messageTimeStamp) {
        this.messageTimeStamp = messageTimeStamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", message='" + message + '\'' +
                ", messageTimeStamp=" + messageTimeStamp +
                ", userId=" + userId +
                '}';
    }
}


