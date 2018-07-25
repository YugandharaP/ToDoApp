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
	public void createNote(NoteDTO notedto, String token) throws ToDoExceptions, ParseException {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(notedto.getNoteId(), "NULLException: noteId must not be null");
		RestPreconditions.checkNotNull(notedto.getTitle(), "Note title is null");
		RestPreconditions.checkNotNull(notedto.getDiscription(), "note discription is null");

		String userId = tokenProvider.parseJWT(token);

		/*
		 * LOGGER.info("header token : " + token); String userToken =
		 * redisService.getToken("userId");
		 * 
		 * note.setUserId(tokenProvider.parseJWT(userToken));
		 * LOGGER.info("header token : " + userToken);
		 */
		RestPreconditions.checkNotNull(userId, "NULLPointerException : User id not present in database");
		RestPreconditions.checkNotNull(noteRepository.existsById(notedto.getNoteId()),
				"ConflictException : Note Id Conflict.Note id allready present");
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(notedto.getRemainder());
		RestPreconditions.checkArgument(date.before(new Date()), "DateFormatException: Not valid Date");
		Note note = modelMapper.map(notedto, Note.class);
		note.setReminder(date);
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
	public String deleteNote(String noteId, String token) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	public Note readNote(String noteId, String token) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	public List<Note> readAllNotes(String token) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void updateNote(NoteDTO notedto, String token) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(notedto, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void deleteNoteFromTrash(String noteId, String token) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException : token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException : noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void restoreNoteFromTrash(String noteId, String token) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void pinNote(String token, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	 *            <p>
	 *            <b>To unpin the particular note</b>
	 *            </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public void unpinNote(String token, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void archiveNote(String token, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	 * @param token
	 * @param noteId
	 * @param reminderDate
	 *            <p>
	 *            To set reminder date for particular note
	 *            </p>
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */

	@Override
	public void reminderNote(String token, String noteId, String reminderDate) throws ToDoExceptions, ParseException {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		RestPreconditions.checkNotNull(reminderDate, "NULLPointerException :date is null");

		String userId = tokenProvider.parseJWT(token);
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
	public void removeReminder(String token, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void createLabels(String token, String lableName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(lableName, "NULLPointerException :labelName is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void addColor(String token, NoteDTO notedto) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		String userId = tokenProvider.parseJWT(token);
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
	public void addlabels(String token, String noteId, List<String> labelNames) throws ToDoExceptions {
		RestPreconditions.checkNotNull(token, "NULLPointerException :token is null");
		RestPreconditions.checkNotNull(noteId, "NULLPointerException :noteId is null");
		String userId = tokenProvider.parseJWT(token);
		RestPreconditions.checkNotNull(userId, "NULLPointerException :User id not present in database");
		Optional<Note> optionalNote = noteRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				"NULLPointerException: Note id not present in database");
		for (int i = 0; i < labelNames.size(); i++) {
			Label labelOfUser = labelRepository.findByUserIdAndLabelName(userId, labelNames.get(i));
			if (labelOfUser == null) {
				Label label = new Label();
				label.setUserId(userId);
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
}
