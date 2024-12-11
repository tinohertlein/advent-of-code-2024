package dev.hertlein.aoc2024;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

class Day11 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return Stones.of(inputLines).countStonesAfterBlinks(25);
    }

    @Override
    public Object part2(List<String> inputLines, Void v) {
        return Stones.of(inputLines).countStonesAfterBlinks(75);
    }

    @RequiredArgsConstructor
    static class Stones {

        private final LoadingCache<BlinksAndStone, StonesCount> loadingCache =
                CacheBuilder.newBuilder().build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public StonesCount load(@NonNull BlinksAndStone blinksAndStone) {
                        return blink(blinksAndStone.blinks, blinksAndStone.stone);
                    }
                });

        private final List<Stone> values;

        static Stones of(List<String> inputLines) {
            return new Stones(Arrays.stream(inputLines.getFirst().split(" "))
                    .map(Stone::of)
                    .toList());
        }

        long countStonesAfterBlinks(int blinks) {
            return values.stream()
                    .map(stone -> blink(new Blinks(blinks), stone))
                    .mapToLong(StonesCount::value)
                    .sum();
        }

        @SneakyThrows
        private StonesCount blinkUsingCache(Blinks blinks, Stone stone) {
            return loadingCache.get(new BlinksAndStone(blinks, stone));
        }

        private StonesCount blink(Blinks blinks, Stone stone) {
            if (blinks.noneLeft()) {
                return new StonesCount(1);
            }

            if (stone.shouldIncrement()) {
                return blinkUsingCache(blinks.decrement(), stone.increment());
            } else if (stone.shouldSplit()) {
                var splitStones = stone.split();
                return blinkUsingCache(blinks.decrement(), splitStones.getLeft())
                        .plus(blinkUsingCache(blinks.decrement(), splitStones.getRight()));
            } else {
                return blinkUsingCache(blinks.decrement(), stone.times2024());
            }
        }

        record Stone(long number) {

            static Stone of(String number) {
                return new Stone(Long.parseLong(number));
            }

            boolean shouldIncrement() {
                return number == 0;
            }

            boolean shouldSplit() {
                return String.valueOf(number).length() % 2 == 0;
            }

            Stone increment() {
                return new Stone(number + 1);
            }

            Pair<Stone, Stone> split() {
                var stoneAsString = String.valueOf(number);
                var middle = stoneAsString.length() / 2;
                return Pair.of(
                        Stone.of(stoneAsString.substring(0, middle)),
                        Stone.of(stoneAsString.substring(middle)));
            }

            Stone times2024() {
                return new Stone(number * 2024);
            }
        }

        record Blinks(int value) {

            boolean noneLeft() {
                return value == 0;
            }

            Blinks decrement() {
                return new Blinks(value - 1);
            }
        }

        record BlinksAndStone(Blinks blinks, Stone stone) {
        }

        record StonesCount(long value) {

            StonesCount plus(StonesCount other) {
                return new StonesCount(value + other.value);
            }
        }
    }
}