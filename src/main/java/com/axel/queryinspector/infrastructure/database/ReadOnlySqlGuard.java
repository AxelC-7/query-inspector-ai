package com.axel.queryinspector.infrastructure.database;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
@Component
public class ReadOnlySqlGuard {
 private static final Pattern OBJECT=Pattern.compile("(?i)\\b(?:FROM|JOIN)\\s+([A-Z0-9_$#\\.\\\"]+)");
 private static final Pattern SIDE_EFFECT=Pattern.compile("(?i)\\b(?:DBMS_|UTL_|SYS\\.)[A-Z0-9_$#]*|\\b(?:NEXTVAL|CURRVAL)\\b|@");
 private static final Set<String> BLOCKED=Set.of("INSERT","UPDATE","DELETE","MERGE","DROP","ALTER","CREATE","TRUNCATE","GRANT","REVOKE","COMMIT","ROLLBACK","EXECUTE","CALL","DECLARE","BEGIN","LOCK","INTO");
 public void validate(String sql,Set<String> allowedObjects){
  if(sql==null||sql.isBlank()) throw new IllegalArgumentException("SQL vacío");
  String s=sql.strip(),u=s.toUpperCase(Locale.ROOT);
  if(!(u.startsWith("SELECT ")||u.startsWith("WITH "))) throw new IllegalArgumentException("Solo SELECT/WITH está permitido");
  if(s.contains(";")||s.contains("--")||s.contains("/*")||s.contains("*/")) throw new IllegalArgumentException("Comentarios y múltiples sentencias no permitidos");
  if(u.contains(" FOR UPDATE")) throw new IllegalArgumentException("FOR UPDATE no permitido");
  if(SIDE_EFFECT.matcher(u).find()) throw new IllegalArgumentException("Referencia a paquete, secuencia o database link no permitido");
  for(String token:BLOCKED) if(Pattern.compile("(?s).*\\b"+Pattern.quote(token)+"\\b.*").matcher(u).matches()) throw new IllegalArgumentException("Operación SQL bloqueada: "+token);
  Matcher m=OBJECT.matcher(s);
  while(m.find()){
   String object=m.group(1).replace("\"","").toUpperCase(Locale.ROOT);
   String simple=object.contains(".")?object.substring(object.lastIndexOf('.')+1):object;
   if(!allowedObjects.contains(simple)&&!allowedObjects.contains(object)) throw new IllegalArgumentException("Objeto fuera de allowlist: "+object);
  }
 }
}
