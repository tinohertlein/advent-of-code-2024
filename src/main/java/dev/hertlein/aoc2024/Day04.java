package dev.hertlein.aoc2024;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Day04 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return XMAS.of(inputLines).countWords();
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return CrossedMAS.of(inputLines).countWords();
    }

    record XMAS(List<Word> words) {

        @FunctionalInterface
        interface CharInDirection<T, U, V, W> {
            W apply(T t, U u, V v);
        }

        static XMAS of(List<String> inputLines) {
            var xmasWords = new ArrayList<Word>();

            CharInDirection<Integer, Integer, Integer, Character> horizontally = (y, x, i) -> charAt(inputLines, y, x + i);
            CharInDirection<Integer, Integer, Integer, Character> vertically = (y, x, i) -> charAt(inputLines, y + i, x);
            CharInDirection<Integer, Integer, Integer, Character> diagonallyRight = (y, x, i) -> charAt(inputLines, y + i, x + i);
            CharInDirection<Integer, Integer, Integer, Character> diagonallyLeft = (y, x, i) -> charAt(inputLines, y + i, x - i);

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {

                    for (var direction : List.of(horizontally, vertically, diagonallyRight, diagonallyLeft)) {
                        var word = createWordWithCharsIn(direction, y, x);
                        if (word.isXMAS()) {
                            xmasWords.add(word);
                        }
                    }
                }
            }
            return new XMAS(List.copyOf(xmasWords));
        }

        private static Word createWordWithCharsIn(CharInDirection<Integer, Integer, Integer, Character> direction, int y, int x) {
            return Word.of(IntStream.range(0, Word.LENGTH).mapToObj(i -> direction.apply(y, x, i)));
        }

        long countWords() {
            return words.size();
        }
    }

    record CrossedMAS(List<Word> words) {

        static CrossedMAS of(List<String> inputLines) {
            var crossWords = new ArrayList<Word>();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {
                    char topLeft = charAt(inputLines, y, x);
                    char topRight = charAt(inputLines, y, x + 2);
                    char center = charAt(inputLines, y + 1, x + 1);
                    char bottomLeft = charAt(inputLines, y + 2, x);
                    char bottomRight = charAt(inputLines, y + 2, x + 2);

                    Word word = Word.of(topLeft, topRight, center, bottomLeft, bottomRight);

                    if (word.isCrossedMAS()) {
                        crossWords.add(word);
                    }
                }
            }
            return new CrossedMAS(List.copyOf(crossWords));
        }

        long countWords() {
            return words.size();
        }
    }

    record Word(String chars) {

        static final int LENGTH = 4;

        static Word of(char topLeft, char topRight, char center, char bottomLeft, char bottomRight) {
            return new Word(Joiner.on("").join(topLeft, topRight, center, bottomLeft, bottomRight));
        }

        static Word of(Stream<Character> chars) {
            return new Word(chars.map(Object::toString).collect(Collectors.joining("")));
        }

        boolean isXMAS() {
            return chars.equals("XMAS") ||
                    chars.equals("SAMX");
        }

        boolean isCrossedMAS() {
            return chars.equals("MSAMS") ||
                    chars.equals("SMASM") ||
                    chars.equals("MMASS") ||
                    chars.equals("SSAMM");
        }
    }

    private static char charAt(List<String> inputLines, int y, int x) {
        if (y < 0 || x < 0) {
            return '.';
        }
        if (y >= inputLines.size() || x >= inputLines.get(y).length()) {
            return '.';
        }
        return inputLines.get(y).charAt(x);
    }
}