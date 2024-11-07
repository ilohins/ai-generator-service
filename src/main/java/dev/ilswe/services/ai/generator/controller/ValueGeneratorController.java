package dev.ilswe.services.ai.generator.controller;

import dev.ilswe.services.ai.generator.ai.IAIGeneratorService;
import dev.ilswe.services.ai.generator.entity.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("ags")
public class ValueGeneratorController {
    private final Logger log = LoggerFactory.getLogger(ValueGeneratorController.class);

    private final IAIGeneratorService<Values> aiService;

    @Value("${app.ai.generator.retry.num:3}")
    private int retryNum;

    public ValueGeneratorController(IAIGeneratorService<Values> aiService) {
        log.info("Initializing controller");

        this.aiService = aiService;
        String content = aiService.testService();

        log.info("Application startup joke (AI API Connection Test): {}", content);
    }

    @PostMapping(value = "/generate/{number}", produces = "application/json")
    public Values generate(@PathVariable(value = "number") int number) {
        log.info("Received request, Generating {} value[s]", number);

        Values returnVal = null;
        int attempt = 1;
        do {
            log.debug("Generating Values, attempt {}", attempt);

            try {
                returnVal = aiService.generate(number);
            } catch (Exception e) {
                log.warn("Failed to generate records due to {}, Attempt {} out of {}", e.getMessage(), attempt, retryNum);
                log.debug("Failed stackTrace: {}", Arrays.toString(e.getStackTrace()));
            }
            attempt += 1;
        } while(returnVal == null || attempt <= retryNum);

        log.info("Returning values back to controller, is successful? - {}", (returnVal != null));
        return returnVal;
    }
}
