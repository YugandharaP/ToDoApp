package com.bridgelabz.todoapplication.userservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todoapplication.userservice.model.User;

/**
 * @author yuga
 *@since 09/07/2018
 *<p><b>To deal with MongoDB database</b></p>
 */
@Repository
public interface IUserRepository extends MongoRepository<User, String> {
	/**
	 * @param email
	 * @return User
	 * <p><b>used to find user in database is allready present or not through email id</b></p>
	 */
	public Optional<User> findByEmail(String email);

}