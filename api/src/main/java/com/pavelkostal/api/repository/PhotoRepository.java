package com.pavelkostal.api.repository;

import com.pavelkostal.api.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
	
	List<Photo> findPhotosByUniqueUserId(String jwtId);
//	List<Photo> findPhotoByPositionCity(String city);

	@Query("SELECT photo FROM Photo photo WHERE photo.position.city = ?1 ")
	List<Photo> findByCity(String City);

}
