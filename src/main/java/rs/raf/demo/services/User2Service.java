package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.UpdateUserModel;
import rs.raf.demo.model.User2;

import rs.raf.demo.repositories.User2Repository;
import rs.raf.demo.repositories.userCrudRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class User2Service implements UserDetailsService {
    private userCrudRepository userCrudRepository;
    private User2Repository userRepository;


    @Autowired
    public User2Service(User2Repository userRepository,userCrudRepository userCrudRepository) {
        this.userRepository = userRepository;
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User2 myUser = this.userRepository.findByEmail(email);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+email+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), new ArrayList<>());
    }

    public List<User2> findAll() {
        return userRepository.findAll();
    }


    public User2 save(User2 user) {
        return userRepository.save(user);
    }

    public void update(UpdateUserModel user) {

         userCrudRepository.update(user.getCurrEmail(),user.getEmail(),user.getFirst_name(),user.getLast_name(),user.getCreateUser(),user.getReadUser(),user.getDeleteUser(),user.getUpdateUser(),user.getPassword(),user.getCreateMachine(),user.getSearchMachine(),user.getStartMachine(),user.getStopMachine(),user.getRestartMachine(),user.getDestroyMachine());
    }


    public User2 findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void deleteByEmail(String email) {
         userCrudRepository.delete(email);
    }
}
