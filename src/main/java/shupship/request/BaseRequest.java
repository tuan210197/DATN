package shupship.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseRequest implements Serializable, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date lastModifiedDate;
	
	private int page;
	
	private int size;
	
	private String direction;

	private String sortBy;
	
	private String keyword;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
