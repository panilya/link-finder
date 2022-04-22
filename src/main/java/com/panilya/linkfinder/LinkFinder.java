package com.panilya.linkfinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinkFinder {
    public static void main(String[] args) {
        String regexString = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
        Pattern pattern = Pattern.compile(regexString);

        String pathToFileWithoutLink = "C:\\Users\\Bizzard28\\Dropbox\\Комп'ютер\\Documents\\programs\\tldr\\pages\\common\\r2.md";
        String pathToSearch = "C:\\Users\\Bizzard28\\Dropbox\\Комп'ютер\\Documents\\programs\\tldr";

        long startTime = System.nanoTime();

        try {
            System.out.println(listFilesInDir(Paths.get(pathToSearch)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Boolean> list;
        try {
            list = listFilesInDir(Paths.get(pathToSearch)).stream()
                    .filter(filePath -> !filePath.endsWith(".md"))
                    .map(filePath -> {
                        try {
                            return findRegex(pattern, filePath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(list);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time: " + timeElapsed / 1000000000);
    }

    private static boolean findRegex(Pattern pattern, Path path) throws IOException {
        boolean match;
        try (Stream<String> filesStream = Files.lines(path)) {
            match = filesStream.map(input -> pattern.matcher(input).find())
                    .anyMatch(el -> el.equals(true));
        }
        System.out.println(match + " " + path.getFileName() + " " + path.toRealPath());

        return match;
    }

    private static List<Path> listFilesInDir(Path path) throws IOException {
        List<Path> pathList;
        try (Stream<Path> walker = Files.walk(path)) {
            pathList = walker.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".md"))
                    .collect(Collectors.toList());
        }
        return pathList;
    }
}
