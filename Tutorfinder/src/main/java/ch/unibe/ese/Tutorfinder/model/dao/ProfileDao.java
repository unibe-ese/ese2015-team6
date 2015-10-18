package ch.unibe.ese.Tutorfinder.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Profile;

public interface ProfileDao extends CrudRepository<Profile, Long>{

}
