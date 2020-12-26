package cz.cvut.kbss.ear.homeLibrary.repository;

import cz.cvut.kbss.ear.homeLibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("FROM User WHERE email = ?1")
    public User findByEmail(String email);

}

