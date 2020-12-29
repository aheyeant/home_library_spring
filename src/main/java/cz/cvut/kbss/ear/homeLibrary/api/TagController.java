package cz.cvut.kbss.ear.homeLibrary.api;

import cz.cvut.kbss.ear.homeLibrary.api.exceptions.NotFoundException;
import cz.cvut.kbss.ear.homeLibrary.model.Book;
import cz.cvut.kbss.ear.homeLibrary.model.Library;
import cz.cvut.kbss.ear.homeLibrary.model.Tag;
import cz.cvut.kbss.ear.homeLibrary.service.TagService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tag")
public class TagController {

    private final TagService tagService;


    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tag getById(@PathVariable Integer id){
        final Tag tag = tagService.find(id);
        if (tag == null) {
            throw NotFoundException.create(Tag.class.getName(), id);
        }
        return tag;
    }



}
