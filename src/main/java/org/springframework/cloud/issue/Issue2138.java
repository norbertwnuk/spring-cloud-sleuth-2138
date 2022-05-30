package org.springframework.cloud.issue;

import brave.baggage.BaggageField;
import brave.baggage.CorrelationScopeConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class Issue2138 {

    public static void main(String[] args) {
        SpringApplication.run(Issue2138.class, args);
    }

    @Bean
    CurrentTraceContext.ScopeDecorator mdcScopeDecorator() {
        return MDCScopeDecorator.newBuilder().clear().add(
                CorrelationScopeConfig.SingleCorrelationField.newBuilder(BaggageField.create("x-request-id-2")).flushOnUpdate().build()
        ).build();
    }

    @GetMapping("/")
    public String work() {
        Map<String, String> allValues = BaggageField.getAllValues();
        String id = MDC.get("x-request-id");
        String id2 = MDC.get("x-request-id-2");
        return "OK";
    }
}
