package com;

import com.exceptions.NotSuchDirException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс осуществляет поиск текстовых файлов в текущем рабочем каталоге в папке testProgram.
 * Найденные файлы сортируются по имени. Результаты объединяются и записываются в текстовый файл result.txt
 *
 * @autor Андрей Борисов (borisovandrey.work@gmail.com)
 */
public class Search {
    static final String FILES_DIRECTORY = System.getProperty("user.dir") + "\\testProgram";
    static final String RESULT_FILE_NAME = "result.txt";

    public static void main(String[] args) {
        List<File> files = new ArrayList<>();
        String pathForSearch = FILES_DIRECTORY;

        Path resultFile = Paths.get(FILES_DIRECTORY + "\\" + RESULT_FILE_NAME);
        if (Files.exists(resultFile)) {
            try {
                Files.delete(resultFile);
            } catch (IOException e) {
                System.out.println(String.format("Невозможно удалить результирующий файл. Причина: %s", e.getMessage()));
            }
        }

        search(pathForSearch, files);

        if (files.isEmpty()) {
            System.out.println("В указанной папке не найдено ни одного текстового файла!");
            return;
        }

        List<File> sortedFileList = files.stream()
                .sorted(Comparator.comparing(File::getName))
                .collect(Collectors.toList());


        StringBuilder builder = new StringBuilder();
        for (File file : sortedFileList) {

            try {
                builder.append(readFile(file.getPath()))
                        .append(";");

                writeFile(RESULT_FILE_NAME, builder.toString());

            } catch (IOException e) {
                System.out.println(String.format("Ошибка чтения файла: %s . Причина: %s", file.getName(), e.getMessage()));
            }
        }

        try {
            writeFile(RESULT_FILE_NAME, builder.toString());
        } catch (IOException e) {
            System.out.println(String.format("Ошибка записи результирующего файла. Причина: %s", e.getMessage()));
        }
    }

    /**
     * Метод поиска текстовых файлов в указанной дирректории.
     *
     * @param dirName   имя дирректории в которой осуществляется поиск
     * @param textFiles список в который будут добавлены найденные текстовые файлы
     */
    private static void search(String dirName, List<File> textFiles) {
        String dir = (dirName != null) ? dirName : FILES_DIRECTORY;
        File dirForSearch = new File(dir);

        if (!dirForSearch.exists()) {
            throw new NotSuchDirException(String.format("Дирректории: %s не существует", dirForSearch.getPath()));
        }

        List<File> fileList = Arrays.asList(dirForSearch.listFiles());
        fileList.forEach(file -> {
                    if (file.isDirectory())
                        search(file.getPath(), textFiles);
                    else {
                        try {
                            if (Files.probeContentType(file.toPath()).equals("text/plain")) {
                                textFiles.add(file);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * Метод производит чтение данных из тексового файла.
     *
     * @param fileName путь до файла
     * @return строковое представление содержимого файла
     */
    public static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    /**
     * Метод производит запись данных в текстовыый файл.
     *
     * @param fileName имя результирующего файла
     * @param fileData данный для записи в файл
     */
    public static void writeFile(String fileName, String fileData) throws IOException {
        Path resultPath = Paths.get(FILES_DIRECTORY + "\\" + fileName);
        if (!Files.exists(resultPath))
            Files.createFile(resultPath);
        Files.write(resultPath, fileData.getBytes());
    }
}
