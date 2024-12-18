package dev.hertlein.aoc2024;

import java.util.List;

public interface Day<T> {

    Void EMPTY = null;

    Object part1(List<String> inputLines, T additionalInput);

    Object part2(List<String> inputLines, T additionalInput);

}
