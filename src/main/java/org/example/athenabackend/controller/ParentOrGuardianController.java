package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.ParentOrGuardianDao;
import org.example.athenabackend.dto.ParentOrGuardianDto;
import org.example.athenabackend.entity.ParentOrGuardian;
import org.example.athenabackend.exception.ParentOrGuardianNotFoundException;
import org.example.athenabackend.model.ParentType;
import org.example.athenabackend.service.FileStorageService;
import org.example.athenabackend.service.ParentOrGuardianService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/parents")
public class ParentOrGuardianController {
    private final ParentOrGuardianService parentOrGuardianService;
    private final FileStorageService fileStorageService;
    private final ParentOrGuardianDao parentOrGuardianDao;

    public ParentOrGuardianController(ParentOrGuardianDao parentDao, FileStorageService fileStorageService,
                                      ParentOrGuardianService parentOrGuardianService){
        this.parentOrGuardianDao = parentDao;
        this.fileStorageService = fileStorageService;
        this.parentOrGuardianService = parentOrGuardianService;
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getParentImage(@PathVariable Integer id){
        ParentOrGuardian p = parentOrGuardianDao.findById(id).orElseThrow(() -> new ParentOrGuardianNotFoundException(id));

        String fileName = p.getProfileImagePath();
        if(fileName == null){
            throw new ParentOrGuardianNotFoundException(id);
        }

        Resource resource = fileStorageService.loadFile(fileName);

        String contentType;
        try{
            Path path = fileStorageService.getFileStorageLocation().resolve(fileName);
            contentType = Files.probeContentType(path);
            if(contentType == null) contentType = "application/octet-stream";
        } catch (IOException ex){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PutMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadParentImage(@PathVariable Integer id, @RequestParam("file")MultipartFile file){

        ParentOrGuardian p = parentOrGuardianDao.findById(id).orElseThrow(() -> new ParentOrGuardianNotFoundException(id));
        String oldFile = p.getProfileImagePath();
        if(oldFile != null && !oldFile.isEmpty()){
            fileStorageService.deleteFile(oldFile);
        }

        String newFileName = fileStorageService.storeFile(file);
        p.setProfileImagePath(newFileName);
        parentOrGuardianDao.save(p);

        return ResponseEntity.ok("Parent image uploaded: " + newFileName);
    }

    @GetMapping("/byName/{name}")
    public ParentOrGuardianDto getByName(@PathVariable String name){
        return parentOrGuardianService.findParentWithStudents(name);
    }

    @GetMapping
    public List<ParentOrGuardianDto> getParentOrGuardianList(){
        return parentOrGuardianService.getAllParentOrGuardians();
    }

    @GetMapping("/search")
    public List<ParentOrGuardianDto> getParentOrGuardianByUsername(@RequestParam("username") String username){
        return parentOrGuardianService.searchByName(username);
    }

    @GetMapping("/search/type/{parentType}")
    public List<ParentOrGuardianDto> searchByParentType(@PathVariable("parentType") ParentType parentType){
        return parentOrGuardianService.searchByParentType(parentType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentOrGuardianDto> getParentOrGuardianById(@PathVariable("id") Integer id){
        parentOrGuardianService.getParentOrGuardianById(id);
        return ResponseEntity.ok(parentOrGuardianService.getParentOrGuardianById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentOrGuardianDto> updateParentOrGuardian(@PathVariable("id") Integer id,@RequestBody ParentOrGuardianDto parentOrGuardianDto) {
        return ResponseEntity.ok(parentOrGuardianService.updateParentOrGuardian(id, parentOrGuardianDto));
    }

    @PutMapping("/addStudents/{id}")
    public ParentOrGuardianDto addStudentToParent(@PathVariable("id") Integer id, @RequestBody ParentOrGuardianDto parentOrGuardianDto){
        return  parentOrGuardianService.addStudentToParent(id, parentOrGuardianDto);
    }

    @DeleteMapping("/{parentId}/students/{studentId}")
    public ResponseEntity<Void> removeStudent(
            @PathVariable Integer parentId,
            @PathVariable Integer studentId){
        parentOrGuardianService.removeStudentFromParentOrGuardian(parentId, studentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParentOrGuardianById(@PathVariable("id") Integer id){
        parentOrGuardianService.deleteParentOrGuardianById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
