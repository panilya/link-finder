package com.panilya.linkfinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinkFinder {
    public static void main(String[] args) {
        String regexString = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
        Pattern pattern = Pattern.compile(regexString);

        String pathToSearch = "C:\\Users\\Bizzard28\\Dropbox\\Комп'ютер\\Documents\\programs\\tldr";

        long startTime = System.nanoTime();

        List<LinkSearchContainer> containers;

        containers = listFilesInDir(Paths.get(pathToSearch)).stream()
                .map(path -> findRegex(pattern, path))
                .filter(container -> !container.isFound())
                .collect(Collectors.toList());

        containers.forEach(System.out::println);
        System.out.println(containers.size() + " Size of list with result");

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time: " + timeElapsed / 1000000);
    }

    private static LinkSearchContainer findRegex(Pattern pattern, Path path) {
        boolean match;
        try (Stream<String> filesStream = Files.lines(path)) {
            match = filesStream.map(input -> pattern.matcher(input).find())
                    .anyMatch(el -> el.equals(true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new LinkSearchContainer(path, match);
    }

    private static List<Path> listFilesInDir(Path path) {
        List<Path> pathList;
        try (Stream<Path> walker = Files.walk(path)) {
            pathList = walker.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".md"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pathList;
    }
}
