package cz.cvut.kbss.ear.homeLibrary.model;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.lang.annotation.ElementType.METHOD;

@Entity
@Table(name="books")
@NamedQueries({
        @NamedQuery(name = "Book.getAllBooksFromLibrary", query = "SELECT b FROM Book b WHERE b.library.id = :id")
        //@NamedQuery(name = "Book.getAvailableBooksFromLibrary", query = "SELECT b FROM Book b WHERE b.library.id = :id AND b.available"),
        //@NamedQuery(name = "Book.getNotAvailableBooksFromLibrary", query = "SELECT b FROM Book b WHERE b.library.id = :id AND NOT b.available")
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
    private Library library;

    @ManyToMany
    @OrderBy("text")
    private List<Tag> tags;

    @OneToMany(mappedBy = "book")
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

    //todo
    public void addTag(Tag tag){
        Objects.requireNonNull(tag);
        if (tags == null) {
            this.tags = new ArrayList<>();
        }
        tags.add(tag);
    }

    //todo
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

    //todo
    public void addBookRent(BookRent rent){
        Objects.requireNonNull(rent);
        if (rents == null) {
            this.rents = new ArrayList<>();
        }
        rents.add(rent);
    }

    //todo
    public void removeBookRent(BookRent rent){
        Objects.requireNonNull(rent);
        if (rents == null) {
            return;
        }
        rents.removeIf(t -> Objects.equals(t.getId(), rent.getId()));
    }

    //todo toString
    @Override
    public String toString() {
        return "Book";
    }
}
