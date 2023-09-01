package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.StatusEnum;
import rs.raf.demo.model.User2;

import java.util.Date;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {

     List<Machine> findMachineByCreatedByAndDestroyAndDestroyIsFalse(User2 id, Boolean a);
     List<Machine> findMachinesByNameContainingAndCreatedByAndDestroyAndDestroyIsFalse(String name,User2 id, Boolean a);
     List<Machine> findMachinesByStatusAndCreatedByAndDestroyAndDestroyIsFalse(StatusEnum name,User2 id, Boolean a);
     List<Machine> findMachinesByNameContainingAndStatusAndCreatedByAndDestroyAndDestroyIsFalse(String name,StatusEnum status,User2 id, Boolean a);
     List<Machine> findMachinesByDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(Date date, User2 id, Boolean a);
     List<Machine> findMachinesByDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(Date date, User2 id, Boolean a);
     List<Machine> findMachinesByNameContainingAndDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(String name,Date date, User2 id, Boolean a);
     List<Machine> findMachinesByStatusAndDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(StatusEnum name,Date date, User2 id, Boolean a);
     List<Machine> findMachinesByStatusAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(StatusEnum name,Date date, User2 id, Boolean a);
     List<Machine> findMachinesByDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(Date date1,Date date2, User2 id, Boolean a);
     List<Machine> findMachinesByStatusAndNameContainingAndDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(StatusEnum status,String name,Date date, User2 id, Boolean a);
     List<Machine> findMachinesByStatusAndNameContainingAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(StatusEnum status,String name,Date date, User2 id, Boolean a);
     List<Machine> findMachinesByStatusAndNameContainingAndDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(StatusEnum status,String name,Date date1,Date date2, User2 id, Boolean a);
     List<Machine> findMachinesByNameContainingAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(String name,Date date, User2 id, Boolean a);
     List<Machine> findMachinesByNameContainingAndDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(String name,Date date1,Date date2, User2 id, Boolean a);
     List<Machine> findMachinesByStatusAndDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(StatusEnum status,Date date1,Date date2, User2 id, Boolean a);

    Machine findMachineByIdEquals(Long id);



    @Transactional
    @Modifying
    @Query("UPDATE Machine  SET active = true WHERE id = :id")
    void updateActiveTrue(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Machine  SET active = false WHERE id = :id")
    void updateActiveFalse(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Machine  SET destroy = true WHERE id = :id and status = :status")
    void destroy(@Param("id") Long id,@Param("status") StatusEnum status);

    @Transactional
    @Modifying
    @Query("UPDATE Machine  SET status =:status1 WHERE id = :id and status = :status2 ")
    void startAndStop(@Param("id") Long id,@Param("status1") StatusEnum status1,@Param("status2") StatusEnum status2);


}
