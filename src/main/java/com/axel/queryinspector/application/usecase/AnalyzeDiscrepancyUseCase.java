package com.axel.queryinspector.application.usecase;
import com.axel.queryinspector.config.AppProperties;
import com.axel.queryinspector.domain.model.Models.*;
import com.axel.queryinspector.domain.port.Ports.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;
@Service
public class AnalyzeDiscrepancyUseCase {
 private final DatabaseInspectionPort inspection;private final DiagnosticExecutionPort execution;private final AiAnalysisPort ai;private final AppProperties props;
 public AnalyzeDiscrepancyUseCase(DatabaseInspectionPort inspection,DiagnosticExecutionPort execution,AiAnalysisPort ai,AppProperties props){this.inspection=inspection;this.execution=execution;this.ai=ai;this.props=props;}
 public AuditResult execute(AuditReport report,String requestedSchema){
  if(report.expectedAmount()==null||report.actualAmount()==null) throw new IllegalArgumentException("Importes obligatorios");
  if(report.startDate()!=null&&report.endDate()!=null&&report.startDate().isAfter(report.endDate())) throw new IllegalArgumentException("Rango de fechas inválido");
  String schema=requestedSchema==null||requestedSchema.isBlank()?props.database().defaultSchema():requestedSchema;
  if(schema==null||!schema.matches("[A-Za-z][A-Za-z0-9_$#]{0,29}")) throw new IllegalArgumentException("Esquema inválido");
  DatabaseContext context=inspection.inspect(schema);Set<String> allowed=new HashSet<>();
  context.tables().forEach(n->allowed.add(n.toUpperCase(Locale.ROOT)));context.views().forEach(n->allowed.add(n.toUpperCase(Locale.ROOT)));
  DiagnosticPlan plan=ai.createPlan(report,context);
  if(plan.queries()==null||plan.queries().size()>5) throw new IllegalStateException("Plan inválido: máximo 5 consultas");
  List<ExecutedDiagnostic> evidence=new ArrayList<>();
  for(DiagnosticQuery q:plan.queries()) evidence.add(new ExecutedDiagnostic(q.purpose(),q.sql(),execution.executeReadOnly(q.sql(),Set.copyOf(allowed))));
  return ai.analyzeEvidence(report,context,plan,evidence);
 }
}
