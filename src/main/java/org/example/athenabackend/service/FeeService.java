package org.example.athenabackend.service;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.FeeDao;
import org.example.athenabackend.dao.StudentDao;
import org.example.athenabackend.dto.FeeDto;
import org.example.athenabackend.dtoSummaries.FeeSummaryDto;
import org.example.athenabackend.request.MassFeeRequest;
import org.example.athenabackend.entity.Fee;
import org.example.athenabackend.entity.Student;
import org.example.athenabackend.exception.FeeNotFoundException;
import org.example.athenabackend.exception.StudentNotFoundException;
import org.example.athenabackend.model.FeeStatus;
import org.example.athenabackend.util.FeeUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeeService {
    private final FeeDao feeDao;
    private final StudentDao studentDao;

    public Page<FeeDto> getFeesForParent(Integer parentId, FeeStatus status, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").descending());
        Page<Fee> fees;

        if(status != null){
            fees = feeDao.findAllByParentIdAndStatus(parentId, status, pageable);
        } else {
            fees = feeDao.findAllByParentId(parentId, pageable);
        }
        return fees.map(FeeUtil::toFeeDto);
    }

    public FeeSummaryDto getUnpaidFeesSummary(int month, int year){
        BigDecimal total = feeDao.sumAmountByStatusAndMonthAndYear(FeeStatus.UNPAID, month, year);
        return new FeeSummaryDto(month, year, total);
    }

    public FeeSummaryDto getUnpaidFeesSummaryCurrentMonth(){
        LocalDate now = LocalDate.now();
        return getUnpaidFeesSummary(now.getMonthValue(), now.getYear());
    }

    public List<FeeDto> getByStudent(Integer studentId){
        return feeDao.findByStudentId(studentId).stream()
                .map(FeeUtil::toFeeDto)
                .toList();
    }

    public List<FeeDto> getByStatus(FeeStatus status){
        return feeDao.findByStatus(status).stream()
                .map(FeeUtil::toFeeDto)
                .toList();
    }

    public List<FeeDto> getByDueDateBetween(LocalDate start, LocalDate end){
        return feeDao.findByDueDateBetween(start, end).stream()
                .map(toFeeDto -> FeeUtil.toFeeDto(toFeeDto))
                .toList();
    }

    public List<FeeDto> getAllFees() {
        return feeDao.findAll().stream()
                .map(FeeUtil::toFeeDto)
                .toList();
    }

    public FeeDto addFee(FeeDto dto){
        Student student = studentDao.findById(dto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(dto.getStudentId()));
        Fee fee = FeeUtil.toFee(dto, student);
        System.out.println(fee.getStudent());
        student.addFee(fee);
        feeDao.save(fee);
        return FeeUtil.toFeeDto(fee);
    }

    public void createFeeForAllStudents(MassFeeRequest request){
        List<Student> students = studentDao.findAll();

        List<Fee> fees = students.stream()
                .map(student ->{
                   Fee fee = new Fee();
                   fee.setStudent(student);
                   fee.setAmount(request.amount());
                   fee.setDueDate(request.dueDate());
                   fee.setStatus(FeeStatus.UNPAID);
                   fee.setRemarks(request.remarks());
                   return fee;
                })
                .toList();

        feeDao.saveAll(fees);
    }

    public FeeDto updateFeeStatus(Integer id, LocalDate paymentDate){
        Fee fee = feeDao.findById(id).orElseThrow(() -> new FeeNotFoundException(id));

        fee.setStatus(FeeStatus.PAID);
        fee.setPaymentDate(paymentDate);

        Fee updatedFee = feeDao.save(fee);

        return FeeUtil.toFeeDto(updatedFee);
    }
}
