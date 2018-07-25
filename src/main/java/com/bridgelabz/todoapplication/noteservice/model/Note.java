package com.bridgelabz.todoapplication.noteservice.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author yuga
 * @since 17-07-2018
 *        <p>
 *        <b>To provide setter and getter methods to deal with note details</b>
 *        </p>
 */
@Document
public class Note {

	@Id
	private String id;
	private String title;
	private String discription;
	private String colorCode;
	private Date reminder;
	private Date createdDate;
	private Date lastModifiedDate;
	private boolean trashStatus;
	private boolean pinNote;
	private boolean archiveNOte;
	
	private List<Label>listOfLabels;
	
	private String userId;

	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date basicDBObject) {
		this.createdDate = basicDBObject;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date date) {
		this.lastModifiedDate = date;
	}

	public boolean isTrashStatus() {
		return trashStatus;
	}

	public List<Label> getListOfLabels() {
		return listOfLabels;
	}

	public void setListOfLabels(List<Label> listOfLabels) {
		this.listOfLabels = listOfLabels;
	}

	public void setTrashStatus(boolean trashStatus) {
		this.trashStatus = trashStatus;
	}

	public boolean isPinNote() {
		return pinNote;
	}

	public void setPinNote(boolean pinNote) {
		this.pinNote = pinNote;
	}

	public boolean isArchiveNOte() {
		return archiveNOte;
	}

	public void setArchiveNOte(boolean archiveNOte) {
		this.archiveNOte = archiveNOte;
	}
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
}
