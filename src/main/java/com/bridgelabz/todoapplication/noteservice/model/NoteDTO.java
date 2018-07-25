package com.bridgelabz.todoapplication.noteservice.model;


/**
 * @author yuga
 * @since 19/07/2018
 *  <p>
 * 	<b>DTO can interact between view and controller to send and receive data.</b>
 * </p>
 */
public class NoteDTO {

	private String noteId;
	private String title;
	private String discription;
	private String remainder;
	private String labelName;
	private String color;


	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getRemainder() {
		return remainder;
	}

	public void setRemainder(String remainder) {
		this.remainder = remainder;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

}
