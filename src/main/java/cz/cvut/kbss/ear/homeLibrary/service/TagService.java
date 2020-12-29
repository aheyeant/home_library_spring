package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.dao.TagDAO;
import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    private final TagDAO tagDAO;


    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Transactional(readOnly = true)
    public Tag find(Integer id) {
        return tagDAO.find(id);
    }

    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Tag findByText(String text) {
        return tagDAO.findByText(text);
    }

    @Transactional
    public void persist(Tag tag){
        tagDAO.persist(tag);
    }

    @Transactional
    public void update(Tag tag) {
        tagDAO.update(tag);
    }

    @Transactional
    public void remove(Tag tag) {
        tagDAO.remove(tag);
    }


}
