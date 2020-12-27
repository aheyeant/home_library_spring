package cz.cvut.kbss.ear.homeLibrary.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="messages")
public class Message extends AbstractIdentifiableObject {
    @Basic(optional = false)
    @Column(nullable = false)
    private Date timeStamp;   // in mySQL: format "YYYY-MM-DD HH:MM:SS"

    @Basic(optional = false)
    @Column(nullable = false)
    private String text;     // in mySQL: TEXT(1000), max 1000 chars

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Chat chat;


    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    //todo toString
}
