package com.axel.queryinspector.presentation.controller;
import com.axel.queryinspector.application.usecase.AnalyzeDiscrepancyUseCase;
import com.axel.queryinspector.domain.model.Models.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/audits")
public class AuditController {
 private final AnalyzeDiscrepancyUseCase useCase;public AuditController(AnalyzeDiscrepancyUseCase useCase){this.useCase=useCase;}
 public record Request(@NotBlank String reportType,@NotBlank String reference,LocalDate startDate,LocalDate endDate,@NotNull BigDecimal expectedAmount,@NotNull BigDecimal actualAmount,@NotBlank String description,String schema){}
 @PostMapping("/analyze") public ResponseEntity<AuditResult> analyze(@Valid @RequestBody Request r){return ResponseEntity.ok(useCase.execute(new AuditReport(r.reportType(),r.reference(),r.startDate(),r.endDate(),r.expectedAmount(),r.actualAmount(),r.description()),r.schema()));}
}
