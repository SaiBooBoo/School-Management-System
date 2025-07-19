package org.example.athenabackend.service;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.SubjectDao;
import org.example.athenabackend.entity.Subject;
import org.example.athenabackend.exception.SubjectAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectService {
    private final SubjectDao subjectDao;

    public Subject createSubject(String subjectName){
        if(subjectDao.findBySubjectName(subjectName).isPresent()){
            throw new SubjectAlreadyExistsException(subjectName);
    }
        Subject s = new Subject();
        s.setSubjectName(subjectName);
        return subjectDao.save(s);
    }

    public void deleteSubject(Integer id){
        subjectDao.deleteById(id);
    }

    public List<Subject> getAllSubjects(){
        return subjectDao.findAll();
    }
}
