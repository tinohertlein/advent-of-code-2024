package dev.hertlein.aoc2024;

import com.google.common.base.Joiner;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static dev.hertlein.aoc2024.Day03.Accumulator.NOT_NEEDED;

class Day03 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        var joinedLines = Joiner.on("").join(inputLines);
        Pattern regex = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

        return regex
                .matcher(joinedLines)
                .results()
                .map(Multiplication::of)
                .mapToLong(Multiplication::product)
                .sum();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        var joinedLines = Joiner.on("").join(inputLines);
        Pattern regex = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)");

        return regex
                .matcher(joinedLines)
                .results()
                .reduce(Accumulator.empty(),
                        Accumulator::add,
                        // No need for a combiner, as non-parallel execution of this stream is mandatory because ordering of instructions is crucial
                        (Accumulator _, Accumulator _) -> NOT_NEEDED
                )
                .sum;
    }

    record Accumulator(boolean isMultiplicationEnabled, long sum) {

        static final Accumulator NOT_NEEDED = empty();

        static Accumulator empty() {
            return new Accumulator(true, 0);
        }

        Accumulator add(MatchResult matchResult) {
            return switch (matchResult.group(0)) {
                case "do()" -> new Accumulator(true, sum);
                case "don't()" -> new Accumulator(false, sum);
                default -> new Accumulator(isMultiplicationEnabled, sum + productOf(matchResult));
            };
        }

        private int productOf(MatchResult matchResult) {
            return isMultiplicationEnabled ? Multiplication.of(matchResult).product() : 0;
        }
    }

    record Multiplication(int multiplicand, int multiplier) {

        static Multiplication of(MatchResult matchResult) {
            return Multiplication.of(matchResult.group(1), matchResult.group(2));
        }

        private static Multiplication of(String multiplicand, String multiplier) {
            return new Multiplication(Integer.parseInt(multiplicand), Integer.parseInt(multiplier));
        }

        int product() {
            return multiplicand * multiplier;
        }
    }
}
