package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

/**
 * The persistent class for the product database table.
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "industry")
public class Industry extends BaseObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @ManyToMany(mappedBy = "industries")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Lead> leads;

}