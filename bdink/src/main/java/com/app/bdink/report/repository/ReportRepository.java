package com.app.bdink.report.repository;

import com.app.bdink.member.entity.Member;
import com.app.bdink.report.domain.ReportCase;
import com.app.bdink.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByMemberAndReportCase(Member member, ReportCase reportCase);
}
