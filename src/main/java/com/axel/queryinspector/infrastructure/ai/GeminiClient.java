package com.axel.queryinspector.infrastructure.ai;
import com.axel.queryinspector.config.AppProperties;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
@Component
public class GeminiClient {
 private final RestClient client; private final AppProperties props;
 public GeminiClient(RestClient client,AppProperties props){this.client=client;this.props=props;}
 public String generate(String prompt){
  var cfg=props.ai().gemini();
  Map<String,Object> body=Map.of("contents",List.of(Map.of("role","user","parts",List.of(Map.of("text",prompt)))));
  Map<?,?> response=client.post().uri("/v1beta/models/{model}:generateContent?key={key}",cfg.model(),cfg.apiKey()).body(body).retrieve()
   .onStatus(HttpStatusCode::isError,(req,res)->{throw new IllegalStateException("Gemini HTTP "+res.getStatusCode());}).body(Map.class);
  if(response==null) throw new IllegalStateException("Respuesta Gemini vacía");
  List<?> candidates=(List<?>)response.get("candidates");if(candidates==null||candidates.isEmpty()) throw new IllegalStateException("Gemini no devolvió candidatos");
  Map<?,?> content=(Map<?,?>)((Map<?,?>)candidates.get(0)).get("content");List<?> parts=(List<?>)content.get("parts");
  if(parts==null||parts.isEmpty()) throw new IllegalStateException("Gemini devolvió contenido vacío");
  return String.valueOf(((Map<?,?>)parts.get(0)).get("text"));
 }
}
