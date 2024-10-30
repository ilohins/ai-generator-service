package net.l3ns.services.ai.generator.ai.impl;

import net.l3ns.services.ai.generator.ai.IAIGeneratorService;
import net.l3ns.services.ai.generator.entity.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Profile("ollama")
public class OllamaAIGeneratorService implements IAIGeneratorService<Values> {

    private final Logger log = LoggerFactory.getLogger(OllamaAIGeneratorService.class);

    private final OllamaChatModel ollamaClient;

    @Value("app.ai.generator.test.prompt")
    private String testPrompt;

    @Value("${app.ai.generator.topic}")
    private String valueTopic;

    @Value("${app.ai.directions.value1}")
    private String value1Instructions;

    @Value("${app.ai.directions.value2}")
    private String value2Instructions;

    public OllamaAIGeneratorService(OllamaChatModel chatModel) {
        super();

        this.ollamaClient = chatModel;

        log.info("Initializing Ollama AI Generator service");
    }

    @Override
    public Values generate(Integer number) {
        log.info("Received request, Generating {} value[s]", number);

        var outputConverter = new BeanOutputConverter<>(Values.class);
        Prompt prompt = getPrompt(number, outputConverter.getFormat());

        Generation generation = this.ollamaClient.call(prompt).getResult();

        log.info("Received processed successfully, sending output to requester");
        return outputConverter.convert(generation.getOutput().getContent());
    }

    @Override
    public String testService() {
        Prompt prompt = new Prompt("Tell me an application startup joke",
                OllamaOptions.builder()
                        .withTemperature(0.85)
                        .withModel(OllamaModel.LLAMA3_2)
                        .build());

        ChatResponse response = this.ollamaClient.call(prompt);
        return response.getResult().getOutput().getContent();
    }


    private Prompt getPrompt(int number, String format) {

        String promptText = """
                Generate {number} of records;
                Topic: {topic}
                Value 1 instructions: {value1}
                Value 2 instructions: {value2}
                Output format: {format}
                """;

        PromptTemplate template = new PromptTemplate(promptText, Map.of("number", number,"topic", valueTopic,
                "value1", value1Instructions, "value2",value2Instructions,"format", format));

        return template.create();
    }
}

