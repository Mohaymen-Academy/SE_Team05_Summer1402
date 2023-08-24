package ir.shelmos.drive.services;

import ir.shelmos.drive.models.File;
import ir.shelmos.drive.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public File get(int id) {
        return fileRepository.findById(id).orElse(null);
    }

    public Iterable<File> getAll() {
        return fileRepository.findAll();
    }

    public File create(String fileName, String fileContentType, byte[] data) {
        File photo = File.builder()
                .data(data)
                .contentType(fileContentType)
                .fileName(fileName).build();

        fileRepository.save(photo);
        return photo;
    }

    public void delete(int id) {
        fileRepository.deleteById(id);
    }
}
