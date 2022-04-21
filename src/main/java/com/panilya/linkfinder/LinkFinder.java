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
    public static void main(String[] args) throws IOException {
        String regexString = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        Pattern pattern = Pattern.compile(regexString);
        String pathToFileTestingPurpose = "C:\\Users\\Bizzard28\\Dropbox\\Комп'ютер\\Documents\\programs\\tldr\\pages\\common\\r2.md";
        String pathToSearch = "C:\\Users\\Bizzard28\\Dropbox\\Комп'ютер\\Documents\\programs\\tldr";

//        Future<?> thread1 = executorService.submit(() -> {
//            try {
//                findRegex(pattern, pathToTestFile);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
        long startTime = System.nanoTime();
        System.out.println(listFilesInDir(Paths.get(pathToSearch)));
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time: " + timeElapsed / 1000000);
    }

    private static boolean findRegex(Pattern pattern, String pathToTestFile) throws IOException {
        boolean match;
        try (Stream<String> filesStream = Files.lines(Paths.get(pathToTestFile))) {
            match = filesStream.map(input -> pattern.matcher(input).find())
                    .anyMatch(el -> el.equals(true));
        }
        System.out.println(match);
        System.out.println(Paths.get(pathToTestFile).getFileName() + " " + Paths.get(pathToTestFile).toRealPath());

        return match;
    }

    private static List<Path> listFilesInDir(Path path) throws IOException {
        List<Path> pathList;
        try (Stream<Path> walker = Files.walk(path, 15)) {
            pathList = walker.filter(Files::isRegularFile)
//                    .filter(file -> !file.toAbsolutePath().endsWith(".md"))
                    .collect(Collectors.toList());
        }
        return pathList;
    }
}
