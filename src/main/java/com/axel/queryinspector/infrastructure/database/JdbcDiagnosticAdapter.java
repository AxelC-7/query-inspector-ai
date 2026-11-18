package com.axel.queryinspector.infrastructure.database;
import com.axel.queryinspector.config.AppProperties;
import com.axel.queryinspector.domain.port.Ports.DiagnosticExecutionPort;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class JdbcDiagnosticAdapter implements DiagnosticExecutionPort {
 private final JdbcTemplate jdbc; private final AppProperties props; private final ReadOnlySqlGuard guard;
 public JdbcDiagnosticAdapter(JdbcTemplate jdbc,AppProperties props,ReadOnlySqlGuard guard){this.jdbc=jdbc;this.props=props;this.guard=guard;}
 @Override public List<Map<String,Object>> executeReadOnly(String sql,Set<String> allowedObjects){
  guard.validate(sql,allowedObjects);
  return jdbc.query(con->{PreparedStatement ps=con.prepareStatement(sql);ps.setQueryTimeout(props.database().queryTimeoutSeconds());ps.setMaxRows(props.database().maxRows());return ps;},(ResultSet rs,int n)->{
   Map<String,Object> row=new LinkedHashMap<>();ResultSetMetaData md=rs.getMetaData();
   for(int i=1;i<=md.getColumnCount();i++) row.put(md.getColumnLabel(i),rs.getObject(i));return row;
  });
 }
}
