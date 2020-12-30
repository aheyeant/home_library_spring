package cz.cvut.kbss.ear.homeLibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="bookrents")
@NamedQueries({
        @NamedQuery(name = "BookRent.findAllByBookId", query = "SELECT br FROM BookRent br WHERE br.book.id = :book_id"),
        @NamedQuery(name = "BookRent.findAllByUserId", query = "SELECT br FROM BookRent br WHERE br.user.id = :user_id"),
        @NamedQuery(name = "BookRent.findRentedByBookId", query = "SELECT br FROM BookRent br WHERE br.book.id = :book_id AND br.archive = false")

})
public class BookRent extends AbstractIdentifiableObject {
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer ownerId;

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

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JsonIgnore
    private Book book;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JsonIgnore
    private User user;

    public int getOwnerId() { return ownerId; }

    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

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

    @JsonIgnore
    public boolean validate() {
        return (ownerId != null) && (startDate != null) && (endDate != null);
    }
}
