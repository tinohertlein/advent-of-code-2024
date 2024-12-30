package dev.hertlein.aoc2024;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

class Day24 implements Day<Void> {

    @Override
    public Object part1(List<String> inputLines, Void v) {
        return new Device(inputLines).output();
    }

    @Override
    public String part2(List<String> inputLines, Void v) {
        // Copy & paste the output to <a href="https://dreampuf.github.io/GraphvizOnline/">...</a>
        // to spot anomalies in the graph and fix them manually.
        new Device(inputLines).printGraph();

        // to be swapped:

        // z13 <-> vcv
        // z25 <-> mps
        // z19 <-> vwp
        // cqm <-> vjv
        return Stream.of("z13", "vcv", "z25", "mps", "z19", "vwp", "cqm", "vjv").sorted().collect(joining(","));
    }

    @RequiredArgsConstructor
    static class Device {

        private final Map<String, Boolean> wireValues;
        private final List<Gate> gates;

        Device(List<String> inputLines) {
            this.wireValues = inputLines.stream()
                    .takeWhile(not(String::isEmpty))
                    .map(line -> line.split(": "))
                    .collect(toMap(split -> split[0], split -> split[1].equals("1")));
            this.gates = inputLines.stream()
                    .dropWhile(not(String::isEmpty))
                    .dropWhile(String::isEmpty)
                    .map(Gate::new)
                    .toList();
        }

        Long output() {
            var queue = new ArrayList<>(gates);

            while (!queue.isEmpty()) {
                var gate = queue.removeFirst();
                boolean wasExecuted = gate.execute();
                if (!wasExecuted) {
                    queue.add(gate);
                }
            }

            var values = wireValues.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("z"))
                    .sorted((entry1, entry2) -> entry2.getKey().compareTo(entry1.getKey()))
                    .map(Map.Entry::getValue)
                    .map(value -> value ? "1" : "0")
                    .collect(joining(""));

            return parseLong(values, 2);
        }

        void printGraph() {
            var z = gates.stream().filter(gate -> gate.output.startsWith("z")).map(Gate::getOutput).sorted().collect(joining("->"));
            var x = z.replace('z', 'x');
            var y = z.replace('z', 'y');

            var and = gates.stream().filter(gate -> gate.operation == Operation.AND).map(Gate::getOutput).collect(joining(" "));
            var or = gates.stream().filter(gate -> gate.operation == Operation.OR).map(Gate::getOutput).collect(joining(" "));
            var xor = gates.stream().filter(gate -> gate.operation == Operation.XOR).map(Gate::getOutput).collect(joining(" "));

            var subGraphZ = """
                     subgraph {
                           node [style=filled,color=green]
                            %s
                        }
                    """.formatted(z);
            var subGraphX = """
                     subgraph {
                           node [style=filled,color=gray]
                            %s
                        }
                    """.formatted(x);
            var subGraphY = """
                     subgraph {
                           node [style=filled,color=gray]
                            %s
                        }
                    """.formatted(y);
            var subGraphAND = """
                      subgraph {
                            node [style=filled,color=red]
                            %s
                        }
                    """.formatted(and);
            var subGraphOR = """
                      subgraph {
                            node [style=filled,color=yellow]
                            %s
                        }
                    """.formatted(or);
            var subGraphXOR = """
                      subgraph {
                            node [style=filled,color=blue]
                            %s
                        }
                    """.formatted(xor);


            var inOut = gates.stream().map(gate -> gate.input1 + " -> " + gate.output + "\r\n" + gate.input2 + " -> " + gate.output).collect(joining("\r\n"));

            StringBuilder sb = new StringBuilder();
            sb.append("digraph G {");
            sb.append(subGraphZ);
            sb.append(subGraphX);
            sb.append(subGraphY);
            sb.append(subGraphAND);
            sb.append(subGraphOR);
            sb.append(subGraphXOR);
            sb.append(inOut);
            sb.append("}");

            System.out.println(sb);
        }

        @RequiredArgsConstructor
        @Data
        class Gate {
            private final String input1;
            private final String input2;
            private final Operation operation;
            private final String output;

            private static final Pattern REGEX = Pattern.compile("(\\w{3}) (AND|XOR|OR) (\\w{3}) -> (\\w{3})");

            Gate(String inputLine) {
                var matcher = REGEX.matcher(inputLine);
                if (matcher.find()) {
                    this.input1 = matcher.group(1);
                    this.operation = Operation.valueOf(matcher.group(2));
                    this.input2 = matcher.group(3);
                    this.output = matcher.group(4);
                } else {
                    throw new IllegalArgumentException("No gate found in '%s'" + inputLine);
                }
            }

            boolean execute() {
                Boolean input1 = wireValues.get(this.input1);
                Boolean input2 = wireValues.get(this.input2);

                if (Stream.of(input1, input2).anyMatch(Objects::isNull)) {
                    return false;
                }

                wireValues.put(output, operation.execute(input1, input2));
                return true;
            }

            @Override
            public String toString() {
                return input1 + " " + operation + " " + input2 + " -> " + output;
            }
        }
    }

    enum Operation {
        AND {
            @Override
            Boolean execute(Boolean a, Boolean b) {
                return a && b;
            }
        }, XOR {
            @Override
            Boolean execute(Boolean a, Boolean b) {
                return a ^ b;
            }
        }, OR {
            @Override
            Boolean execute(Boolean a, Boolean b) {
                return a || b;
            }
        };

        abstract Boolean execute(Boolean a, Boolean b);
    }
}
