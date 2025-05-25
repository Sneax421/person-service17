package ait.cohort55.person.model;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Setter
@Embeddable
public class Address implements Serializable {

    private String city;
    private String street;
    private int building;
}
