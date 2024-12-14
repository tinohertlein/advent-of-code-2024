package dev.hertlein.aoc2024;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static dev.hertlein.aoc2024.Day14.AreaSize;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class Day14 implements Day<AreaSize> {

    record AreaSize(int x, int y) {
    }

    // trial & error: "most of the robots should arrange themselves into a picture of a Christmas tree"
    private final static int THRESHOLD_FOR_CONNECTED_ROBOTS_FORMING_CHRISTMAS_TREE = 200;

    @Override
    public Object part1(List<String> inputLines, AreaSize areaSize) {
        return safetyFactor(areaSize, inputLines);
    }

    @Override
    public Object part2(List<String> inputLines, AreaSize areaSize) {
        return new Robots(inputLines).findChristmasTree(areaSize);
    }

    private long safetyFactor(AreaSize areaSize, List<String> inputLines) {
        var robots = new Robots(inputLines);
        robots.navigate(areaSize, 100);
        return robots
                .splitIntoQuadrants(areaSize)
                .mapToInt(Quadrant::size)
                .reduce(1, (a, b) -> a * b);
    }

    @Data
    static class Robots {
        private final List<Robot> robots;

        Robots(List<String> inputLines) {
            this.robots = inputLines.stream().map(Robot::new).toList();
        }

        Robots(Robot... robots) {
            this.robots = Arrays.asList(robots);
        }

        void navigate(AreaSize areaSize, int times) {
            this.robots.forEach(robot -> robot.navigate(areaSize, times));
        }

        long findChristmasTree(AreaSize areaSize) {
            for (int second = 1; second < Integer.MAX_VALUE; second++) {
                this.robots.forEach(robot -> robot.navigate(areaSize, 1));

                if (sizeOfLargestAreaWithConnectedRobots() >= THRESHOLD_FOR_CONNECTED_ROBOTS_FORMING_CHRISTMAS_TREE) {
                    printImage(areaSize);
                    return second;
                }
            }
            return 0;
        }

        long sizeOfLargestAreaWithConnectedRobots() {
            Map<Position, Robot> robotPositions = robots.stream()
                    .collect(toMap(
                            Robot::getCurrentPosition,
                            Function.identity(),
                            (r1, _) -> r1));

            var robotsToCheck = new ArrayList<>(robotPositions.values());
            var areasOfConnectedRobots = new ArrayList<ConnectedRobotsArea>();

            while (!robotsToCheck.isEmpty()) {
                var robotToCheck = robotsToCheck.getFirst();
                var area = new ConnectedRobotsArea();
                fillAreaWithConnectedRobots(area, robotToCheck, robotPositions);
                robotsToCheck.removeAll(area.robots);
                areasOfConnectedRobots.add(area);
            }
            return areasOfConnectedRobots
                    .stream()
                    .mapToLong(ConnectedRobotsArea::size)
                    .max()
                    .orElse(0L);
        }

        private void fillAreaWithConnectedRobots(ConnectedRobotsArea connectedRobots, Robot robot, Map<Position, Robot> robotPositions) {
            var neighbours = robot
                    .currentPosition
                    .neighbours()
                    .stream()
                    .map(robotPositions::get)
                    .filter(Objects::nonNull)
                    .filter(not(connectedRobots::contains))
                    .collect(toSet());

            if (connectedRobots.contains(robot) && neighbours.isEmpty()) {
                return;
            }

            connectedRobots.add(robot);
            for (var next : neighbours) {
                fillAreaWithConnectedRobots(connectedRobots, next, robotPositions);
            }
        }

        private void printImage(AreaSize size) {
            var positionsOfRobots = robots.stream()
                    .map(Robot::getCurrentPosition)
                    .collect(toSet());

            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < size.y; y++) {
                for (int x = 0; x < size.x; x++) {
                    if (positionsOfRobots.contains(new Position(x, y))) {
                        sb.append("#");
                    } else {
                        sb.append(".");
                    }
                }
                sb.append("\r\n");
            }
            System.out.println(sb);
        }

        Stream<Quadrant> splitIntoQuadrants(AreaSize size) {
            var quadrantSize = new AreaSize(size.x / 2, size.y / 2);

            Predicate<Robot> upperLeft = robot -> robot.currentPosition.x < quadrantSize.x & robot.currentPosition.y < quadrantSize.y;
            Predicate<Robot> upperRight = robot -> robot.currentPosition.x > quadrantSize.x & robot.currentPosition.y < quadrantSize.y;
            Predicate<Robot> lowerLeft = robot -> robot.currentPosition.x < quadrantSize.x & robot.currentPosition.y > quadrantSize.y;
            Predicate<Robot> lowerRight = robot -> robot.currentPosition.x > quadrantSize.x & robot.currentPosition.y > quadrantSize.y;

            return Stream.of(upperLeft, upperRight, lowerLeft, lowerRight)
                    .map(predicate -> new Quadrant(robots.stream().filter(predicate).toList()));
        }
    }

    record Quadrant(List<Robot> robots) {

        int size() {
            return robots.size();
        }
    }

    @RequiredArgsConstructor
    static class ConnectedRobotsArea {
        private final Set<Robot> robots;

        ConnectedRobotsArea() {
            this.robots = new HashSet<>();
        }

        void add(Robot robot) {
            this.robots.add(robot);
        }

        boolean contains(Robot robot) {
            return robots.contains(robot);
        }

        int size() {
            return robots.size();
        }
    }

    @Data
    static class Robot {
        private final Velocity velocity;
        private Position currentPosition;

        Robot(String line) {
            var split = line.split("\\s");
            this.currentPosition = Position.of(split[0]);
            this.velocity = Velocity.of(split[1]);
        }

        Robot(Position position) {
            this.currentPosition = position;
            this.velocity = null;
        }

        void navigate(AreaSize areaSize, int times) {
            var x = currentPosition.x + velocity.x * times;
            var y = currentPosition.y + velocity.y * times;
            currentPosition = new Position(x, y).wrapAround(areaSize);
        }
    }

    record Position(int x, int y) {
        private static final Pattern REGEX = Pattern.compile("(\\d+),(\\d+)");

        static Position of(String input) {
            var matcher = REGEX.matcher(input);

            if (matcher.find()) {
                return new Position(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            } else {
                throw new IllegalArgumentException("No position found in '%s'".formatted(input));
            }
        }

        Position wrapAround(AreaSize size) {
            return new Position(((x % size.x) + size.x) % size.x,
                    ((y % size.y) + size.y) % size.y);
        }

        List<Position> neighbours() {
            return List.of(
                    new Position(x - 1, y),
                    new Position(x + 1, y),
                    new Position(x, y - 1),
                    new Position(x, y + 1)
            );
        }
    }

    record Velocity(int x, int y) {

        private static final Pattern REGEX = Pattern.compile("(-?\\d+),(-?\\d+)");

        static Velocity of(String input) {
            var matcher = REGEX.matcher(input);

            if (matcher.find()) {
                var x = matcher.group(1);
                var y = matcher.group(2);

                return new Velocity(Integer.parseInt(x), Integer.parseInt(y));
            } else {
                throw new IllegalArgumentException("No velocity found in '%s'".formatted(input));
            }
        }
    }
}
