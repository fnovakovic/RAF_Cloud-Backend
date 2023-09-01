package rs.raf.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.User2;
import rs.raf.demo.services.User2Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "MY JWT SECRET";

    private final User2Service user2Service;

    public JwtUtil(User2Service user2Service) {
        this.user2Service = user2Service;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        User2 usr = user2Service.findByEmail(username);
        claims.put("CreateUserPermission", usr.getCreateUser());
        claims.put("ReadUserPermission", usr.getReadUser());
        claims.put("UpdateUserPermission", usr.getUpdateUser());
        claims.put("DeleteUserPermission", usr.getDeleteUser());
        claims.put("CreateMachinePermission", usr.getCreateMachine());
        claims.put("SearchMachinePermission", usr.getSearchMachine());
        claims.put("StartMachinePermission", usr.getStartMachine());
        claims.put("StopMachinePermission", usr.getStopMachine());
        claims.put("RestartMachinePermission", usr.getRestartMachine());
        claims.put("DestroyMachinePermission", usr.getDestroyMachine());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
