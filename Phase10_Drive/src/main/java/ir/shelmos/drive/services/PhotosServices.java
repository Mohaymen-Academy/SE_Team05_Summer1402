package ir.shelmos.drive.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ir.shelmos.drive.models.Photo;
import ir.shelmos.drive.repositories.PhotosRepository;

@Service
public class PhotosServices {
    @Autowired
    private PhotosRepository photosRepository;

    public Photo get(int id) {
        return photosRepository.findById(id).orElse(null);
    }

    public Iterable<Photo> getAll() {
        return photosRepository.findAll();
    }

    public Photo create(String fileName, String fileContentType, byte[] data) {
        var photo = new Photo();
        photo.setData(data);
        photo.setContentType(fileContentType);
        photo.setFileName(fileName);
        photosRepository.save(photo);
        return photo;
    }

    public void delete(int id) {
        photosRepository.deleteById(id);
    }

}
