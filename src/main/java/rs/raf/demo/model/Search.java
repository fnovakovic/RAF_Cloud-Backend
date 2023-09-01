package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;


@Getter
@Setter
public class Search {
    @Column
    private String name;

    @Column
    private String status;

    @Column
    private String dateFrom;

    @Column
    private String dateTo;



}
