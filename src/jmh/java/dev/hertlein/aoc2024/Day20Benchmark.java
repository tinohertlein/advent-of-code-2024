package dev.hertlein.aoc2024;

import dev.hertlein.aoc2024.lib.InputReader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static dev.hertlein.aoc2024.Day20.MinimumNumberOfPicosecondsToSave;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@Measurement(iterations = 3, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
public class Day20Benchmark {

    private Day20 day;
    private List<String> challengeInput;

    @Setup
    public void setup() {
        day = new Day20();
        challengeInput = InputReader.readChallengeInputFor(day);
    }

    @Benchmark
    public void part1(Blackhole bh) {
        bh.consume(day.part1(challengeInput, new MinimumNumberOfPicosecondsToSave(100)));
    }

    @Benchmark
    public void part2(Blackhole bh) {
        bh.consume(day.part2(challengeInput, new MinimumNumberOfPicosecondsToSave(100)));
    }
}
