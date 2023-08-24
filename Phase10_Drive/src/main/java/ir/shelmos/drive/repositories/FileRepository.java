package ir.shelmos.drive.repositories;

import ir.shelmos.drive.models.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Integer> {

}
