package org.example.athenabackend.util;

import org.example.athenabackend.dto.FeeDto;
import org.example.athenabackend.entity.Fee;
import org.example.athenabackend.entity.Student;

public class FeeUtil {

    public static FeeDto toFeeDto(Fee fee){
        return new FeeDto(
                fee.getId(),
                fee.getStudent().getId(),
                fee.getAmount(),
                fee.getDueDate(),
                fee.getPaymentDate(),
                fee.getStatus(),
                fee.getRemarks()
        );
    }

    public static Fee toFee(FeeDto feeDto, Student student){
        return new Fee(
                student,
                feeDto.getAmount(),
                feeDto.getDueDate(),
                feeDto.getStatus(),
                feeDto.getPaymentDate(),
                feeDto.getRemarks()
        );
    }

}
