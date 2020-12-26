package cz.cvut.kbss.ear.homeLibrary.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="libraries")
public class Library extends AbstractEntity {
    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private Integer borrowLength;

    @Basic(optional = false)
    private Boolean visible;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "origin")
    private List<Book> myBooks;

    @OneToMany(mappedBy = "current")
    private List<Book> borrowedBooks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBorrowLength() {
        return borrowLength;
    }

    public void setBorrowLength(Integer borrowLength) {
        this.borrowLength = borrowLength;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Book> getMyBooks() {
        return myBooks;
    }

    public void setMyBooks(List<Book> books) {
        this.myBooks = books;
    }

    public void addMyBook(Book book){
        Objects.requireNonNull(book);
        if (myBooks == null) {
            this.myBooks = new ArrayList<>();
        }
        myBooks.add(book);
    }

    public void removeMyBook(Book book){
        Objects.requireNonNull(book);
        if (myBooks == null) {
            return;
        }
        myBooks.removeIf(t -> Objects.equals(t.getId(), book.getId()));
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> books) {
        this.borrowedBooks = books;
    }

    public void addBorrowedBook(Book book){
        Objects.requireNonNull(book);
        if (borrowedBooks == null) {
            this.borrowedBooks = new ArrayList<>();
        }
        borrowedBooks.add(book);
    }

    public void removeBorrowedBook(Book book){
        Objects.requireNonNull(book);
        if (borrowedBooks == null) {
            return;
        }
        borrowedBooks.removeIf(t -> Objects.equals(t.getId(), book.getId()));
    }
}
