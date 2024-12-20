package com.cdac.mumbai.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class IpKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer regId;
    private String ip;

   
}
