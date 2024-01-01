package com.pavelkostal.api.repository;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findPhotosByUniqueUserId(String userUniqueId);

	@Query("SELECT photo FROM User WHERE uniqueUserId = ?1 ")
	List<Photo> findAllPhotosForSelectedUser(String user);

}
