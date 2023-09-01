package rs.raf.demo.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.requests.LoginRequest;
import rs.raf.demo.responses.LoginResponse;
import rs.raf.demo.services.User2Service;
import rs.raf.demo.utils.JwtUtil;

import java.nio.charset.StandardCharsets;

import java.util.Base64;



@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final User2Service user2Service;
    private final JwtUtil jwtUtil;


    public AuthController(AuthenticationManager authenticationManager, User2Service user2Service, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;

        this.user2Service = user2Service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            System.out.println("EMAIL JE " + loginRequest.getEmail() + ", PASSWORD JE " + loginRequest.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), Base64.getEncoder().encodeToString(loginRequest.getPassword().getBytes(StandardCharsets.UTF_8))));
        } catch (Exception   e){ //Hashing.sha256().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString()
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getEmail())));
    }

}
