package ir.shelmos.drive.repositories;

import org.springframework.data.repository.CrudRepository;

import ir.shelmos.drive.models.Photo;

public interface PhotosRepository extends CrudRepository<Photo, Long> {

}
