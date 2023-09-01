package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.User2;

@Repository
public interface userCrudRepository extends CrudRepository<User2, Long> {


    @Transactional
    @Modifying
    @Query("DELETE FROM User2  WHERE email = :email" )
    void delete(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE User2  SET email = :email,first_name = :first_name, last_name = :last_name,createUser = :createUser, readUser = :readUser, deleteUser = :deleteUser, updateUser = :updateUser,password = :password,createMachine = :createMachine,searchMachine = :searchMachine,startMachine = :startMachine,stopMachine = :stopMachine,restartMachine = :restartMachine,destroyMachine = :destroyMachine WHERE email = :currEmail")
    void update(@Param("currEmail") String currEmail,@Param("email") String email,@Param("first_name") String first_name,@Param("last_name") String last_name,@Param("createUser") String createUser,
                @Param("readUser") String readUser,@Param("deleteUser") String deleteUser,@Param("updateUser") String updateUser,@Param("password") String password,
            @Param("createMachine") String createMachine,@Param("searchMachine") String searchMachine,@Param("startMachine") String startMachine,@Param("stopMachine") String stopMachine,
            @Param("restartMachine") String restartMachine,@Param("destroyMachine") String destroyMachine);


}
