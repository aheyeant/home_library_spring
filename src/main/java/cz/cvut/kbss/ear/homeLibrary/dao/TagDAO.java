package cz.cvut.kbss.ear.homeLibrary.dao;


import cz.cvut.kbss.ear.homeLibrary.model.Tag;
import org.springframework.stereotype.Repository;

@Repository
public class TagDAO extends BaseDAO<Tag>{
    public TagDAO() {
        super(Tag.class);
    }
}
