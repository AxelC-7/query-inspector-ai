package com.axel.queryinspector.config;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
@Configuration
public class HttpClientConfiguration {
 @Bean RestClient geminiRestClient(AppProperties p) {
  var f=new SimpleClientHttpRequestFactory(); f.setConnectTimeout(Duration.ofSeconds(10));
  f.setReadTimeout(Duration.ofSeconds(p.ai().gemini().timeoutSeconds()));
  return RestClient.builder().baseUrl(p.ai().gemini().baseUrl()).requestFactory(f).build();
 }
}
