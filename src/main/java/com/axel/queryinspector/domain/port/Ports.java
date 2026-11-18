package com.axel.queryinspector.domain.port;
import com.axel.queryinspector.domain.model.Models.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
public final class Ports {
 private Ports() {}
 public interface DatabaseInspectionPort { DatabaseContext inspect(String schema); }
 public interface DiagnosticExecutionPort { List<Map<String,Object>> executeReadOnly(String sql,Set<String> allowedObjects); }
 public interface AiAnalysisPort { DiagnosticPlan createPlan(AuditReport report,DatabaseContext context); AuditResult analyzeEvidence(AuditReport report,DatabaseContext context,DiagnosticPlan plan,List<ExecutedDiagnostic> evidence); }
}
