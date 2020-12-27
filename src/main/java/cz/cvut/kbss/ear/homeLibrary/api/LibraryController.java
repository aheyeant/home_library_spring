package cz.cvut.kbss.ear.homeLibrary.api;

import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {
//    2DO - LOGGING, - TRY CATCH BLOCKS, - DOC

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Library getById(@PathVariable Integer id){
        return this.libraryService.find(id);
    }

/*    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @RequestBody Library newLibrary){
        Library oldLibrary = service.find(id);
        if (newLibrary.getId().equals(oldLibrary.getId())){
            this.service.update(newLibrary);
        }
    }*/

/*    @GetMapping(value = "/{id}/hide")
    public void hide(@PathVariable Integer id){
        Library library = service.find(id);
        service.hide(library);
    }*/

/*    @GetMapping(value = "/{id}/show")
    public void show(@PathVariable Integer id){
        Library library = service.find(id);
        service.show(library);
    }*/
}
