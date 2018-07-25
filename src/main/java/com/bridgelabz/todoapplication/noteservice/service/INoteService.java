package com.bridgelabz.todoapplication.noteservice.service;

import java.text.ParseException;
import java.util.List;

import com.bridgelabz.todoapplication.noteservice.model.Note;
import com.bridgelabz.todoapplication.noteservice.model.NoteDTO;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;

/**
 *@author yuga
* @since 17/07/2018
*<p><b>To connect the controller and mongoDb repository.
*service interface is the media between controller and repository 
*</b></p>
 *
 */
public interface INoteService {

	/**
	 * @param createnotedto
	 *  @param token 
	 * <p><b>To create a note in todo application</b></p>
	 *
	 * @throws ToDoExceptions 
	 * @throws ParseException 
	 */
	void createNote(NoteDTO notedto, String token) throws ToDoExceptions, ParseException;

	/**
	 * @param noteId @param token
	 * <p><b>To delete perticular  note from todo application</b></p>
	 *  
	 * @throws ToDoExceptions 
	 */
	String deleteNote(String noteId, String token) throws ToDoExceptions;

	/**
	 * @param token 
	 * @param noteId
	 * @return Note
	 * <p><b>To read perticular  note from todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	Note readNote(String noteId, String token) throws ToDoExceptions;
	
	
	/**
	 * @param token
	 * @return listOfNotes
	 * <p><b>To read all notes of perticular user id from todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	List<Note> readAllNotes(String token) throws ToDoExceptions;

	/**
	 * @param token
	 * @param updatedto
	 * <p><b>To update note of perticular note id from todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void updateNote(NoteDTO notedto, String token) throws ToDoExceptions;

	/**
	 * @param noteId
	 * @param token
	 * <p><b>To delete note from trash</b></p>
	 * @throws ToDoExceptions 
	 */
	void deleteNoteFromTrash(String noteId, String token) throws ToDoExceptions;

	/**
	 * @param noteId
	 * @param token
	 * <p><b>To restore note from trash</b></p>
	 * @param reminderDate 
	 * @throws ToDoExceptions 
	 */
	void restoreNoteFromTrash(String noteId, String token) throws ToDoExceptions;

	/**
	 * @param token
	 * @param noteId
	 * <p><b>To pin note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void pinNote(String token, String noteId) throws ToDoExceptions;

	/**
	 * @param token
	 * @param noteId
	 * <p><b>To archive note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void archiveNote(String token, String noteId) throws ToDoExceptions;

	/**
	 * @param token
	 * @param noteId
	 * <p><b>To added reminder on note in todo application</b></p>
	 * @throws ToDoExceptions 
	 * @throws ParseException 
	 */
	void reminderNote(String token, String noteId, String reminderDate) throws ToDoExceptions, ParseException;

	/**
	 * @param header
	 * @param noteId 
	 * <p><b>To added labels on note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void addlabels(String header, String noteId, List<String> labels) throws ToDoExceptions;
	/**
	 * @param header
	 * @param noteId
	 * <p><b>To remove reminder from particular note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void removeReminder(String token, String noteId) throws ToDoExceptions;
	/**
	 * @param header
	 * @param notedto
	 *  <p><b>To add color for particular note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void addColor(String header, NoteDTO notedto) throws ToDoExceptions;

	/**
	 * @param header
	 * @param labeldto
	 * <p><b>To create label in todo application</b></p>
	 * @return 
	 * @throws ToDoExceptions 
	 */
	void createLabels(String token, String lableName) throws ToDoExceptions;

	/**
	 * @param header
	 * @param noteId
	 *<p><b>To unpin note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void unpinNote(String header, String noteId) throws ToDoExceptions;

	
	

}
