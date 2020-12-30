package cz.cvut.kbss.ear.homeLibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name="books")
@NamedQueries({
        @NamedQuery(name = "Book.getAllAvailable", query = "SELECT b FROM Book b WHERE b.available=true"),
        @NamedQuery(name = "Book.getAllBorrowed", query = "SELECT b FROM Book b WHERE b.available=false"),
        @NamedQuery(name = "Book.getAllBooksFromLibrary", query = "SELECT b FROM Book b WHERE b.library.id = :id"),
        @NamedQuery(name = "Book.getAvailableBooksFromLibrary", query = "SELECT b FROM Book b WHERE b.library.id = :id AND b.available = true"),
        @NamedQuery(name = "Book.getBorrowedBooksFromLibrary", query = "SELECT b FROM Book b WHERE b.library.id = :id AND b.available = false")
})
public class Book extends AbstractIdentifiableObject {
    @Basic(optional = false)
    @Column(nullable = false)
    private String title;

    @Basic(optional = false)
    @Column(nullable = false)
    private String author;

    @Column(nullable = true)
    private String ISBN;

    @Basic(optional = false)
    @Column(columnDefinition = "TINYINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean available;

    @Column(nullable = true)
    private Date availableFrom;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Library library;

    @ManyToMany
    @OrderBy("text")
    private List<Tag> tags;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookRent> rents;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag){
        Objects.requireNonNull(tag);
        if (tags == null) {
            this.tags = new ArrayList<>();
        }
        tags.add(tag);
    }

    public void removeTag(Tag tag){
        Objects.requireNonNull(tag);
        if (tags == null) {
            return;
        }
        tags.removeIf(t -> Objects.equals(t.getId(), tag.getId()));
    }

    public List<BookRent> getRents() {
        return rents;
    }

    public void setRents(List<BookRent> rents) {
        this.rents = rents;
    }

    public void addBookRent(BookRent rent){
        Objects.requireNonNull(rent);
        if (rents == null) {
            this.rents = new ArrayList<>();
        }
        rents.add(rent);
    }

    public void removeBookRent(BookRent rent){
        Objects.requireNonNull(rent);
        if (rents == null) {
            return;
        }
        rents.removeIf(t -> Objects.equals(t.getId(), rent.getId()));
    }

    @JsonIgnore
    public boolean validate() {
        return (this.title != null) && (this.author != null) && (this.available != null);
    }
}
