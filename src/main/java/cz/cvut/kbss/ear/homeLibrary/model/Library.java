package cz.cvut.kbss.ear.homeLibrary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.kbss.ear.homeLibrary.utils.Constants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="libraries")
@NamedQueries({
        @NamedQuery(name = "Library.findByUserId", query = "SELECT l FROM Library l WHERE l.user.id = :id")
})
public class Library extends AbstractIdentifiableObject {
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer borrowingPeriod;    // days

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean visible;

    @OneToOne(optional = false)
    //@JsonIgnore
    private User user;

    @OneToMany(mappedBy = "library", fetch = FetchType.LAZY)
    //@JsonIgnore
    private List<Book> books;


    public Library() {
        this.borrowingPeriod = Constants.DEFAULT_BORROWING_PERIOD;
        this.visible = true;
    }

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    //todo
    public void addMyBook(Book book){
        Objects.requireNonNull(book);
        if (books == null) {
            this.books = new ArrayList<>();
        }
        books.add(book);
    }

    //todo
    public void removeMyBook(Book book){
        Objects.requireNonNull(book);
        if (books == null) {
            return;
        }
        books.removeIf(t -> Objects.equals(t.getId(), book.getId()));
    }

    //todo toString
}
