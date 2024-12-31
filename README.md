# 🎄🎄🎄Advent of Code 2024 - Java 🎄🎄🎄

![](https://github.com/tinohertlein/advent-of-code-2024/actions/workflows/tests.yml/badge.svg)
![Maintainability](https://api.codeclimate.com/v1/badges/e8f5294ee66ff9e7efa5/maintainability)
![Test Coverage](https://api.codeclimate.com/v1/badges/e8f5294ee66ff9e7efa5/test_coverage)
![](https://img.shields.io/badge/days%20completed-25-red)
![](https://img.shields.io/badge/stars%20⭐-50-blue)

Welcome to the Advent of Code[^aoc] Java project created by [tinohertlein](https://github.com/tinohertlein).

In this repository, Tino is about to provide solutions for the puzzles using Java language.

## What are Tino's rules for solving the puzzles - in order of importance?

1. Prefer functional/declarative programming style to imperative programming style
2. Prefer readability to performance
3. Don't care about memory usage or execution duration

## What do I need to run this stuff?

- Java 23 - preview is enabled in `build.gradle`
- Gradle 8+ (but Gradle wrapper is included)

## How to use this repo?

### Build

- `gradle build`

### Test

- `gradle test`

### Benchmark

- Though focus is more on readability than performance of the solutions, there are also [jmh] benchmark files
  in [./src/jmh](./src/jmh/java/dev/hertlein/aoc2024) that can be used to compare different solution approaches.
- `gradle jmh`

### Setup files for the next puzzle day

- `gradle setupNextDay`
- Template files starting with `Day00` are used as copy source for that.

### Download challenge input for the current puzzle day

- Create a file `.aocconfig` with your [advent-of-code session cookie](https://letmegooglethat.com/?q=advent+of+code+session+cookie).
- `gradle downloadChallengeInput`

## Anything else?

[^aoc]:
[Advent of Code][aoc] – An annual event of Christmas-oriented programming challenges started December 2015.
Every year since then, beginning on the first day of December, a programming puzzle is published every day for
twenty-five days.
You can solve the puzzle and provide an answer using the language of your choice.

[aoc]: https://adventofcode.com

[jmh]: https://github.com/openjdk/jmh