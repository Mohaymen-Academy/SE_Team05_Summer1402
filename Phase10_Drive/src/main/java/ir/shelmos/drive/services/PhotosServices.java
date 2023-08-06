package ir.shelmos.drive.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ir.shelmos.drive.models.Photo;
import ir.shelmos.drive.repositories.PhotosRepository;

@Service
public class PhotosServices {
    @Autowired
    private PhotosRepository photosRepository;

    public Photo get(long id) {
        return photosRepository.findById(id).orElse(null);
    }

    public Iterable<Photo> getAll() {
        return photosRepository.findAll();
    }

    public Photo create(String fileName, String fileContentType, byte[] data) {
        Photo photo = new Photo() {
            {
                setFileName(fileName);
                setContentType(fileContentType);
                setData(data);
            }
        };
        return photosRepository.save(photo);
    }

    public void delete(long id) {
        photosRepository.deleteById(id);
    }

}
