package cz.cvut.kbss.ear.homeLibrary.model;

//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    private String firstName;

    @Basic(optional = false)
    @Column(nullable = false)
    private String surname;

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String email;
    
    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Basic(optional = false)
    @Column(nullable = false)
    private String role; // change to enum


    //todo
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Library library;

    @OneToMany(mappedBy = "user")
    private List<BookRent> bookRents;

    @ManyToMany(mappedBy = "users")
    private List<Chat> chats;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLibrary(Library library){
        if (library == null) {
            if (this.library != null) {
                this.library.setUser(null);
            }
        }
        else {
            library.setUser(this);
        }
        this.library = library;
    }

    public Library getLibrary() {
        return library;
    }

    public List<BookRent> getBookRents() {
        return bookRents;
    }

    public void setBookRents(List<BookRent> bookRents) {
        this.bookRents = bookRents;
    }

    public void addBookRent(BookRent bookRent){
        Objects.requireNonNull(bookRent);
        if (bookRents == null) {
            this.bookRents = new ArrayList<>();
        }
        bookRents.add(bookRent);
    }

    public void removeBookRent(BookRent bookRent){
        Objects.requireNonNull(bookRent);
        if (bookRents == null) {
            return;
        }
        bookRents.removeIf(b -> Objects.equals(b.getId(), bookRent.getId()));
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public void addChat(Chat chat){
        Objects.requireNonNull(chat);
        if (chats == null) {
            this.chats = new ArrayList<>();
        }
        chats.add(chat);
    }

    public void removeChat(Chat chat){
        Objects.requireNonNull(chat);
        if (chats == null) {
            return;
        }
        chats.removeIf(b -> Objects.equals(b.getId(), chat.getId()));
    }







}
