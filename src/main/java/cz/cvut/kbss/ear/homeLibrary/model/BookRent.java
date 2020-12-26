package cz.cvut.kbss.ear.homeLibrary.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="bookrent")
public class BookRent extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    private int ownerId;

    @Basic(optional = false)
    @Column(nullable = false)
    private Date startDate;

    @Basic(optional = false)
    @Column(nullable = false)
    private Date endDate;

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean archive;

    //todo
    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private User user;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
