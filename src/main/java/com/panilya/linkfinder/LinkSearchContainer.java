package com.panilya.linkfinder;

import java.nio.file.Path;

public class LinkSearchContainer {
    private final Path pathToFile;
    private final boolean result;

    public LinkSearchContainer(Path pathToFile, boolean result) {
        this.pathToFile = pathToFile;
        this.result = result;
    }

    public Path getPathToFile() {
        return pathToFile;
    }

    public boolean isFound() {
        return result;
    }

    @Override
    public String toString() {
        return "LinkSearchContainer{" +
                "pathToFile=" + pathToFile.toString() +
                ", result=" + result +
                '}';
    }
}
