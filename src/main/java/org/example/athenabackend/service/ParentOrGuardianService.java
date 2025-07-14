package org.example.athenabackend.service;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.ParentOrGuardianDao;
import org.example.athenabackend.dao.StudentDao;
import org.example.athenabackend.dto.ParentOrGuardianDto;
import org.example.athenabackend.dtoSummaries.StudentSummaryDto;
import org.example.athenabackend.entity.ParentOrGuardian;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.entity.StudentParent;
import org.example.athenabackend.exception.ParentOrGuardianNotFoundException;
import org.example.athenabackend.exception.StudentDoesNotExistInParentException;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.model.ParentType;
import org.example.athenabackend.util.ParentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentOrGuardianService {
    private final ParentOrGuardianDao parentOrGuardianDao;
    private final StudentDao studentDao;

    public void removeStudentFromParentOrGuardian(Integer parentId, Integer studentId){
        ParentOrGuardian parent = parentOrGuardianDao.findById(parentId)
                .orElseThrow(()-> new ParentOrGuardianNotFoundException("Parent doesn't exist that you want to remove student from"));
        Student student = studentDao.findById(studentId)
                .orElseThrow(()-> new StudentNotFoundException("Student doesn't exit in the parent that you want to remove student from"));

        StudentParent toRemove = parent.getStudents().stream()
                        .filter(sp -> sp.getStudent().equals(student) && sp.getParentOrGuardian().equals(parent))
                        .findFirst().orElseThrow(() -> new StudentDoesNotExistInParentException(student.getDisplayName(), parent.getDisplayName()));

        parent.getStudents().remove(toRemove);
        student.getParents().remove(toRemove);
        parentOrGuardianDao.saveAndFlush(parent);
    }

    public List<ParentOrGuardianDto> getAllParentOrGuardians() {
        return parentOrGuardianDao.findAll().stream()
                .map(ParentUtil::toParentOrGuardianDto)
                .toList();
    }

    public ParentOrGuardianDto getParentOrGuardianById(Integer id) {
        return parentOrGuardianDao.findById(id)
                .map(ParentUtil::toParentOrGuardianDto)
                .orElseThrow(() -> new ParentOrGuardianNotFoundException(id));
    }

    public List<ParentOrGuardianDto> searchByName(String usernamePart) {
        return parentOrGuardianDao.findByUsernameContainingIgnoreCase(usernamePart).stream()
                .map(ParentUtil::toParentOrGuardianDto)
                .collect(Collectors.toList());
    }

    public ParentOrGuardianDto updateParentOrGuardian(Integer id,ParentOrGuardianDto parentOrGuardianDto){
        ParentOrGuardian parentOrGuardian = ParentUtil.toParentOrGuardian(parentOrGuardianDto);
        parentOrGuardian.setId(id);
        parentOrGuardian = parentOrGuardianDao.saveAndFlush(parentOrGuardian);
        return ParentUtil.toParentOrGuardianDto(parentOrGuardian);
    }

    public void deleteParentOrGuardianById(Integer id){
        parentOrGuardianDao.deleteById(id);
    }

    public List<ParentOrGuardianDto> searchByParentType(ParentType parentType) {
        try{
            return parentOrGuardianDao.findByParentType(parentType).stream()
                    .map(ParentUtil::toParentOrGuardianDto)
                    .collect(Collectors.toList());
        } catch(IllegalArgumentException e){
            throw new ParentOrGuardianNotFoundException("Invalid parent type: " +parentType);
        }
    }

    public ParentOrGuardianDto findParentWithStudents(String name) {
        ParentOrGuardian p = parentOrGuardianDao.findByDisplayName(name)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        return ParentUtil.toParentOrGuardianDto(p);
    }

    public ParentOrGuardianDto addStudentToParent(Integer id, ParentOrGuardianDto parentOrGuardianDto) {
        ParentOrGuardian parent = parentOrGuardianDao.findById(id)
                .orElseThrow(() -> new ParentOrGuardianNotFoundException(id));

        new ArrayList<>(parent.getStudents()).forEach(link -> {
            Student s = link.getStudent();
            parent.getStudents().remove(link);
            s.getParents().remove(link);
        });

        for (StudentSummaryDto sd: parentOrGuardianDto.getStudents()){
            Student student = null;

            if(sd.getId() != null){
                student = studentDao.findById(sd.getId())
                        .orElseThrow(() -> new StudentNotFoundException("Fuck you for not giving me a valid student id"));
            } else if (sd.getUsername() != null){
                student = studentDao.findByUsername(sd.getUsername())
                        .orElseThrow(() -> new StudentNotFoundException("Fuck you for not giving me a valid student username"));
            } else {
                throw new IllegalArgumentException("Must supply id or username for student");
            }

            StudentParent sp = new StudentParent(parent, student);


            student.getParents().add(sp);
        }

        ParentOrGuardian savedParent = parentOrGuardianDao.saveAndFlush(parent);
        return ParentUtil.toParentOrGuardianDto(savedParent);
    }
}
