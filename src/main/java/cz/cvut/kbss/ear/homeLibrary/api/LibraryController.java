package cz.cvut.kbss.ear.homeLibrary.api;

import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.api.exceptions.ValidationException;
import cz.cvut.kbss.ear.homeLibrary.api.util.RestUtils;
import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.security.jwt.JwtAuthenticationManager;
import cz.cvut.kbss.ear.homeLibrary.service.BookService;
import cz.cvut.kbss.ear.homeLibrary.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/library")
public class LibraryController {
//    2DO - LOGGING, - TRY CATCH BLOCKS, - DOC

    private final LibraryService libraryService;

    public final JwtAuthenticationManager jwtAuthenticationManager;


    @Autowired
    public LibraryController(LibraryService libraryService, JwtAuthenticationManager jwtAuthenticationManager) {
        this.libraryService = libraryService;
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }

    // all - get only visible libraries
    // admin - get all libraries
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Library> getAllLibraries(Authentication auth){
        List<Library> libraries;
        if (jwtAuthenticationManager.isAdmin(auth)) {
            libraries = libraryService.findAllAsAdmin();
        } else {
            libraries = libraryService.findAllAsAnonymous();
        }
        return libraries;
    }

    // all - get if library visible
    // admin - get all
    @GetMapping(value = "/{library_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Library getLibrary(Authentication auth, @PathVariable("library_id") Integer id){
        Objects.requireNonNull(id);
        Library library = null;
        if (jwtAuthenticationManager.isAdmin(auth)) {
            library = libraryService.findByIdAsAdmin(id);
        } else {
            library = libraryService.findByIdAsAnonymous(id);
        }
        if (library == null) {
            throw NotFoundException.create(Library.class.getName(), id);
        }
        return library;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public Library getCurrentLibrary(Authentication auth) {
        Integer userId = jwtAuthenticationManager.getUserId(auth);
        Library library = libraryService.findByUserId(userId);
        if (library == null) {
            throw NotFoundException.create(Library.class.getName(), "undefined");
        }
        return library;
    }

    // user - can modify own library
    // admin - can modify all library
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/update/{library_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(Authentication auth, @PathVariable("library_id") Integer id, @RequestBody Library newLibrary){
        Objects.requireNonNull(id);
        Objects.requireNonNull(newLibrary);
        Library oldLibrary = libraryService.findByIdAsAdmin(id);
        if (oldLibrary == null) {
            throw NotFoundException.create(Library.class.getName(), id);
        }
        if (!oldLibrary.getUser().getId().equals(jwtAuthenticationManager.getUserId(auth)) && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("Library_id=" + id);
        }
        if (!newLibrary.validate()) {
            throw new ValidationException(Library.class.getName());
        }
        oldLibrary.setBorrowingPeriod(newLibrary.getBorrowingPeriod());
        oldLibrary.setVisible(oldLibrary.getVisible());
        libraryService.update(oldLibrary);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromHomeURI("api/library/{id}", oldLibrary.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // user - can delete in its own library
    // admin - can be deleted in the whole library
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete/{library_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLibrary(Authentication auth, @PathVariable("library_id") Integer id){
        Objects.requireNonNull(id);
        Library library = libraryService.findByIdAsAdmin(id);
        if (library == null) {
            throw NotFoundException.create(Library.class.getName(), id);
        }
        if (!library.getUser().getId().equals(jwtAuthenticationManager.getUserId(auth)) && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("Library_id=" + id);
        }
        libraryService.remove(library);
    }


    // user - can hide own library
    // admin - can hide all libraries
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{library_id}/hide")
    public ResponseEntity<Void> hide(Authentication auth, @PathVariable("library_id") Integer id){
        Objects.requireNonNull(id);
        Library oldLibrary = libraryService.findByIdAsAdmin(id);
        if (oldLibrary == null) {
            throw NotFoundException.create(Library.class.getName(), id);
        }
        if (!oldLibrary.getUser().getId().equals(jwtAuthenticationManager.getUserId(auth)) && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("Library_id=" + id);
        }
        oldLibrary.setVisible(false);
        libraryService.update(oldLibrary);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromHomeURI("api/library/{id}", oldLibrary.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // user - can show own library
    // admin - can show all libraries
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{library_id}/show")
    public ResponseEntity<Void> show(Authentication auth, @PathVariable("library_id") Integer id){
        Objects.requireNonNull(id);
        Library oldLibrary = libraryService.findByIdAsAdmin(id);
        if (oldLibrary == null) {
            throw NotFoundException.create(Library.class.getName(), id);
        }
        if (!oldLibrary.getUser().getId().equals(jwtAuthenticationManager.getUserId(auth)) && !jwtAuthenticationManager.isAdmin(auth)) {
            throw new AccessDeniedException("Library_id=" + id);
        }
        oldLibrary.setVisible(true);
        libraryService.update(oldLibrary);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromHomeURI("api/library/{id}", oldLibrary.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
