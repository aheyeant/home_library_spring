package cz.cvut.kbss.ear.homeLibrary.model;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User extends AbstractIdentifiableObject {
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
    @Enumerated(EnumType.STRING)
    private EUserRole role;

    //todo
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Library library;

    @OneToMany(mappedBy = "user")
    private List<BookRent> bookRents;

    @ManyToMany(mappedBy = "users")
    @OrderBy("title")
    private List<Chat> chats;

    public User() {
        this.role = EUserRole.GUEST;
    }

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

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    public void erasePassword() {
        this.password = null;
    }

    public EUserRole getRole() {
        return role;
    }

    public void setRole(EUserRole role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return role == EUserRole.ADMIN;
    }

    public Library getLibrary() {
        return library;
    }

    //todo
    public void setLibrary(Library library){
        if (library == null) {
            throw new IllegalArgumentException("User::setLibrary(library == null)");
        }
        library.setUser(this);
        this.library = library;
    }

    public List<BookRent> getBookRents() {
        return bookRents;
    }

    //todo
    public void setBookRents(List<BookRent> bookRents) {
        this.bookRents = bookRents;
    }

    //todo
    public void addBookRent(BookRent bookRent){
        Objects.requireNonNull(bookRent);
        if (bookRents == null) {
            this.bookRents = new ArrayList<>();
        }
        bookRents.add(bookRent);
    }

    //todo
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

    //todo
    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    //todo
    public void addChat(Chat chat){
        Objects.requireNonNull(chat);
        if (chats == null) {
            this.chats = new ArrayList<>();
        }
        chats.add(chat);
    }

    //todo
    public void removeChat(Chat chat){
        Objects.requireNonNull(chat);
        if (chats == null) {
            return;
        }
        chats.removeIf(b -> Objects.equals(b.getId(), chat.getId()));
    }

}
