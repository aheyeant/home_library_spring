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
    @JsonIgnore
    private String password;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EUserRole role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Library library;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<BookRent> bookRents;

    @ManyToMany(mappedBy = "users")
    @OrderBy("title")
    @JsonIgnore
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

    public void setLibrary(Library library){
        if (library == null) {
            this.library = null;
            return;
        }
        if (this.getLibrary() != null) {
            throw new IllegalArgumentException("User::setLibrary(library already exist)");
        }
        library.setUser(this);
        this.library = library;
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

    //todo check
    public void addChat(Chat chat){
        Objects.requireNonNull(chat);
        if (chats == null) {
            this.chats = new ArrayList<>();
        }
        chats.add(chat);
    }

    //todo check
    public void removeChat(Chat chat){
        Objects.requireNonNull(chat);
        if (chats == null) {
            return;
        }
        chats.removeIf(b -> Objects.equals(b.getId(), chat.getId()));
    }

    public User convertTDO() {
        User user = new User();
        user.setId(this.getId());
        user.setFirstName(this.getFirstName());
        user.setSurname(this.getSurname());
        user.setEmail(this.getEmail());
        user.setRole(this.getRole());
        return user;
    }

    @JsonIgnore
    public boolean validate() {
        return (this.firstName != null) && (this.surname != null) && (this.email != null) && (this.password != null) && (this.role != null);
    }

}
