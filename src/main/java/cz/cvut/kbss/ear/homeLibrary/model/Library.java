package cz.cvut.kbss.ear.homeLibrary.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="libraries")
public class Library extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer borrowingPeriod;    // days

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean visible;


    //todo
    @OneToOne
    private User user;


    //todo change to one array
    @OneToMany(mappedBy = "origin")
    private List<Book> myBooks;

    @OneToMany(mappedBy = "current")
    private List<Book> borrowedBooks;

    public Integer getBorrowingPeriod() {
        return borrowingPeriod;
    }

    public void setBorrowingPeriod(Integer borrowingPeriod) {
        this.borrowingPeriod = borrowingPeriod;
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
