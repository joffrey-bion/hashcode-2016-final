package org.ohutouch.hashcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No input file provided");
            return;
        }
        for (String inputFilename : args) {
            try {
                List<String> inputLines = Files.readAllLines(Paths.get(inputFilename));
                List<String> outputLines = run(inputLines);
                String outputFilename = inputFilename.replaceAll("in", "out");
                writeOutput(outputFilename, outputLines);
            } catch (IOException e) {
                System.err.println("Error during file I/O: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static List<String> run(List<String> inputLines) {
        Simulation simulation = InputReader.read(inputLines);
        List<Picture> picturesTaken = simulation.run();
        return picturesTaken.stream().map(Picture::toOutputLine).collect(Collectors.toList());
    }

    private static void writeOutput(String filename, List<String> lines) throws IOException {
        Path filePath = Paths.get(filename);
        Path parentDir = filePath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

}
