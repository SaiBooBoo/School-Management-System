package org.example.athenabackend.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.StudentDao;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Data
public class FileStorageService {

    private final Path fileStorageLocation;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("Upload dir: " + uploadDir);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory!", ex);
        }
    }

    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(UUID.randomUUID() + "_" + file.getOriginalFilename());
        try {
            if (fileName.contains("..")){
                throw new RuntimeException("Invalid file path: " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Saved to: " + targetLocation.toAbsolutePath());
            return fileName;
        } catch (IOException ex){
            throw new RuntimeException("Could not store file:" + fileName, ex);
        }
    }

    public Resource loadFile(String fileName){
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            } else {
                throw new RuntimeException("File not found: " +fileName);
            }

        } catch (MalformedURLException e){
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }

    public Path getFileStorageLocation(){
        return fileStorageLocation;
    }

    public void deleteFile(String fileName){
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch(IOException ex){
            System.err.println("Could not delete file: " + fileName + " error: " + ex.getMessage());
        }
    }
}
