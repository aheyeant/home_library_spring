package cz.cvut.kbss.ear.homeLibrary.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="bookrents")
public class BookRent extends AbstractIdentifiableObject {
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
    @Column(columnDefinition = "TINYINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean archive;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private User user;

    public int getOwnerId() { return ownerId; }

    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

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

    public boolean isArchive() { return archive; }

    public void setArchive(boolean archive) { this.archive = archive; }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //todo toString
}
