package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.raf.demo.model.User2;

@Repository
public interface User2Repository extends JpaRepository<User2, Long> {
     User2 findByEmail(String email);



}
