package rs.raf.demo.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class User2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Column
    private String first_name;

    @NotBlank
    @Column
    private String last_name;

    @NotBlank
    @Column
    private String email;

    @NotBlank
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
