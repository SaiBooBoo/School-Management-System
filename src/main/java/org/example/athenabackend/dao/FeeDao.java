package org.example.athenabackend.dao;

import org.example.athenabackend.entity.Fee;
import org.example.athenabackend.model.FeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FeeDao extends JpaRepository<Fee, Integer> {

    List<Fee> findByStudentId(Integer studentId);
    List<Fee> findByStatus(FeeStatus status);
    List<Fee> findByDueDateBetween(LocalDate start, LocalDate end);

    @Query("""
       SELECT COALESCE(SUM(f.amount), 0)
        FROM Fee f
        WHERE f.status = :status
          AND FUNCTION('MONTH', f.dueDate) = :month
          AND FUNCTION('YEAR',  f.dueDate) = :year
    """)
    BigDecimal sumAmountByStatusAndMonthAndYear( @Param("status") FeeStatus status,
                                                 @Param("month") int month,
                                                 @Param("year") int year);

    @Query("""
     SELECT f FROM Fee f
        JOIN f.student s
        JOIN s.parents sp
        WHERE sp.parentOrGuardian.id = :parentId
    """)
    Page<Fee> findAllByParentId( @Param("parentId") Integer parentId, Pageable pageable);

    @Query("""
    SELECT f FROM Fee f
    JOIN f.student s
    JOIN s.parents sp
    WHERE sp.parentOrGuardian.id = :parentId
    AND f.status = :status
    """)
    Page<Fee> findAllByParentIdAndStatus(@Param("parentId") Integer parentId, @Param("status") FeeStatus status, Pageable pageable);
}
