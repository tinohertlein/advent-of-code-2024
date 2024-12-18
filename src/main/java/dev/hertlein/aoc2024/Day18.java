package dev.hertlein.aoc2024;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static dev.hertlein.aoc2024.Day18.AdditionalInput;
import static java.util.function.Predicate.not;

class Day18 implements Day<AdditionalInput> {

    record AdditionalInput(Position start, Position end, int numberOfCorruptPositions) {
    }

    record Position(int x, int y) {

        @Override
        public String toString() {
            return x + "," + y;
        }
    }

    @Override
    public Object part1(List<String> inputLines, AdditionalInput additionalInput) {
        return new MemorySpace(inputLines.subList(0, additionalInput.numberOfCorruptPositions), additionalInput.start, additionalInput.end)
                .minimumNumberOfStepsToExit();
    }

    @Override
    public String part2(List<String> inputLines, AdditionalInput additionalInput) {
        return new MemorySpace(inputLines, additionalInput.start, additionalInput.end)
                .positionOfSpoilingByte().toString();
    }

    static class MemorySpace {
        private final static long NO_PATH_FOUND = -1;

        private final List<Position> corruptPositions;
        private final Position start;
        private final Position end;

        MemorySpace(List<String> inputLines, Position start, Position end) {
            this.corruptPositions = inputLines.stream()
                    .map(line -> {
                        var split = line.split(",");
                        return new Position(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                    })
                    .toList();

            this.start = start;
            this.end = end;
        }

        private boolean isWithinMemorySpace(Position position) {
            return position.x >= start.x && position.x <= end.x && position.y >= start.y && position.y <= end.y;
        }

        long minimumNumberOfStepsToExit() {
            return minimumNumberOfStepsToExit(new HashSet<>(this.corruptPositions));
        }

        private long minimumNumberOfStepsToExit(Set<Position> corruptedPositions) {
            var queue = new PriorityQueue<StepsToPosition>();
            var alreadySeen = new HashSet<Position>();
            queue.add(new StepsToPosition(start, 0));

            while (!queue.isEmpty()) {
                var popped = queue.poll();

                if (popped.position.equals(end)) {
                    return popped.steps;
                }

                var neighbours = Arrays.stream(Direction.values())
                        .map(direction -> direction.forwardFrom(popped.position))
                        .filter(this::isWithinMemorySpace)
                        .filter(not(corruptedPositions::contains))
                        .filter(not(alreadySeen::contains))
                        .toList();

                queue.addAll(neighbours.stream().map(neighbour -> new StepsToPosition(neighbour, popped.steps + 1)).toList());
                alreadySeen.addAll(neighbours);
            }
            return NO_PATH_FOUND;
        }

        Position positionOfSpoilingByte() {
            var candidates = new ArrayList<Position>();

            for (var candidate : corruptPositions) {
                candidates.add(candidate);
                if (minimumNumberOfStepsToExit(new HashSet<>(candidates)) == NO_PATH_FOUND) {
                    return candidate;
                }
            }
            throw new IllegalStateException("No byte found which is blocking the path to the exit.");
        }
    }

    record StepsToPosition(Position position, int steps) implements Comparable<StepsToPosition> {

        @Override
        public int compareTo(StepsToPosition other) {
            return this.steps - other.steps;
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Direction {
        NORTH, SOUTH, WEST, EAST;

        Position forwardFrom(Position position) {
            return switch (this) {
                case NORTH -> new Position(position.x, position.y - 1);
                case SOUTH -> new Position(position.x, position.y + 1);
                case WEST -> new Position(position.x - 1, position.y);
                case EAST -> new Position(position.x + 1, position.y);
            };
        }
    }
}
