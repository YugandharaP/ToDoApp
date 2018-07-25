package com.bridgelabz.todoapplication.noteservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todoapplication.noteservice.model.Label;

/**@since 23/07/2018
 * <p><b></b></p>
 * @author yuga
 * 
 *
 */
@Repository
public interface ILabelRepository extends MongoRepository<Label, String> {

	/**
	 * @param labelName
	 * <p><b>To find by label name which is already present in database or not</b></p>
	 * @return 
	 */
	Optional<Label> findByLabelName(String labelName);


	/**
	 * @param noteList
	 * <p><b>To find all notes from not list</b></p>
	 * @return
	 */
	List<String> findAllByNoteList(List<String> noteList);

}
