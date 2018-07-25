package com.bridgelabz.todoapplication.noteservice.model;


import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
/**
 * @since 23/07/2018
 * <p><b></b></p>
 * @author yuga
 *
 */
@Document
public class Label {

	@Id
	private String labelId;
	private String labelName;
	private String userId;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

}
