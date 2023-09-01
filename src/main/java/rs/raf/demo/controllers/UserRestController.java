package rs.raf.demo.controllers;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.UpdateUserModel;
import rs.raf.demo.model.User2;
import rs.raf.demo.model.User3;
import rs.raf.demo.responses.LoginResponse;
import rs.raf.demo.services.User2Service;
import rs.raf.demo.utils.JwtUtil;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final User2Service user2Service;
    private User2 currUser;
    private final PasswordEncoder passwordEncoder;
    private List<User2> users;
    private int flag = 0;
    private final JwtUtil jwtUtil;

    public UserRestController(User2Service user2Service, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.user2Service = user2Service;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping(value = "/all",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User2>> getAllStudents(@RequestBody User3 user){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getReadUser().equals("1")){//Provera da li ima permisiju
        List<User2> lista2 = user2Service.findAll();

            for (int i = 0; i < lista2.size() ; i++) {
//                byte[] actualByte = Base64.getDecoder()
//                        .decode(lista2.get(i).getPassword());  NE ZNAM STO SAM OVO KORISTIO PROVERI ALI SAM GA ZAKOMENTARIOSA NAKON UBACIVANJA ZA MASINE JER PUCA
//                String pass = new String(actualByte);
//                lista2.get(i).setPassword(pass);
            }
            return ResponseEntity.ok(lista2);
        }

        return ResponseEntity.status(403).build();
    }


    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody User3 user){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getCreateUser().equals("1")){//Provera da li ima permisiju

            users = user2Service.findAll();

            for (int i = 0; i < users.size() ; i++) {
                if(users.get(i).getEmail().equals(user.getEmail())){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0) {

                String password = this.passwordEncoder.encode(user.getPassword());

                User2 usr = new User2();
                usr.setEmail(user.getEmail());
                usr.setFirst_name(user.getFirst_name());
                usr.setLast_name(user.getLast_name());
                usr.setPassword(Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
                usr.setCreateUser(user.getCreateUser());
                usr.setReadUser(user.getReadUser());
                usr.setDeleteUser(user.getDeleteUser());
                usr.setUpdateUser(user.getUpdateUser());
                usr.setCreateMachine(user.getCreateMachine());
                usr.setSearchMachine(user.getSearchMachine());
                usr.setStartMachine(user.getStartMachine());
                usr.setStopMachine(user.getStopMachine());
                usr.setRestartMachine(user.getRestartMachine());
                usr.setDestroyMachine(user.getDestroyMachine());

                return ResponseEntity.ok(user2Service.save(usr));
            }
            flag = 0;

        }

        return ResponseEntity.status(403).build();
    }

    @PostMapping(value = "/tkn",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getNewToken(@RequestBody User3 user){

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(SecurityContextHolder.getContext().getAuthentication().getName())));
    }

    @PatchMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserModel user){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getUpdateUser().equals("1")){//Provera da li ima permisiju
            user2Service.update(user);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(403).build();

    }

    @DeleteMapping(value = "/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        String emaill[] = email.split("=");
        user2Service.deleteByEmail(emaill[1]);
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/search",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User2> searchCurrUser(@RequestBody User2 user){

        return ResponseEntity.ok(user2Service.findByEmail(user.getEmail()));
    }

    @PostMapping(value = "/check",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User2> check(@RequestBody User2 user){
        currUser =   user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(user2Service.findByEmail(currUser.getEmail()));
    }

}
