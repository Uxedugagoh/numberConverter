package com.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public class NumeralDataLoader {
    // Путь до JSON файла
    private static final String JSON_FILE_PATH = "/numeralForms.json";


    public static NumeralForms loadData() {
        // Класс для чтения JSON файла и преобразования его в объект из библиотеки Jackson
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = NumeralDataLoader.class.getResourceAsStream(JSON_FILE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Файл не найден: " + JSON_FILE_PATH);
            }
            return mapper.readValue(inputStream, NumeralForms.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении JSON-файла", e);
        }
    }
}