package cz.cvut.kbss.ear.homeLibrary.model;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Entity
@Table(name="books")
public class Book extends AbstractEntity{
    @Basic(optional = false)
    @Column(nullable = false)
    private String title;

    @Basic(optional = false)
    @Column(nullable = false)
    private String author;

    private String ISBN;

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean available;

    private Date availableFrom;

    @ManyToOne
    private Library library;

    @ManyToMany
    private List<Tag> tags;

    @OneToMany(mappedBy = "book")
    private List<BookRent> rents;


    public Book() {}

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

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
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

    @Override
    public String toString() {
        return "Book";
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
}
