package com.bridgelabz.todoapplication.noteservice.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.noteservice.model.Label;
import com.bridgelabz.todoapplication.noteservice.model.Note;
import com.bridgelabz.todoapplication.noteservice.model.NoteDTO;
import com.bridgelabz.todoapplication.noteservice.repository.ILabelRepository;
import com.bridgelabz.todoapplication.noteservice.repository.INoteRepository;
import com.bridgelabz.todoapplication.securityservice.JwtTokenProvider;
import com.bridgelabz.todoapplication.utilservice.exceptions.RestPreconditions;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;
import com.bridgelabz.todoapplication.utilservice.mapperservice.ModelMapperService;

/**
 * @author yuga
 * @since 17/07/2018
 *        <p>
 *        <b>To connect controller and MongoRepository and provides
 *        implementation of the service methods </b>
 *        </p>
 */
@Service
public class NoteServiceImplementation implements INoteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImplementation.class);
	@Autowired
	INoteRepository noteRepository;
	/*
	 * @Autowired IUserRepository userRepository;
	 */
	@Autowired
	JwtTokenProvider tokenProvider;
	/*
	 * @Autowired RedisService redisService;
	 */
	@Autowired
	ModelMapperService modelMapper;

	@Autowired
	ILabelRepository labelRepository;

	/**
	 * @param note
	 * @param token
	 *            <p>
	 *            <b>To create note for particular user id in todo application </b>
	 *            </p>
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */
	@Override
	public void createNote(NoteDTO notedto, String userId) throws ToDoExceptions, ParseException {
		RestPreconditions.checkNotNull(notedto.getTitle(), "Note title is null");
		RestPreconditions.checkNotNull(notedto.getDiscription(), "note discription is null");

		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		Note note = modelMapper.map(notedto, Note.class);
		if (notedto.getRemainder() != "" && notedto.getRemainder() != null) {
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(notedto.getRemainder());
			RestPreconditions.checkArgument(date.before(new Date()), "DateFormatException: Not valid Date");
			note.setReminder(date);
		}
		note.setUserId(userId);
		note.setCreatedDate(new Date());
		note.setLastModifiedDate(new Date());
		noteRepository.insert(note);
	}

	/**
	 * @param noteId
	 * @param token
	 *            <p>
	 *            <b>To delete note from todo application based on note Id which
	 *            isgiven by the user </b>
	 *            </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public String deleteNote(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NullPointerException: Note id not present in database");

		Note note = optionalNote.get();
		note.setTrashStatus(true);
		noteRepository.save(optionalNote.get());
		return optionalNote.get().getId();
	}

	/**
	 * @param note
	 * @param token
	 *            <p>
	 *            <b>To read note from todo application based on note id which is
	 *            enterd by user </b>
	 *            </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public Note readNote(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NullPointerException: Note id not present in database");

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), "BooleanException : Note in Trash");

		Note note = optionalNote.get();
		return note;
	}

	/**
	 * @param token
	 *            <p>
	 *            <b>To read all notes from todo application based on user id </b>
	 *            </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public List<Note> readAllNotes(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");

		List<Note> noteList = noteRepository.findAllByUserId(userId);
		List<Note> finalNoteList = new ArrayList<Note>();
		for (Note note : noteList) {
			if (!note.isTrashStatus())
				finalNoteList.add(note);
		}
		RestPreconditions.checkNotNull(finalNoteList, "NULLPointerException : No note present in database !");
		return finalNoteList;
	}

	/**
	 * @param updatedto
	 * @param token
	 *            <p>
	 *            <b>To update note from todo application based on note id which is
	 *            enterd by user </b>
	 *            </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public void updateNote(NoteDTO notedto, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(notedto, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(notedto.getNoteId());
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NullPointerException: Note id not present in database");

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), "DatabaseException : Note in Trash");

		Note note = optionalNote.get();
		if (!notedto.getTitle().equals("")) {
			note.setTitle(notedto.getTitle());
		}
		if (!notedto.getDiscription().equals("")) {
			note.setDiscription(notedto.getDiscription());
		}
		note.setLastModifiedDate(new Date());
		noteRepository.save(note);
	}

	/**
	 * @param noteId
	 * @param token
	 *            <p>
	 *            <b>To delete note from trash</b>
	 *            </p>
	 * @throws ToDoExceptions
	 **/

	@Override
	public void deleteNoteFromTrash(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException : noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NullPointerException: Note id not present in Trash");

		RestPreconditions.checkArgument(!optionalNote.get().isTrashStatus(),
				"DatabaseException : Note id not present in trash");

		Note note = optionalNote.get();
		noteRepository.delete(note);
	}

	/**
	 * @param noteId
	 * @param token
	 *            <p>
	 *            <b>To restore the note from trash</b>
	 *            </p>
	 * @throws ToDoExceptions
	 **/

	@Override
	public void restoreNoteFromTrash(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NullPointerException: Note id not present in Trash");

		RestPreconditions.checkArgument(!optionalNote.get().isTrashStatus(), "DatabaseException : Note not in trash");

		Note note = optionalNote.get();
		note.setTrashStatus(false);
		note.setLastModifiedDate(new Date());
		noteRepository.save(note);
	}

	/**
	 * @param token
	 * @param noteId
	 *            <p>
	 *            <b>To pin the particular note</b>
	 *            </p>
	 * @throws ToDoExceptions
	 **/
	@Override
	public void pinNote(String userId, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NullPointerException: Note id not present in database");

		Note note = optionalNote.get();
		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), "DatabaseException : Note is in trash");
		RestPreconditions.checkArgument(note.isPinNote(), "DatabaseException: Note is allready in pinned");
		if (note.isArchiveNOte()) {
			note.setArchiveNOte(false);
		}
		note.setPinNote(true);
		noteRepository.save(note);
	}

	/**
	 * @param token
	 * @param noteId
	 * <p> <b>To unpin the particular note</b> </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public void unpinNote(String userId, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NullPointerException: Note id not present in database");

		Note note = optionalNote.get();
		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), "DatabaseException : Note is in trash");
		RestPreconditions.checkArgument(!note.isPinNote(), "DatabaseException: Note is not pinned");
		RestPreconditions.checkArgument(note.isArchiveNOte(), "DatabaseException: Note is in archieve");
		if (note.isPinNote()) {
			note.setPinNote(false);
		}
		noteRepository.save(note);
	}

	/**
	 * @param token
	 * @param noteId
	 *            <p>
	 *            <b>To archive particular note</b>
	 *            </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public void archieveNote(String userId, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NULLPointerException: Note id not present in database");

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), "DatabaseException : Note in trash");

		Note note = optionalNote.get();
		RestPreconditions.checkArgument(note.isArchiveNOte(), "DatabaseException: Note is allready in archive.");

		note.setArchiveNOte(true);
		noteRepository.save(note);
	}

	/**
	 * @throws ToDoExceptions
	 * 
	 */
	@Override
	public void removeNoteFromArcheive(String userId, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NULLPointerException: Note id not present in database");

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), "DatabaseException : Note in trash");

		Note note = optionalNote.get();
		RestPreconditions.checkArgument(!note.isArchiveNOte(), "DatabaseException: Note not present in archive.");

		note.setArchiveNOte(false);
		noteRepository.save(note);
	}

	/**
	 **/
	@Override
	public List<Note> getAllNotesFromArchive(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		List<Note> noteList = noteRepository.findAllByUserId(userId);
		List<Note> finalNoteList = new ArrayList<Note>();
		for (Note note : noteList) {
			if (note.isArchiveNOte())
				finalNoteList.add(note);
		}
		RestPreconditions.checkNotNull(finalNoteList, "NULLPointerException : No note present in database !");
		return finalNoteList;
	}
	/**
	 * @param token
	 * @param noteId
	 * @param reminderDate
	 * <p>To set reminder date for particular note</p>
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */

	@Override
	public void reminderNote(String userId, String noteId, String reminderDate) throws ToDoExceptions, ParseException {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(reminderDate, "NULLPointerException :date is null");

		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NULLPointerException: Note id not present in database");
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(reminderDate);
		RestPreconditions.checkArgument(date.before(new Date()), "DateFormatException: Not valid Date");
		optionalNote.get().setReminder(date);
		noteRepository.save(optionalNote.get());
	}

	/**
	 * @throws ToDoExceptions
	 * 
	 */
	@Override
	public void removeReminder(String userId, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");
		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NULLPointerException: Note id not present in database");
		optionalNote.get().setReminder(null);
		noteRepository.save(optionalNote.get());
	}

	/**
	 * @throws ToDoExceptions
	 * 
	 */
	@Override
	public void createLabels(String userId, String lableName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(lableName, "NULLPointerException :labelName is null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");
		Optional<Label> optionalLabel = labelRepository.findByLabelName(lableName);

		if (!optionalLabel.isPresent()) {
			Label label = new Label();
			label.setLabelName(lableName);
			label.setUserId(userId);
			labelRepository.insert(label);// new label created
		} else {
			throw new ToDoExceptions("Label already created!");
		}
	}

	@Override
	public void addColor(String userId, NoteDTO notedto) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");
		RestPreconditions.checkNotNull(notedto.getNoteId(), "NULLPointerException :Note id not present in database");
		RestPreconditions.checkNotNull(notedto.getColor(), "NULLPointerException :colorCode field is null");

		Optional<Note> optionalNote = noteRepository.findById(notedto.getNoteId());
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NULLPointerException: Note id not present in database");
		optionalNote.get().setColorCode(notedto.getColor());
		noteRepository.save(optionalNote.get());
	}

	/**
	 * @throws ToDoExceptions
	 */
	@Override
	public void addlabels(String userId, String noteId, List<String> labelNames) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");
		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NULLPointerException: Note id not present in database");
		for (int i = 0; i < labelNames.size(); i++) {
			Label labelOfUser = labelRepository.findByUserIdAndLabelName(userId, labelNames.get(i));
			if (labelOfUser == null) {
				Label label = new Label();
				label.setUserId(userId);
				label.setLabelId(label.getLabelId());
				label.setLabelName(labelNames.get(i));
				labelRepository.insert(label);
				List<Label> labelListInNote = optionalNote.get().getListOfLabels();
				if (labelListInNote != null) {
					optionalNote.get().getListOfLabels().add(label);
				} else {
					List<Label> list = new ArrayList<Label>();
					list.add(label);
					optionalNote.get().setListOfLabels(list);
				}
				noteRepository.save(optionalNote.get());// confused out of loop or inside
			} else {
				List<Label> labelListInNote = optionalNote.get().getListOfLabels();

				List<String> labelStringNamesInNote;// contains only names of the labels

				if (labelListInNote != null) {
					labelStringNamesInNote = new ArrayList<String>();
					for (int k = 0; k < labelListInNote.size(); k++) {
						labelStringNamesInNote.add(labelListInNote.get(k).getLabelName());
					}
					if (!labelStringNamesInNote.contains(labelOfUser.getLabelName())) {
						optionalNote.get().getListOfLabels().add(labelOfUser);
					}
				} else {
					List<Label> list = new ArrayList<Label>();
					list.add(labelOfUser);
					optionalNote.get().setListOfLabels(list);
				}
				noteRepository.save(optionalNote.get());
			}
		}
	}

	/**
	 * @param userId-String
	 *            type of user id which extract from token
	 *            <p>
	 *            <b>To read all notes from trash</b>
	 *            </p>
	 * @throws ToDoException
	 **/
	@Override
	public List<Note> readAllFromTrash(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");

		List<Note> noteList = noteRepository.findAllByUserId(userId);
		RestPreconditions.checkNotNull(noteList, "NULLPointerException : No note present in database !");

		List<Note> finalNoteList = new ArrayList<Note>();
		for (Note note : noteList) {
			if (note.isTrashStatus())
				finalNoteList.add(note);
		}
		RestPreconditions.checkNotNull(finalNoteList, "NULLPointerException : No note present in Trash !");
		return finalNoteList;
	}
	
	/**
	 * @param userId-String type of user id which extract from token
	 * <p><b>To empty all notes from trash</b></p>
	 * @throws ToDoExceptions 
	 **/
	@Override
	public void emptyTrash(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");

		List<Note> noteList = noteRepository.findAllByUserId(userId);
		RestPreconditions.checkNotNull(noteList, "NULLPointerException : No note present in Trash !");
		for (int i=0;i<noteList.size();i++) {
			if (noteList.get(i).isTrashStatus())
			noteRepository.delete(noteList.get(i));	
		}
	}
	/**
	 * **/
	@Override
	public void deleteLabel(String userId, String labelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		RestPreconditions.checkNotNull(labelName, "NoValuePresent : Label name not entered ");
		Optional<Label> labelFromRepository = labelRepository.findByLabelName(labelName);
		RestPreconditions.checkArgument(!labelFromRepository.isPresent(),
				"NULLPointerException :label name not present in database");
		Label label = labelFromRepository.get();
		labelRepository.deleteByLabelName(label.getLabelName());
		LOGGER.info("label deleted from label repository");
		List<Note> listOfNotes = noteRepository.findAllByUserId(userId);
		if (listOfNotes.size() == 0) {
			throw new ToDoExceptions("No notes found");
		}
		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getListOfLabels() != null) {
				for (int j = 0; j < listOfNotes.get(i).getListOfLabels().size(); j++) {
					if (listOfNotes.get(i).getListOfLabels().get(j).getLabelName().equals(labelName)) {
						listOfNotes.get(i).getListOfLabels().remove(j);
						LOGGER.info("delete label from list");
						noteRepository.save(listOfNotes.get(i));
						LOGGER.info("save updated note");
					}
				}
			}
		}
	}

	/**
	 * @throws ToDoExceptions
	 **/
	@Override
	public void editLabel(String userId, String currentLabelName, String newLabelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		RestPreconditions.checkNotNull(currentLabelName, "NoValuePresent : Current Label name not entered ");
		RestPreconditions.checkNotNull(newLabelName, "NoValuePresent : New Label name not entered ");
		Optional<Label> labelFromRepository = labelRepository.findByLabelName(currentLabelName);
		RestPreconditions.checkArgument(!labelFromRepository.isPresent(),
				"NULLPointerException :label name not present in database");
		labelFromRepository.get().setLabelName(newLabelName);
		labelRepository.save(labelFromRepository.get());
		LOGGER.info("label edited from label repository");
		List<Note> listOfNotes = noteRepository.findAllByUserId(userId);
		if (listOfNotes.size() == 0) {
			throw new ToDoExceptions("No notes found");
		}
		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getListOfLabels() != null) {
				for (int j = 0; j < listOfNotes.get(i).getListOfLabels().size(); j++) {
					if (listOfNotes.get(i).getListOfLabels().get(j).getLabelName().equals(currentLabelName)) {
						listOfNotes.get(i).getListOfLabels().get(j).setLabelName(newLabelName);
						LOGGER.info("edit label from list");
						noteRepository.save(listOfNotes.get(i));
						LOGGER.info("save edited note");
					}
				}
			}
		}
	}

	/**
	 * @throws ToDoExceptions
	 **/
	@Override
	public List<String> getAllLabels(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		List<Note> listOfNotes = noteRepository.findAllByUserId(userId);
		if (listOfNotes.size() == 0) {
			throw new ToDoExceptions("No notes found");
		}
		List<String> finalLabelList = new ArrayList<String>();
		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getListOfLabels() != null) {
				for (int j = 0; j < listOfNotes.get(i).getListOfLabels().size(); j++) {
					finalLabelList.add(listOfNotes.get(i).getListOfLabels().get(j).getLabelName());
				}
			}
		}
		return finalLabelList;
	}

	/**
	 * @throws ToDoExceptions
	 **/
	@Override
	public List<Note> searchNotesByLabelName(String userId, String labelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		List<Note> listOfNotes = noteRepository.findAllByUserId(userId);
		if (listOfNotes.size() == 0) {
			throw new ToDoExceptions("No notes found");
		}
		List<Note> finalNotesList = new ArrayList<Note>();
		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getListOfLabels() != null) {
				for (int j = 0; j < listOfNotes.get(i).getListOfLabels().size(); j++) {
					if (listOfNotes.get(i).getListOfLabels().get(j).getLabelName().equals(labelName)) {
						finalNotesList.add(listOfNotes.get(i));
					}
				}
			}
		}
		return finalNotesList;
	}

	/**
	 * @throws ToDoExceptions 
	 * */
	@Override
	public void removelabelfromnote(String userId, String noteId, String labelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, "NoValuePresent : Note id should not be null");
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),"NULLPointerException: Note id not present in database");
		
		if(optionalNote.get().getListOfLabels().size() == 0)
		{
			throw new ToDoExceptions("Label not found!");
		}
		for(int i=0;i<optionalNote.get().getListOfLabels().size();i++)
		{
			if(optionalNote.get().getListOfLabels().get(i).getLabelName().equals(labelName))
			{
				optionalNote.get().getListOfLabels().remove(i);
			}
		}
	
		noteRepository.save(optionalNote.get());
	}

}
