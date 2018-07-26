package com.bridgelabz.todoapplication.noteservice.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todoapplication.noteservice.model.Note;
import com.bridgelabz.todoapplication.noteservice.model.NoteDTO;
import com.bridgelabz.todoapplication.noteservice.service.INoteService;
import com.bridgelabz.todoapplication.securityservice.JwtTokenProvider;
import com.bridgelabz.todoapplication.utilservice.dto.ResponseDTO;
import com.bridgelabz.todoapplication.utilservice.exceptions.RestPreconditions;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;
import com.bridgelabz.todoapplication.utilservice.redisservice.IRedisRepository;

/**
 * @author yuga
 * @since 17/07/2018
 *        <p>
 *        <b>To interact with the view and services.controller is the media
 *        between view and model.</b>
 *        </p>
 *
 */
@RestController
public class NoteController {
	final static String REQUEST_ID = "IN_NOTE";
	final static String RESPONSE_ID = "OUT_NOTE";
	private Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Autowired
	INoteService noteService;
	
	@Autowired
	IRedisRepository iRedisRepository;
	@Autowired
	JwtTokenProvider tokenProvider;

	/**
	 * @param token
	 * @param notedto
	 * <p><b>To take create note url from view and perform operations </b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/createnote")
	public ResponseEntity<ResponseDTO> createNote(HttpServletRequest request ,@RequestBody NoteDTO notedto) throws ToDoExceptions, ParseException {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.createNote(notedto, tokenFromHeader);
		logger.info(RESPONSE_ID+request.getRequestURI());
		return new ResponseEntity("Note successfully created.", HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take readNote url from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/readnote")
	public ResponseEntity<ResponseDTO> readNote(HttpServletRequest request,@RequestParam String noteId) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());
			
			String tokenFromHeader = request.getHeader("token");
			String userId= tokenProvider.parseJWT(tokenFromHeader);
			logger.info(tokenFromHeader);
			String tokenFromRedis = iRedisRepository.getToken(userId);
			logger.info(tokenFromRedis);
			if(!tokenFromHeader.equals(tokenFromRedis))
			{
				logger.info("Token not matched");
				
				throw new ToDoExceptions("Token not matched");
			}
			
			Note note = noteService.readNote(noteId, tokenFromHeader);
			logger.info(RESPONSE_ID+request.getRequestURI());
			return new ResponseEntity(note, HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param req
	 * <p><b>To take readAllNotes url from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/readallnotes")
	public ResponseEntity<ResponseDTO> readAllNotes(HttpServletRequest request) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());	
			
			String tokenFromHeader = request.getHeader("token");
			String userId= tokenProvider.parseJWT(tokenFromHeader);
			logger.info(tokenFromHeader);
			String tokenFromRedis = iRedisRepository.getToken(userId);
			logger.info(tokenFromRedis);
			if(!tokenFromHeader.equals(tokenFromRedis))
			{
				logger.info("Token not matched");
				
				throw new ToDoExceptions("Token not matched");
			}
			
			List<Note> note = noteService.readAllNotes(tokenFromHeader);
			logger.info(RESPONSE_ID+request.getRequestURI());
			return new ResponseEntity(note, HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param updatedto
	 * @param req
	 * <p><b>To take updateNote url from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/updatenote")
	public ResponseEntity<ResponseDTO> updateNote(HttpServletRequest request,@RequestBody NoteDTO notedto) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.updateNote(notedto, tokenFromHeader);
		logger.info(RESPONSE_ID+request.getRequestURI());
		return new ResponseEntity("Note has been updated successfully", HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take deleteNote url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/deletenote/{noteId}")
	public ResponseEntity<ResponseDTO> deleteNote(HttpServletRequest request,@PathVariable(value="noteId") String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
	
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		RestPreconditions.checkNotNull(noteService.deleteNote(noteId, tokenFromHeader),"Exception in delete ");
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity("Note has been deleted successfully.", HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take deleteNoteFromTrash url and request body or request param and also take token  from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("/permanentdelete")
	public ResponseEntity<ResponseDTO> deleteNoteFromTrash(HttpServletRequest request,@RequestParam String noteId,
			HttpServletRequest req) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());
			
			String tokenFromHeader = request.getHeader("token");
			String userId= tokenProvider.parseJWT(tokenFromHeader);
			logger.info(tokenFromHeader);
			String tokenFromRedis = iRedisRepository.getToken(userId);
			logger.info(tokenFromRedis);
			if(!tokenFromHeader.equals(tokenFromRedis))
			{
				logger.info("Token not matched");
				
				throw new ToDoExceptions("Token not matched");
			}
			
			noteService.deleteNoteFromTrash(noteId, tokenFromHeader);
			logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity("Note has been permanently deleted.", HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take restoreNoteFromTrash url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/restorefromtrash")
	public ResponseEntity<ResponseDTO> restoreNoteFromTrash(HttpServletRequest request,@RequestParam String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.restoreNoteFromTrash(noteId, tokenFromHeader);
			logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity("Note has been restore.", HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take pinnote url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/pinnote")
	public ResponseEntity<ResponseDTO> pinNote(HttpServletRequest request,@RequestParam String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.pinNote(tokenFromHeader,noteId);
		logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity("Note has been pined ", HttpStatus.OK);
	}
	
	
	
	/**
	 * @param request
	 * @param noteId
	 * <p><b>To take unpinnote url and request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/unpinnote")
	public ResponseEntity<ResponseDTO> unpinNote(HttpServletRequest request,@RequestParam String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	

		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.unpinNote(tokenFromHeader,noteId);
		logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity("Note has been pined ", HttpStatus.OK);
	}
	
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take archiveNote url and request body or request param and also take token  from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/archivenote")
	public ResponseEntity<ResponseDTO> archiveNote(HttpServletRequest request,@RequestParam String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.archiveNote(request.getHeader("token"),noteId);
		logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity("Note in archive", HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param request
	 * @param noteId
	 * <p><b>To take remindme url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/remindme")
	public ResponseEntity<ResponseDTO> remindMe(HttpServletRequest request,@RequestParam String noteId , @RequestBody String reminderDate) throws ToDoExceptions, ParseException {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.reminderNote(tokenFromHeader,noteId, reminderDate);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity("Reminder added on your note successfully", HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param request
	 * @param noteId
	 * <p><b>To take remove reminder url and request param  from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/removereminder")
	public ResponseEntity<ResponseDTO> removeReminder(HttpServletRequest request,@RequestParam String noteId) throws ToDoExceptions, ParseException {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		
		noteService.removeReminder(tokenFromHeader,noteId);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity("Reminder added on your note successfully", HttpStatus.OK);
	}
	/******************************************************************************************************/	
	
	/**
	 * @param request
	 * @param notedto
	 * @return response
	 * <p><b></b></p>
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/addcolor")
	public ResponseEntity<ResponseDTO> addColor(HttpServletRequest request,@RequestBody NoteDTO notedto) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.addColor(tokenFromHeader, notedto);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity("color added successfully", HttpStatus.OK);
	}
	
/******************************************************************************************************/
	
	/**
	 * @param request
	 * @param labelName
	 * @return
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/createlabel")
	public ResponseEntity<ResponseDTO> createLabels(HttpServletRequest request,@RequestParam(value="Label")String labelName) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.createLabels(tokenFromHeader, labelName);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity("Label created successfully", HttpStatus.OK);
	}
/********************************************************************************************************/
	/**
	 * @param request
	 * @param noteId
	 *<p><b>To take addlabel url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/addlabel/{noteId}")
	public ResponseEntity<ResponseDTO> addlabels(HttpServletRequest request,@PathVariable(value="noteId")String noteId ,@RequestBody List<String>labels) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String tokenFromHeader = request.getHeader("token");
		String userId= tokenProvider.parseJWT(tokenFromHeader);
		logger.info(tokenFromHeader);
		String tokenFromRedis = iRedisRepository.getToken(userId);
		logger.info(tokenFromRedis);
		if(!tokenFromHeader.equals(tokenFromRedis))
		{
			logger.info("Token not matched");
			
			throw new ToDoExceptions("Token not matched");
		}
		
		noteService.addlabels(tokenFromHeader, noteId,labels);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity("Label added successfully", HttpStatus.OK);
	}
	
	/******************************************************************************************************/
	
}
