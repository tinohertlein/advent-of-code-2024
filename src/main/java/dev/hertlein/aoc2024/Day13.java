package dev.hertlein.aoc2024;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Gatherers;

import static dev.hertlein.aoc2024.Day13.ButtonType.A;
import static dev.hertlein.aoc2024.Day13.ButtonType.B;
import static java.util.Collections.emptyList;
import static java.util.function.Predicate.not;

class Day13 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return fewestTokensToSpendForPrizes(inputLines, 0L);
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return fewestTokensToSpendForPrizes(inputLines, 10_000_000_000_000L);
    }

    private long fewestTokensToSpendForPrizes(List<String> inputLines, long prizeAdjustment) {
        return inputLines.stream()
                .filter(not(String::isEmpty))
                .gather(Gatherers.windowFixed(3))
                .map(machineInput -> ClawMachine.of(machineInput, prizeAdjustment))
                .map(ClawMachine::tryWinPrize)
                .filter(not(PushesOfButtons::none))
                .mapToLong(PushesOfButtons::tokens)
                .sum();
    }

    record ClawMachine(Prize prize, Button buttonA, Button buttonB) {

        static ClawMachine of(List<String> inputLines, long prizeAdjustment) {
            return new ClawMachine(
                    Prize.of(inputLines.get(2), prizeAdjustment),
                    Button.of(inputLines.get(0), A),
                    Button.of(inputLines.get(1), B)
            );
        }

        PushesOfButtons tryWinPrize() {
            // Apply https://en.wikipedia.org/wiki/Cramer%27s_rule

            var determinantDivisor = buttonA.x * buttonB.y - buttonA.y * buttonB.x;
            var determinantA = prize.x * buttonB.y - prize.y * buttonB.x;
            var determinantB = buttonA.x * prize.y - buttonA.y * prize.x;

            if (determinantDivisor == 0.0)
                return PushesOfButtons.NONE;

            var pushes = PushesOfButtons.of(
                    new PushesOfButton(buttonA, (determinantA / determinantDivisor)),
                    new PushesOfButton(buttonB, (determinantB / determinantDivisor)));

            if (prize.isWonWith(pushes)) {
                return pushes;
            } else {
                return PushesOfButtons.NONE;
            }
        }
    }

    record Button(ButtonType type, int x, int y) {

        private final static Pattern REGEX = Pattern.compile("X\\+(\\d+),\\sY\\+(\\d+)");

        static Button of(String inputLine, ButtonType type) {
            var matcher = REGEX.matcher(inputLine);
            if (matcher.find()) {
                var x = matcher.group(1);
                var y = matcher.group(2);
                return new Button(type, Integer.parseInt(x), Integer.parseInt(y));
            } else {
                throw new IllegalArgumentException("No button found in '%s'".formatted(inputLine));
            }
        }
    }

    record Prize(long x, long y) {

        private final static Pattern REGEX = Pattern.compile("X=(\\d+),\\sY=(\\d+)");

        static Prize of(String inputLine, long prizeAdjustment) {
            var matcher = REGEX.matcher(inputLine);
            if (matcher.find()) {
                var x = matcher.group(1);
                var y = matcher.group(2);
                return new Prize(prizeAdjustment + Integer.parseInt(x), prizeAdjustment + Integer.parseInt(y));
            } else {
                throw new IllegalArgumentException("No prize found in '%s'".formatted(inputLine));
            }
        }

        boolean isWonWith(PushesOfButtons pushes) {
            return pushes.x() == x && pushes.y() == y;
        }
    }

    record PushesOfButtons(List<PushesOfButton> values) {

        static PushesOfButtons NONE = new PushesOfButtons(emptyList());

        static PushesOfButtons of(PushesOfButton... pushes) {
            return new PushesOfButtons(Arrays.asList(pushes));
        }

        boolean none() {
            return this == NONE;
        }

        long tokens() {
            return sumOf(PushesOfButton::tokens);
        }

        long x() {
            return sumOf(PushesOfButton::x);
        }

        long y() {
            return sumOf(PushesOfButton::y);
        }

        private long sumOf(Function<PushesOfButton, Long> function) {
            return values.stream().mapToLong(function::apply).sum();
        }
    }

    record PushesOfButton(Button button, long pushes) {

        long tokens() {
            return button.type.tokens * pushes;
        }

        long x() {
            return button.x * pushes;
        }

        long y() {
            return button.y * pushes;
        }
    }

    @RequiredArgsConstructor
    enum ButtonType {
        A(3), B(1);

        final int tokens;
    }
}