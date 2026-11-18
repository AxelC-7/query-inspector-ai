package com.axel.queryinspector.domain.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
public final class Models {
 private Models() {}
 public record AuditReport(String reportType,String reference,LocalDate startDate,LocalDate endDate,BigDecimal expectedAmount,BigDecimal actualAmount,String description) { public BigDecimal difference(){return expectedAmount.subtract(actualAmount);} }
 public record DatabaseContext(String schema,List<String> tables,List<String> views,List<String> columns,List<String> routines,List<String> dependencies,List<String> sourceCode) {}
 public record DiagnosticPlan(String reasoning,List<DiagnosticQuery> queries) {}
 public record DiagnosticQuery(String purpose,String sql) {}
 public record ExecutedDiagnostic(String purpose,String sql,List<Map<String,Object>> rows) {}
 public record AuditResult(String reference,BigDecimal expectedAmount,BigDecimal actualAmount,BigDecimal difference,String probableRootCause,List<String> evidence,List<String> recommendations,String confidence,List<ExecutedDiagnostic> diagnostics) {}
}
