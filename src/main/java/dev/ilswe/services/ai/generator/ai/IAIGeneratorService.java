package dev.ilswe.services.ai.generator.ai;

public interface IAIGeneratorService<T> {
    public T generate(Integer recordNumber);
    public String testService();
}
