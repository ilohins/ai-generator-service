package dev.ilswe.services.ai.generator.ai.impl;

import dev.ilswe.services.ai.generator.ai.IAIGeneratorService;
import dev.ilswe.services.ai.generator.entity.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("openai")
public class OpenAIGeneratorService implements IAIGeneratorService<Values> {
    private final Logger log = LoggerFactory.getLogger(OpenAIGeneratorService.class);

    private final ChatModel chatModel;

    @Value("${app.ai.generator.test.prompt}")
    private String testPrompt;

    @Value("${app.ai.generator.topic}")
    private String valueTopic;

    @Value("${app.ai.directions.value1}")
    private String value1Instructions;

    @Value("${app.ai.directions.value2}")
    private String value2Instructions;

    public OpenAIGeneratorService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;

        log.info("Initializing Open AI Generator service");
    }

    @Override
    public Values generate(Integer recordNumber) {
        var outputConverter = new BeanOutputConverter<>(Values.class);
        var jsonSchema = outputConverter.getJsonSchema();

        Prompt prompt = new Prompt(formatPromt(recordNumber),
                OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .withResponseFormat(new OpenAiApi.ChatCompletionRequest.ResponseFormat(OpenAiApi.ChatCompletionRequest.ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
                        .build());

        ChatResponse response = this.chatModel.call(prompt);
        String content = response.getResult().getOutput().getContent();

        return outputConverter.convert(content);
    }

    @Override
    public String testService() {
        //Connection test, application will not start if test failed. Check API KEY
        Prompt prompt = new Prompt(testPrompt,
                OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .withTemperature(0.95)
                        .build());

        ChatResponse response = this.chatModel.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    private String formatPromt(Integer number) {
        return "Generate " + number + " of records;"
                + "Topic: " + valueTopic
                + "; value1:" + value1Instructions
                + "; value2:" + value2Instructions;
    }
}
