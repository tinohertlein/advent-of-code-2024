package dev.hertlein.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;

import static dev.hertlein.aoc2024.Day15.ObstructionType.*;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

class Day15 implements Day<Void> {

    private final static char EMPTY_IDENTIFIER = '.';

    @Override
    public Object part1(List<String> inputLines, Void v) {
        var warehouseInput = inputLines.stream()
                .takeWhile(not(String::isEmpty))
                .toList();
        var movementInput = inputLines.stream()
                .dropWhile(not(String::isEmpty))
                .dropWhile(String::isEmpty)
                .toList();

        var warehouse = new Warehouse(warehouseInput);
        var movements = Movements.of(movementInput);

        return warehouse.sumOfBoxGPSCoordinatesAfter(movements);
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        var warehouseInput = inputLines.stream()
                .takeWhile(not(String::isEmpty))
                .toList();
        var movementInput = inputLines.stream()
                .dropWhile(not(String::isEmpty))
                .dropWhile(String::isEmpty)
                .toList();

        var warehouse = new Warehouse(warehouseInput).scaleUp();
        var movements = Movements.of(movementInput);

        return warehouse.sumOfBoxGPSCoordinatesAfter(movements);
    }

    @AllArgsConstructor
    @Getter
    static class Warehouse {
        private final int maxX;
        private final int maxY;
        private final Map<Coordinate, ObstructionType> obstructions;
        private Robot robot;

        Warehouse(List<String> inputLines) {
            obstructions = new HashMap<>();
            maxY = inputLines.size();
            maxX = inputLines.getFirst().length();

            for (int y = 0; y < inputLines.size(); y++) {
                for (int x = 0; x < inputLines.get(y).length(); x++) {
                    char symbol = inputLines.get(y).charAt(x);
                    if (ObstructionType.isObstruction(symbol)) {
                        obstructions.put(new Coordinate(x, y), ObstructionType.of(symbol));
                    } else if (Robot.isRobot(symbol)) {
                        robot = new Robot(new Coordinate(x, y));
                    }
                }
            }
        }

        Warehouse scaleUp() {
            var currentWarehouseState = toString();

            var scaledUpWarehouseState = stream(currentWarehouseState.split(""))
                    .map(string -> string.charAt(0))
                    .map(character -> {
                        if (Robot.isRobot(character)) {
                            return Character.toString(Robot.IDENTIFIER) + EMPTY_IDENTIFIER;
                        } else if (ObstructionType.isWall(character)) {
                            return Character.toString(WALL.identifier).repeat(2);
                        } else if (ObstructionType.isBox(character)) {
                            return WIDE_BOX_OPEN.identifier + "" + WIDE_BOX_CLOSE.identifier;
                        } else if (character == '\n') {
                            return "\n";
                        } else {
                            return Character.toString(EMPTY_IDENTIFIER).repeat(2);
                        }
                    }).collect(joining());

            return new Warehouse(asList(scaledUpWarehouseState.split("\n")));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    var coordinate = new Coordinate(x, y);
                    if (robot.position.equals(coordinate)) {
                        sb.append(robot);
                    } else if (obstructions.containsKey(coordinate)) {
                        sb.append(obstructions.get(coordinate).toString());
                    } else {
                        sb.append(EMPTY_IDENTIFIER);
                    }
                }
                sb.append("\n");
            }
            return sb.toString();
        }

        long sumOfBoxGPSCoordinatesAfter(Movements movements) {
            movements.values.forEach(robot::move);

            return obstructions
                    .entrySet()
                    .stream()
                    .filter(obstruction -> obstruction.getValue() == BOX || obstruction.getValue() == WIDE_BOX_OPEN)
                    .map(Map.Entry::getKey)
                    .mapToLong(position -> position.x + position.y * 100L)
                    .sum();
        }

        @AllArgsConstructor
        @Getter
        class Robot {
            private static final char IDENTIFIER = '@';

            private Coordinate position;

            static boolean isRobot(char identifier) {
                return identifier == IDENTIFIER;
            }

            void move(Movement movement) {
                var nextCoordinate = movement.forwardFrom(position);
                if (!obstructions.containsKey(nextCoordinate)) {
                    // Hit an empty position --> robot moves
                    position = nextCoordinate;
                } else {
                    var nextObstruction = obstructions.get(nextCoordinate);
                    if (nextObstruction == WALL) {
                        // Hit a wall --> robot stays in position
                    } else if (nextObstruction.isBoxOrWideBox()) {
                        // Hit a box --> robot tries to move boxes
                        List<Obstruction> boxesToMove = new ArrayList<>();
                        boxesToMove.add(new Obstruction(nextCoordinate, nextObstruction));

                        var moveableBoxes = findMoveableBoxes(movement, boxesToMove, new HashSet<>());
                        if (!moveableBoxes.isEmpty()) {
                            for (var box : moveableBoxes) {
                                obstructions.remove(box.position());
                            }
                            for (var box : moveableBoxes) {
                                obstructions.put(movement.forwardFrom(box.position()), box.type());
                            }
                            position = nextCoordinate;
                        } else {
                            // Hit a wall when robot was trying to move boxes --> robot stays in position
                        }
                    }
                }
            }

            private Set<Obstruction> findMoveableBoxes(Movement movement,
                                                       List<Obstruction> obstructionsToMove,
                                                       Set<Obstruction> moveableBoxes) {
                if (obstructionsToMove.isEmpty()) {
                    return moveableBoxes;
                }

                var currentObstruction = obstructionsToMove.removeFirst();

                if (currentObstruction.type() == WALL) {
                    // Hit a wall: no boxes moveable
                    return emptySet();
                }

                if (!moveableBoxes.contains(currentObstruction)) {
                    moveableBoxes.add(currentObstruction);

                    if (currentObstruction.type == ObstructionType.WIDE_BOX_OPEN) {
                        obstructionsToMove.add(new Obstruction(currentObstruction.position.rightOf(), WIDE_BOX_CLOSE));
                    }
                    if (currentObstruction.type == ObstructionType.WIDE_BOX_CLOSE) {
                        obstructionsToMove.add(new Obstruction(currentObstruction.position.leftOf(), WIDE_BOX_OPEN));
                    }

                    var nextCoordinate = movement.forwardFrom(currentObstruction.position());
                    var nextObstruction = obstructions.get(nextCoordinate);

                    if (nextObstruction != null) {
                        obstructionsToMove.add(new Obstruction(nextCoordinate, nextObstruction));
                    }
                }
                return findMoveableBoxes(movement, obstructionsToMove, moveableBoxes);
            }

            @Override
            public String toString() {
                return Character.toString(IDENTIFIER);
            }
        }
    }

    record Obstruction(Coordinate position, ObstructionType type) {
    }

    @RequiredArgsConstructor
    @Getter
    enum ObstructionType {
        BOX('O'), WALL('#'), WIDE_BOX_OPEN('['), WIDE_BOX_CLOSE(']');

        private static final Map<Character, ObstructionType> IDENTIFIERS = stream(ObstructionType.values())
                .collect(toMap(ObstructionType::getIdentifier, Function.identity()));

        private final char identifier;

        static ObstructionType of(char value) {
            return ObstructionType.IDENTIFIERS.get(value);
        }

        static boolean isObstruction(char identifier) {
            return IDENTIFIERS.containsKey(identifier);
        }

        static boolean isWall(char identifier) {
            return IDENTIFIERS.get(identifier) == WALL;
        }

        static boolean isBox(char identifier) {
            return IDENTIFIERS.get(identifier) == BOX;
        }

        boolean isBoxOrWideBox() {
            return this == BOX || this == WIDE_BOX_OPEN || this == WIDE_BOX_CLOSE;
        }

        @Override
        public String toString() {
            return Character.toString(identifier);
        }
    }

    record Coordinate(int x, int y) {

        Coordinate leftOf() {
            return new Coordinate(x - 1, y);
        }

        Coordinate rightOf() {
            return new Coordinate(x + 1, y);
        }
    }

    record Movements(List<Movement> values) {

        static Movements of(List<String> inputLines) {
            var movements = inputLines.stream()
                    .flatMap(line -> stream(line.split("")))
                    .map(string -> string.charAt(0))
                    .map(Movement::of)
                    .toList();
            return new Movements(movements);
        }
    }

    @Getter
    @RequiredArgsConstructor
    enum Movement {
        UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

        private static final Map<Character, Movement> IDENTIFIERS = stream(Movement.values())
                .collect(toMap(Movement::getIdentifier, Function.identity()));

        private final char identifier;

        static Movement of(char value) {
            return Movement.IDENTIFIERS.get(value);
        }

        Coordinate forwardFrom(Coordinate currentPosition) {
            return switch (this) {
                case UP -> new Coordinate(currentPosition.x, currentPosition.y - 1);
                case DOWN -> new Coordinate(currentPosition.x, currentPosition.y + 1);
                case LEFT -> new Coordinate(currentPosition.x - 1, currentPosition.y);
                case RIGHT -> new Coordinate(currentPosition.x + 1, currentPosition.y);
            };
        }
    }
}
