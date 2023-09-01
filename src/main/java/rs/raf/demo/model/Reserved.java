package rs.raf.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Getter
@Setter
public class Reserved {
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String dateAndTime;

}
