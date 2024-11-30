package dev.hertlein.aoc2024.lib;

import com.google.common.io.Resources;
import dev.hertlein.aoc2024.Day;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class InputReader {

    public static List<String> readSampleInputFor(Day day) {
        return readInputFor(day, "sample");
    }

    public static List<String> readChallengeInputFor(Day day) {
        return readInputFor(day, "challenge");
    }

    @SneakyThrows
    private static List<String> readInputFor(Day day, String type) {
        var className = classNameOf(day);
        var filename = filenameFrom(className, type);
        return readInputFrom(filename);
    }

    private static String classNameOf(Day day) {
        return day.getClass().getSimpleName();
    }

    private static String filenameFrom(String dayString, String suffix) {
        return dayString + "-" + suffix + ".txt";
    }

    @SneakyThrows
    private static List<String> readInputFrom(String filename) {
        return Resources.toString(Resources.getResource(filename), UTF_8).trim().lines().toList();
    }
}
