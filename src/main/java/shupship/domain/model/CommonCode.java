package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * The persistent class for the common_code database table.
 * 
 */
@Entity
@Table(name = "common_code")
@NamedQuery(name = "CommonCode.findAll", query = "SELECT c FROM CommonCode c")
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCode extends BaseObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "class_cd")
	private String classCd;

	private String code;

	private String name;

	private Long priority;

	@Column(name = "ext_value")
	private String extValue;
	
	private String description;

	private Long status;

}