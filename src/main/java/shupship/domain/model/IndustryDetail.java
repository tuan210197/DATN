package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * The persistent class for the competitor_detail database table.
 */
@Entity
@Table(name = "industry_detail")
@EqualsAndHashCode(callSuper = true)
@Data
public class IndustryDetail extends BaseObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "industry_id")
    private Long industryId;

    @Column(name = "related_to_id")
    private Long relatedToId;


}