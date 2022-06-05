package com;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс содержит тесты для методов класса SearchTest.
 *
 * @autor Андрей Борисов (borisovandrey.work@gmail.com)
 */
class SearchTest {

    @Test
    void should_create_result_file_and_write_data() throws IOException {
        //Given
        String fileName = "newFileForWrite.txt";
        String fileData = "текст для записи!";

        //When
        Search.writeFile(fileName, fileData);

        //Then
        assertTrue(Files.exists(Paths.get(Search.FILES_DIRECTORY + "\\" + fileName)));

        //Clear data
        Files.delete(Paths.get(Search.FILES_DIRECTORY + "\\" + fileName));
    }

    @Test
    void should_read_data_from_file() throws IOException {
        //Given
        String fileName = "newFileForRead.txt";
        String fileNamePath = Search.FILES_DIRECTORY + "\\" + fileName;
        String fileData = "текст для чтения!";

        Search.writeFile(fileName, fileData);

        //When
        String result = Search.readFile(fileNamePath);

        //Then
        assertEquals(fileData, result);

        //Clear data
        Files.delete(Paths.get(fileNamePath));
    }

    @Test
    void should_select_only_text_files_and_write_results_in_result_file() throws IOException {
        //Given
        String fileNamePath = Search.FILES_DIRECTORY + "\\" + Search.RESULT_FILE_NAME;
        String expectedResult = "cccc;gggg;yyyy;zzzz;";
        //When
        Search.main(null);

        //Then
        String actualResult = Search.readFile(fileNamePath);
        assertEquals(expectedResult, actualResult);

        //Clear data
        Files.delete(Paths.get(fileNamePath));
    }
}