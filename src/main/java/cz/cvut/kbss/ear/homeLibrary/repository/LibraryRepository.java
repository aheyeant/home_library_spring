package cz.cvut.kbss.ear.homeLibrary.repository;

import cz.cvut.kbss.ear.homeLibrary.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Integer> {
}
