package rs.raf.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @NotBlank
    @Column
    private String name;

//    @NotBlank
    @Column
    private StatusEnum status;

//    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    @JsonIgnore
//    @Column
    private User2 createdBy; //LONG

//    @NotBlank
    @Column
    private Boolean active;

    @Column
    private Boolean destroy;

    @Column
    private Date date;


}
