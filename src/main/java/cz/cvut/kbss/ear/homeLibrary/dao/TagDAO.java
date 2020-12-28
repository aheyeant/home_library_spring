package cz.cvut.kbss.ear.homeLibrary.dao;


import cz.cvut.kbss.ear.homeLibrary.model.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
public class TagDAO extends BaseDAO<Tag>{
    public TagDAO() {
        super(Tag.class);
    }

    public Tag findByText(String text) {
        Objects.requireNonNull(text);
        try {
            return em.createNamedQuery("Tag.findByText", Tag.class).setParameter("text", text).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
