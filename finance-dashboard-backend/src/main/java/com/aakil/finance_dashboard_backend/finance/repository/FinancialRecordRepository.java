package com.aakil.finance_dashboard_backend.finance.repository;

import com.aakil.finance_dashboard_backend.finance.entity.FinancialRecord;
import com.aakil.finance_dashboard_backend.finance.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    List<FinancialRecord> findByDeletedFalse();

  Page<FinancialRecord> findByDeletedFalse(Pageable pageable);

    Optional<FinancialRecord> findByIdAndDeletedFalse(Long id);

    long countByDeletedFalse();

    long countByDeletedTrue();

    List<FinancialRecord> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    List<FinancialRecord> findByDeletedFalseAndTypeOrderByAmountDesc(RecordType type, Pageable pageable);

        @Query("""
          select coalesce(sum(fr.amount), 0)
          from FinancialRecord fr
          where fr.deleted = false
            and fr.type = :type
          """)
        BigDecimal sumAmountByType(RecordType type);

        @Query("""
          select coalesce(sum(fr.amount), 0)
          from FinancialRecord fr
          where fr.deleted = false
            and fr.type = :type
            and fr.transactionDate between :from and :to
          """)
        BigDecimal sumAmountByTypeAndTransactionDateBetween(RecordType type, LocalDate from, LocalDate to);

        long countByDeletedFalseAndTransactionDateBetween(LocalDate from, LocalDate to);

        @Query("""
          select coalesce(sum(fr.amount), 0)
          from FinancialRecord fr
          where fr.deleted = false
            and fr.createdBy.id = :userId
            and fr.type = :type
          """)
        BigDecimal sumAmountByUserAndType(Long userId, RecordType type);

        long countByCreatedByIdAndDeletedFalse(Long userId);

        @Query("""
          select max(fr.transactionDate)
          from FinancialRecord fr
          where fr.deleted = false
            and fr.createdBy.id = :userId
          """)
        LocalDate findLastActivityByUserId(Long userId);

    @Query("""
            select
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.INCOME then fr.amount else 0 end), 0),
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.EXPENSE then fr.amount else 0 end), 0)
            from FinancialRecord fr
            where fr.deleted = false
            """)
    Object[] findOverallIncomeExpenseSummary();

    @Query("""
            select
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.INCOME then fr.amount else 0 end), 0),
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.EXPENSE then fr.amount else 0 end), 0),
                count(fr)
            from FinancialRecord fr
            where fr.deleted = false
              and year(fr.transactionDate) = :year
              and month(fr.transactionDate) = :month
            """)
    Object[] findMonthlySummary(int year, int month);

    @Query("""
            select
                fr.transactionDate,
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.INCOME then fr.amount else 0 end), 0),
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.EXPENSE then fr.amount else 0 end), 0)
            from FinancialRecord fr
            where fr.deleted = false
              and fr.transactionDate between :from and :to
            group by fr.transactionDate
            order by fr.transactionDate asc
            """)
    List<Object[]> findTrendData(LocalDate from, LocalDate to);

    @Query("""
            select
                fr.category,
                coalesce(sum(fr.amount), 0)
            from FinancialRecord fr
            where fr.deleted = false
              and fr.type = :type
            group by fr.category
            order by sum(fr.amount) desc
            """)
    List<Object[]> findCategoryBreakdown(RecordType type);

    @Query("""
            select
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.INCOME then fr.amount else 0 end), 0),
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.EXPENSE then fr.amount else 0 end), 0),
                count(fr),
                max(fr.transactionDate)
            from FinancialRecord fr
            where fr.deleted = false
              and fr.createdBy.id = :userId
            """)
    Object[] findUserSummary(Long userId);

    @Query("""
            select
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.INCOME then 1 else 0 end), 0),
                coalesce(sum(case when fr.type = com.aakil.finance_dashboard_backend.finance.entity.RecordType.EXPENSE then 1 else 0 end), 0)
            from FinancialRecord fr
            where fr.deleted = false
            """)
    Object[] findRecordCountByType();

    @Query("""
            select
                u.id,
                u.name,
                count(fr)
            from User u
            left join FinancialRecord fr on fr.createdBy = u and fr.deleted = false
            group by u.id, u.name
            order by count(fr) desc
            """)
    List<Object[]> findRecordCountByUser();
}
