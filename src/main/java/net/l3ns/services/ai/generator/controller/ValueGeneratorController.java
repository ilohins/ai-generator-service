package net.l3ns.services.ai.generator.controller;

import net.l3ns.services.ai.generator.entity.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vg")
public class ValueGeneratorController {
    private final Logger log = LoggerFactory.getLogger(ValueGeneratorController.class);

    private final ChatModel chatModel;

    @Value("${app.ai.generator.topic}")
    private String valueTopic;

    @Value("${app.ai.directions.value1}")
    private String value1Instructions;

    @Value("${app.ai.directions.value2}")
    private String value2Instructions;

    public ValueGeneratorController(ChatModel chatModel) {
        this.chatModel = chatModel;

        //Connection test, application will not start if test failed. Check API KEY
        Prompt prompt = new Prompt("Tell me an application startup joke",
                OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .build());

        ChatResponse response = this.chatModel.call(prompt);
        String content = response.getResult().getOutput().getContent();

        log.info("Application startup joke (Connection Test): {}", content);
    }

    @PostMapping(value = "/generate/{number}", produces = "application/json")
    public Values generate(@PathVariable(value = "number") int number) {
        log.info("Received request, Generating {} value[s]", number);

        var outputConverter = new BeanOutputConverter<>(Values.class);
        var jsonSchema = outputConverter.getJsonSchema();

        String promptText = "Generate " + number + " of records;"
                + "Topic: " + valueTopic
                + "; value1:" + value1Instructions
                + "; value2:" + value2Instructions;

        Prompt prompt = new Prompt(promptText,
                OpenAiChatOptions.builder()
                        .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
                        .build());

        ChatResponse response = this.chatModel.call(prompt);
        String content = response.getResult().getOutput().getContent();

        log.info("Received processed successfully, sending output to requester");
        return outputConverter.convert(content);
    }
}
