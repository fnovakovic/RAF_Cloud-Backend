package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Getter
@Setter
public class User3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String first_name;

    @Column
    private String last_name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String createUser;

    @Column
    private String readUser;

    @Column
    private String deleteUser;

    @Column
    private String updateUser;

    @Column
    private String currEmail;

    @Column
    private String createMachine;

    @Column
    private String searchMachine;

    @Column
    private String startMachine;

    @Column
    private String stopMachine;

    @Column
    private String restartMachine;

    @Column
    private String destroyMachine;
}
