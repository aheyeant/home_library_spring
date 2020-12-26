package cz.cvut.kbss.ear.homeLibrary.service;

import cz.cvut.kbss.ear.homeLibrary.dao.LibraryDAO;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void update(Library library) {
        libraryDAO.update(library);
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
