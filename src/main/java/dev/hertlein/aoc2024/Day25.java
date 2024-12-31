package dev.hertlein.aoc2024;

import com.google.common.collect.Streams;

import java.util.List;

import static dev.hertlein.aoc2024.Day25.Type.KEY;
import static dev.hertlein.aoc2024.Day25.Type.LOCK;
import static java.util.Arrays.asList;
import static java.util.Arrays.fill;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Gatherers.windowFixed;

class Day25 implements Day<Void> {

    @Override
    public Integer part1(List<String> inputLines, Void v) {
        return Schematics.of(inputLines).numberOfFittingLockKeyPairs();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        // No part 2 for day 25
        return 0L;
    }

    record Schematics(List<Schematic> keys, List<Schematic> locks) {

        static Schematics of(List<String> inputLines) {
            var keysAndLocks = inputLines.stream()
                    .filter(not(String::isEmpty))
                    .gather(windowFixed(7))
                    .map(Schematic::of)
                    .collect(groupingBy(Schematic::type));

            return new Schematics(keysAndLocks.get(KEY), keysAndLocks.get(LOCK));
        }

        int numberOfFittingLockKeyPairs() {
            int numberOfFittingLockKeyPairs = 0;

            for (var key : keys) {
                for (var lock : locks) {
                    if (Streams.zip(key.pins.stream(), lock.pins.stream(), Integer::sum).allMatch(sumOfPins -> sumOfPins <= 5)) {
                        numberOfFittingLockKeyPairs++;
                    }
                }
            }
            return numberOfFittingLockKeyPairs;
        }
    }

    record Schematic(Type type, List<Integer> pins) {

        static Schematic of(List<String> inputLines) {
            var schematicType = Type.of(inputLines.getFirst(), inputLines.getLast());
            var schematicLines = inputLines.subList(1, inputLines.size() - 1);

            Integer[] pins = new Integer[schematicLines.getFirst().length()];
            fill(pins, 0);

            for (String schematicLine : schematicLines) {
                for (int x = 0; x < schematicLine.length(); x++) {
                    if (isPin(schematicLine.charAt(x))) {
                        pins[x]++;
                    }
                }
            }
            return new Schematic(schematicType, asList(pins));
        }

        private static boolean isPin(char c) {
            return c == '#';
        }
    }

    enum Type {
        LOCK, KEY;

        static Type of(String firstLine, String lastLine) {
            if (firstLine.equals("#####") && lastLine.equals(".....")) {
                return LOCK;
            } else if (firstLine.equals(".....") && lastLine.equals("#####")) {
                return KEY;
            } else {
                throw new IllegalArgumentException("No type found in '%s' & '%s'".formatted(firstLine, lastLine));
            }
        }
    }
}
