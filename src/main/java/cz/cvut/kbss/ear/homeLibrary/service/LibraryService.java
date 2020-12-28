package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.dao.LibraryDAO;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LibraryService {

    private final LibraryDAO libraryDAO;


    @Autowired
    public LibraryService(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }

    @Transactional(readOnly = true)
    public Library find(Integer id){
        return libraryDAO.find(id);
    }

    @Transactional(readOnly = true)
    public List<Library> findAll(){
        return libraryDAO.findAll();
    }

    @Transactional(readOnly = true)
    public List<Library> findAllVisible() { return libraryDAO.findAllVisible(); }

    @Transactional
    public void persist(Library library) {
        Objects.requireNonNull(library);
        Objects.requireNonNull(library.getUser());
        libraryDAO.persist(library);
    }

    @Transactional
    public void update(Library library) {
        Objects.requireNonNull(library);
        Objects.requireNonNull(library.getUser());
        libraryDAO.update(library);
    }

    //todo remove books;
    @Transactional
    public void remove(Library library) {
        Objects.requireNonNull(library);
        libraryDAO.remove(library);
    }

    @Transactional
    public void hide(Library library) {
        library.setVisible(false);
        libraryDAO.update(library);
    }

    @Transactional
    public void show(Library library) {
        library.setVisible(true);
        libraryDAO.update(library);
    }
}
