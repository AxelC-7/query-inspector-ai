package com.axel.queryinspector.infrastructure.database;
import com.axel.queryinspector.domain.model.Models.DatabaseContext;
import com.axel.queryinspector.domain.port.Ports.DatabaseInspectionPort;
import java.util.List;
import java.util.Locale;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class OracleMetadataAdapter implements DatabaseInspectionPort {
 private final JdbcTemplate jdbc;
 public OracleMetadataAdapter(JdbcTemplate jdbc){this.jdbc=jdbc;}
 @Override public DatabaseContext inspect(String schema){
  String owner=schema.toUpperCase(Locale.ROOT);
  List<String> tables=q("SELECT table_name FROM all_tables WHERE owner=? ORDER BY table_name",owner);
  List<String> views=q("SELECT view_name FROM all_views WHERE owner=? ORDER BY view_name",owner);
  List<String> columns=q("SELECT table_name||'.'||column_name||' ['||data_type||']' FROM all_tab_columns WHERE owner=? ORDER BY table_name,column_id",owner);
  List<String> routines=q("SELECT object_name||' ['||object_type||']' FROM all_objects WHERE owner=? AND object_type IN ('PROCEDURE','FUNCTION','PACKAGE','TRIGGER') ORDER BY object_type,object_name",owner);
  List<String> deps=q("SELECT name||' -> '||referenced_owner||'.'||referenced_name FROM all_dependencies WHERE owner=? AND referenced_owner IS NOT NULL",owner);
  List<String> source=jdbc.query("SELECT * FROM (SELECT name||' ['||type||'] '||line||': '||text AS source_line FROM all_source WHERE owner=? AND type IN ('PROCEDURE','FUNCTION','PACKAGE','PACKAGE BODY','TRIGGER') ORDER BY name,type,line) WHERE ROWNUM <= 1500",(rs,n)->rs.getString(1),owner);
  return new DatabaseContext(owner,tables,views,columns,routines,deps,source);
 }
 private List<String> q(String sql,String owner){return jdbc.query(sql,(rs,n)->rs.getString(1),owner);}
}
