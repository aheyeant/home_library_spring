package cz.cvut.kbss.ear.homeLibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="tags")
@NamedQueries({
        @NamedQuery(name = "Tag.findByText", query = "SELECT t FROM Tag t WHERE t.text = :text")
})
public class Tag extends AbstractIdentifiableObject {
    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String text;    // in mySQL: VARCHAR(255)

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book> books;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Tag.class)) {
            return ((Tag) obj).text.equals(this.text);
        }
        throw new IllegalArgumentException("Tag::equals -> Different types");
    }

    @JsonIgnore
    public boolean validate() {
        return (text != null);
    }
}
