package cz.cvut.kbss.ear.homeLibrary.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="chat")
public class Chat extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    private String title;

    @ManyToMany
    private List<User> users;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        Objects.requireNonNull(user);
        if (users == null) {
            this.users = new ArrayList<>();
        }
        users.add(user);
    }

    public void removeUser(User user){
        Objects.requireNonNull(user);
        if (users == null) {
            return;
        }
        users.removeIf(b -> Objects.equals(b.getId(), user.getId()));
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message){
        Objects.requireNonNull(message);
        if (messages == null) {
            this.messages = new ArrayList<>();
        }

        messages.add(message);
    }

    public void removeMessage(Message message){
        Objects.requireNonNull(message);
        if (messages == null) {
            return;
        }
        messages.removeIf(b -> Objects.equals(b.getId(), message.getId()));
    }
}
