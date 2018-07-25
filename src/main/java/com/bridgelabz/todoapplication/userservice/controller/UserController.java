package com.bridgelabz.todoapplication.userservice.controller;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todoapplication.userservice.model.UserDTO;
import com.bridgelabz.todoapplication.userservice.service.IUserService;
import com.bridgelabz.todoapplication.utilservice.dto.ResponseDTO;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;

/**
 * @author yuga
 * @since 09/07/2018
 * <p><b>To interact with the view and services.controller is the media
 * between view and model.</b></p>
 */
@RestController
public class UserController {

	final static String REQUEST_ID ="IN_USER";
	final static String RESPONSE_ID="OUT_USER";
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	IUserService userService ;
	
	/**<p><b>To take register url from view and perform operations</b></p>
	 * @param registerdto
	 * @return response
	 * @throws ToDoExceptions 
	 * @throws RegistrationExceptions
	 * @throws MessagingException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO userdto ) throws ToDoExceptions, MessagingException {
		logger.info(REQUEST_ID);
		userService.register(userdto);
		logger.info(RESPONSE_ID);
		return new ResponseEntity("you are successfully registered with "+userdto.getEmail(),HttpStatus.OK);
	}
	/**<p><b>To take activation url from view and perform operations</b></p>
	 * @param token
	 * @return response
	 * @throws ToDoExceptions 
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/useractivation")
	public ResponseEntity<ResponseDTO> activateUser(@RequestParam(value="token") String token ) throws ToDoExceptions {
		logger.info(REQUEST_ID);
		userService.activateUser(token);
		logger.info(RESPONSE_ID);
		return new ResponseEntity(" successfully activated ",HttpStatus.OK);
	}

	/**
	 * @param logindto
	 * <p><b>To take login url from view and perform operations</b></p>
	 * @return response
	 * @throws Exception 
	 * @throws ToDoExceptions 
	 * @throws LoginException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/login")
	public ResponseEntity<ResponseDTO> loginUser(@RequestBody UserDTO userdto ,HttpServletResponse resp) throws ToDoExceptions, Exception  {
			logger.info(REQUEST_ID);
			String token=userService.login(userdto);
			resp.setHeader("token",token);
			logger.info("in user Login : "+token);
			logger.info(RESPONSE_ID);
			return new ResponseEntity(new ResponseDTO("you are successfully logged in",1),HttpStatus.OK);
		
	}
	/**
	 * @param String emailId
	 * <p><b>To take forgot password url from view and perform operations</b></p>
	 * @return response
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/forgetpassword")
	public ResponseEntity<ResponseDTO> forgotPassword(@RequestParam (value="email") String email ) throws Exception {
			logger.info(REQUEST_ID);
			userService.forgotPassword(email);
			logger.info(RESPONSE_ID);
			return new ResponseEntity(new ResponseDTO("Activation link has been successfully send.Please check ",1),HttpStatus.OK);
	}
	
	/**
	 * @param token
	 * @param userdto
	 * <p><b>To take reset password url from view and perform operations </b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/resetpassword")
	public ResponseEntity<ResponseDTO> resetpassword(@RequestParam(value="token") String token ,@RequestBody UserDTO userdto) throws ToDoExceptions 
	{
			logger.info(REQUEST_ID);
			userService.resetPassword(token,userdto);
			logger.info(RESPONSE_ID);
			return new ResponseEntity("Password reset successfully ",HttpStatus.OK);
	}
}
