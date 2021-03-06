package Solutions.EighteentDezember;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class EighteentDezemberSolution {
    int partOne;
    int partTwo;
    public void Solution(){
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get("rsc/input18Dezember.txt")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        solve(lines);
        System.out.println("The Solution is/ Part 1: " + partOne + " Part 2: " + partTwo);


    }


        public void solve(List<String> input) {
            Deque<SnailNumber> sums = new ArrayDeque<>();
            List<SnailNumber> combinations = new ArrayList<>();

            for (String line : input) {
                SnailNumber sn = parse(line);
                sums.addLast(sn);
                combinations.add(sn);
            }
            SnailNumber sum = sums.pop();
            while (!sums.isEmpty()) {
                sum = sum.add(sums.pop());
            }
//            System.out.println("Part 1: " + sum.magnitude());
            partOne = sum.magnitude();

            int maxm = -1;
            for (SnailNumber n1 : combinations) {
                for (SnailNumber n2 : combinations) {
                    if (n1.equals(n2)) continue;
                    maxm = Math.max(maxm, n1.add(n2).magnitude());
                    maxm = Math.max(maxm, n2.add(n1).magnitude());
                }
            }
//            System.out.println("Part 2: " + maxm);
            partTwo = maxm;
        }

        static class SnailNumber {
            private SnailNumber left;
            private SnailNumber right;
            private Integer value;
            private SnailNumber parent;

            public SnailNumber(SnailNumber left, SnailNumber right) {
                this.left = left;
                this.right = right;
                left.parent = this;
                right.parent = this;
            }

            public SnailNumber(Integer value) {
                this.value = value;
            }

            public SnailNumber add(SnailNumber toAdd) {
                SnailNumber sn = new SnailNumber(this.clone(), toAdd.clone());
                sn.reduce();
                return sn;
            }

            public int magnitude() {
                if (isDigit()) return value;
                return 3 * left.magnitude() + 2 * right.magnitude();
            }

            protected SnailNumber clone() {
                if (isDigit()) return new SnailNumber(value);
                return new SnailNumber(left.clone(), right.clone());
            }

            private void reduce() {
                while (explode() || split()) ;
            }

            private boolean isDigit() {
                return value != null;
            }

            private int level() {
                if (parent == null) return 0;
                return parent.level() + 1;
            }

            private SnailNumber origin() {
                SnailNumber current = this;
                while (current.parent != null) current = current.parent;
                return current;
            }

            private List<SnailNumber> allDigits() {
                if (isDigit()) return List.of(this);
                List<SnailNumber> all = new ArrayList<>();
                all.addAll(left.allDigits());
                all.addAll(right.allDigits());
                return all;
            }

            private Optional<SnailNumber> protect(List<SnailNumber> list, int i) {
                return (i < 0 || i >= list.size()) ? Optional.empty() : Optional.of(list.get(i));
            }

            private Optional<SnailNumber> nearestDigit(SnailNumber number, int delta) {
                List<SnailNumber> digits = origin().allDigits();
                return protect(digits, digits.indexOf(number) + delta);
            }

            private boolean explode() {
                if (!isDigit()) {
                    if (level() == 4) {
                        nearestDigit(left, -1).ifPresent(sn -> sn.value += left.value);
                        nearestDigit(right, 1).ifPresent(sn -> sn.value += right.value);
                        left = null;
                        right = null;
                        value = 0;
                        return true;
                    } else {
                        return left.explode() || right.explode();
                    }
                }
                return false;
            }

            private boolean split() {
                if (isDigit()) {
                    if (value >= 10) {
                        left = new SnailNumber(value / 2);
                        right = new SnailNumber(value / 2 + value % 2);
                        left.parent = this;
                        right.parent = this;
                        value = null;
                        return true;
                    }
                } else {
                    return left.split() || right.split();
                }
                return false;
            }

            @Override
            public String toString() {
                if (isDigit()) return value.toString();
                return "[" + left + "," + right + "]";
            }
        }

        private SnailNumber parse(String s) {
            if (s.length() == 1) {
                return new SnailNumber(Integer.parseInt(s));
            }
            int level = 0;
            int splitAt = -1;
            for (int i = 0; i < s.length() && splitAt == -1; i++) {
                switch (s.charAt(i)) {
                    case '[' -> level++;
                    case ']' -> level--;
                    case ',' -> {
                        if (level == 1) {
                            splitAt = i;
                        }
                    }
                }
            }
            return new SnailNumber(parse(s.substring(1, splitAt)), parse(s.substring(splitAt + 1, s.length() - 1)));
        }


    }
