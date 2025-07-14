package org.example.athenabackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FileUploadResponse {
    private String fileName;

    public FileUploadResponse(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }
}
