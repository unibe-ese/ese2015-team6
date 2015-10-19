package ch.unibe.ese.Tutorfinder.model.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Profile;

public interface ProfileDao extends CrudRepository<Profile, Long>{

	@Modifying
	@Query("update Profile p set p.biography = ?1 where p.id = ?2")
	int updateBiography(String biography, long id);
	
	@Modifying
	@Query("update Profile p set p.region = ?1 where p.id = ?2")
	int updateRegion(String region, long id);
	
	@Modifying
	@Query("update Profile p set p.imgPath = ?1 where p.id = ?2")
	int updateImgPath(String imgPath, long id);
	
	@Modifying
	@Query("update Profile p set p.wage = ?1 where p.id = ?2")
	int updateWage(double wage, long id);
	
}
