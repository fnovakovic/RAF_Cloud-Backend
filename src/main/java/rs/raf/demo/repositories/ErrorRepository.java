package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User2;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorMessage, Long> {




}
