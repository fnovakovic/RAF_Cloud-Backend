package rs.raf.demo.bootstrap;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Component
public class BootstrapData implements CommandLineRunner {


    private final User2Repository user2Repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData( User2Repository user2Repository, PasswordEncoder passwordEncoder) {
        this.user2Repository = user2Repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        User2 user2 = new User2();
        user2.setFirst_name("user1FirstName");
        user2.setLast_name("user1LastName");
        user2.setEmail("user1@gmail.com");
        user2.setCreateUser("1");
        user2.setReadUser("1");
        user2.setUpdateUser("1");
        user2.setDeleteUser("1");
        user2.setCreateMachine("1");
        user2.setDestroyMachine("1");
        user2.setRestartMachine("1");
        user2.setSearchMachine("1");
        user2.setStartMachine("1");
        user2.setStopMachine("1");

//        user2.setPassword(this.passwordEncoder.encode("user1"));
        user2.setPassword(Base64.getEncoder().encodeToString("user1".getBytes(StandardCharsets.UTF_8)));
        user2Repository.save(user2);


        User2 user3 = new User2();
        user3.setFirst_name("fica");
        user3.setLast_name("novakovic");
        user3.setEmail("fica@gmail.com");
        user3.setCreateUser("1");
        user3.setReadUser("1");
        user3.setUpdateUser("0");
        user3.setDeleteUser("0");
        user3.setCreateMachine("1");
        user3.setDestroyMachine("1");
        user3.setRestartMachine("1");
        user3.setSearchMachine("1");
        user3.setStartMachine("1");
        user3.setStopMachine("1");
       // user3.setPassword(this.passwordEncoder.encode("user1"));
        user3.setPassword(Base64.getEncoder().encodeToString("user1".getBytes(StandardCharsets.UTF_8)));
        user2Repository.save(user3);



        System.out.println("Data loaded!");
    }
}
