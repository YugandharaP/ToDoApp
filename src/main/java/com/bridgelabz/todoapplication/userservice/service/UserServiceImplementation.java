package com.bridgelabz.todoapplication.userservice.service;

import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.securityservice.JwtTokenProvider;
import com.bridgelabz.todoapplication.userservice.model.Email;
import com.bridgelabz.todoapplication.userservice.model.User;
import com.bridgelabz.todoapplication.userservice.model.UserDTO;
import com.bridgelabz.todoapplication.userservice.repository.IUserRepository;
import com.bridgelabz.todoapplication.utilservice.exceptions.RestPreconditions;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;
import com.bridgelabz.todoapplication.utilservice.mailservice.IEmailService;
import com.bridgelabz.todoapplication.utilservice.mailservice.IMailProducer;
import com.bridgelabz.todoapplication.utilservice.mapperservice.ModelMapperService;
import com.bridgelabz.todoapplication.utilservice.redisservice.IRedisRepository;

/**
 * @author yuga
 * @since 09/07/2018
 *        <p>
 * 		<b>To connect controller and MongoRepository and provides
 *        implementation of the service methods </b>
 *        </p>
 */
@Service
public class UserServiceImplementation implements IUserService {
	@Autowired
	IUserRepository userRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplementation.class);

	@Autowired
	IEmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IMailProducer mailProducer;
	
	@Autowired
	ModelMapperService modelMapper;
	
	@Autowired
	IRedisRepository redisRepository;

	/**
	 * @param logindto
	 * <p><b>To check conditon(check email id and password) when user want's to login</b>  </p>
	 * @throws ToDoExceptions
	 * @exception throws ToDoException exception
	 */
	@Override
	public String login(UserDTO userdto) throws ToDoExceptions, Exception {
		RestPreconditions.checkNotNull(userdto.getEmail(), "NoValuePresent : Email should not be null");
		RestPreconditions.checkNotNull(userdto.getPassword() , "NoValuePresent : Password should not be null");
		
		Optional<User> optionalUser = userRepository.findByEmail(userdto.getEmail());
		RestPreconditions.checkArgument(!optionalUser.isPresent(), "NullPointerException: Email id not present in Database");
		RestPreconditions.checkArgument(!optionalUser.get().isStatus(), "AuthenticationException: Not authorised user !");
		if (!passwordEncoder.matches(userdto.getPassword(), optionalUser.get().getPassword())) {
			LOGGER.error("Wrong Password given");
			throw new ToDoExceptions("Wrong Password given");
		}
			User user = optionalUser.get();
			JwtTokenProvider token = new JwtTokenProvider();
			String tokenGenerated = token.generator(user);
			LOGGER.info("token : " + tokenGenerated);
			String userId= user.getId();
			String tokenFromRedis = redisRepository.getToken(userId);
			RestPreconditions.checkNotNull(tokenFromRedis, "AuthenticationException : UnAuthorised user ! ");
			LOGGER.info("token from Redis"+tokenFromRedis);
			String userIdfromRedis = token.parseJWT(tokenFromRedis);
			if(userId.equals(userIdfromRedis)) {
				return tokenGenerated;
			}
			throw new ToDoExceptions("UnAuthorised user !");
	}

	/**
	 * @param registerdto
	 * <p><b>To check whether user is already present in database or not</b></p>
	 * @throws RegistrationExceptions
	 * @throws MessagingException
	 * @throws ToDOException
	 */
	@Override
	public void register(UserDTO userdto) throws ToDoExceptions, MessagingException {
		RestPreconditions.checkNotNull(userdto.getEmail(), "NoValuePresent : Email should not be null");
		RestPreconditions.checkNotNull(userdto.getPassword(), "NoValuePresent : Password should not be null");
		RestPreconditions.checkNotNull(userdto.getFirstName(), "NoValuePresent : FirstName should not be null");
		RestPreconditions.checkNotNull(userdto.getLastName(), "NoValuePresent : LastNBame should not be null");
		RestPreconditions.checkNotNull(userdto.getMobile(), "NoValuePresent : Mobile Number should not be null");

		Optional<User> optionalUser = userRepository.findByEmail(userdto.getEmail());
		RestPreconditions.checkArgument(optionalUser.isPresent(), "DatabaseException: Email id allready present in Database");

		User user = modelMapper.map(userdto, User.class);
		user.setPassword(passwordEncoder.encode(userdto.getPassword()));
		userRepository.insert(user);
		sendAuthenticationMail(user);
	}

	/**
	 * @param registerdto
	 * <p><b>To send mail with activation link</b></p>
	 * @return boolean value
	 * @throws MessagingException
	 */
	public void sendAuthenticationMail(User user) throws ToDoExceptions, MessagingException {
		JwtTokenProvider token = new JwtTokenProvider();
		String validToken = token.generator(user);
		RestPreconditions.checkNotNull(validToken, "Token not generated");
		LOGGER.info("Your token is : " + validToken);
		
		redisRepository.setToken(validToken);
		LOGGER.info("Token set into redis repository");
		
		Email email = new Email();
		email.setTo(user.getEmail());
		email.setSubject("Bellow activation Link");
		email.setBody("http://localhost:8080/useractivation/?token=" + validToken);
		mailProducer.produceMessage(email);
	}

	/**
	 * @param registerdto
	 * <p><b>To activate user by setting status true into the database</b></p>
	 * @throws ToDoExceptions 
	 * @throws RegistrationExceptions
	 */
	@Override
	public void activateUser(String token) throws ToDoExceptions {
		JwtTokenProvider claimToken = new JwtTokenProvider();
		String userId = claimToken.parseJWT(token);
		Optional<User> optionalUser = userRepository.findById(userId);
		RestPreconditions.checkArgument(!optionalUser.isPresent(), "user id is null");
		User user = optionalUser.get();
		user.setStatus(true);
		userRepository.save(user);
	}

	/**
	 * @param String emailId
	 * <p> <b>Forgot password operations</b> </p>
	 * @throws ToDoExceptionsng
	 * @throws MessagingException
	 */
	@Override
	public void forgotPassword(String emailId) throws ToDoExceptions, MessagingException {
		RestPreconditions.checkNotNull(emailId, "Email id not entered");
		Optional<User> user = userRepository.findByEmail(emailId);
		RestPreconditions.checkNotNull(user, "Email id not present in our database");
		
		JwtTokenProvider token = new JwtTokenProvider();
		String validToken = token.generator(user.get());
		RestPreconditions.checkNotNull(validToken, "Token not generated");
		
		Email email = new Email();
		email.setTo(user.get().getEmail());
		email.setSubject("Bellow activation Link");
		email.setBody("http://localhost:8080/resetpassword/?" + validToken);
		mailProducer.produceMessage(email);
	}

	/**@param token
	 * @param passworddto
	 * <p><b>To reset password of the intended user</b></p>
	 **/
	@Override
	public void resetPassword(String token, UserDTO userdto) throws ToDoExceptions {
		if (!userdto.getPassword().equals(userdto.getConfirmPassword())) {
			LOGGER.error("Password not matched");
			throw new ToDoExceptions("Password not mathched");
		}
		JwtTokenProvider claimToken = new JwtTokenProvider();
		String userId = claimToken.parseJWT(token);
		Optional<User> optionalUser = userRepository.findById(userId);
		User user = optionalUser.get();
		user.setPassword(passwordEncoder.encode(userdto.getPassword()));
		userRepository.save(user);

	}
}
