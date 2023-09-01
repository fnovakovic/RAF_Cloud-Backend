package rs.raf.demo.model;

import lombok.Getter;
        import lombok.Setter;

        import javax.persistence.*;
        import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
public class ErrorMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machineId;

//    @NotBlank
    @Column
    private Date date;

//    @NotBlank
    @Column
    private String operation;

//    @NotBlank
    @Column
    private String message;

}
