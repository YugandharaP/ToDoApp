package com.bridgelabz.todoapplication.noteservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todoapplication.noteservice.model.Label;
import com.bridgelabz.todoapplication.noteservice.model.Note;

/**
 *@author yuga
 *@since 17/07/2018
 *<p><b>To deal with MongoDB database this note repository interface is required</b></p>
 *
 */
@Repository
public interface INoteRepository extends MongoRepository<Note,String>{
	/**
	 * @param userId
	 * <p><b>Finding the user id into the repository ,to get the list of notes</b></p>
	 *  @return list
	 */
	List<Note> findAllByUserId(String userId);

	List<Label> findAllByListOfLabels(List<Label> listOfLabels);

}
