# Assignment 4. Patient Zero

Student ID: 253122
Name: Madi Shalabay
Group: SE-2510

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 80
    Shared core: 1205
    Marker hits: 6
    First hit: 710
    Border length: 3
    Shared words: 565
    Tests needed: 45
    Cheapest testing: 202
    Longest chain: 39

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?

The cell d[i][j] represents the minimum number of single-letter operations (insertion, deletion, or replacement) needed to transform the first i letters of strain A into the first j letters of strain B. It is computed by taking the minimum of three values: d[i-1][j-1] + (0 if A[i-1] == B[j-1], else 1) for a match or replacement, d[i-1][j] + 1 for a deletion from A, and d[i][j-1] + 1 for an insertion into A.

Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?

A longest common substring requires matching characters to appear contiguously without gaps, whereas a longest common subsequence allows gaps between matching letters as long as their relative order is preserved. My Shared core (1205) is much larger than a direct index-by-index comparison because insertions and deletions shift identical segments to different positions, and LCS realigns these shifted letters across gaps.

Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?

The prefix function pi[i] gives the length of the longest proper prefix of the pattern substring p[0..i] that is also a suffix of p[0..i]. In KMP, the text pointer never moves backward because upon a mismatch after k matched letters, pi[k-1] tells us exactly how many characters of the pattern already match the suffix of the text read so far. My marker's Border length is 3, which means the first 3 letters of my marker are identical to its last 3 letters.

Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?

Because many different strings are mapped into a finite modulus range, two distinct words can produce the exact same hash value (a collision), so we must compare the actual characters to eliminate false alarms. If many windows share the same hash, the algorithm repeatedly performs full character-by-character checks of length k, which degrades the running time from O(n + m) on average to O(n * m * k) in the worst case.

Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?

The two states for each person v are take[v] (minimum subtree cost when v is tested) and skip[v] (minimum subtree cost when v is not tested). If v is not tested, all of its children must be tested (skip[v] = sum of take[c]); if v is tested, each child can independently choose its cheaper state (take[v] = cost[v] + sum of min(take[c], skip[c])). A greedy strategy fails because choosing the person with the most links ignores individual testing prices and can force us to test expensive nodes when a combination of cheaper neighbours would cover the same links for less cost.

## AI use

The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.

ChatGPT: used to review the rolling hash modulo arithmetic in Rabin-Karp and the top-down reconstruction logic for the tree DP in Mission 5.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | 98.8 ms / 171.0 ms / 355.9 ms |
| KMP | 20 000 / 40 000 / 80 000 | 4.0 ms / 2.2 ms / 0.6 ms |
| edit distance | 500 / 1000 / 2000 / 4000 | 9.2 ms / 23.5 ms / 30.5 ms / 136.0 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.

When the text size doubles, the naive search time roughly doubles (growing by a factor of ~2x) because for a fixed pattern length m = 2000 it performs ~n*m comparisons, whereas KMP runs in linear O(n + m) time (taking under 4 ms and even speeding up after JVM warm-up). For edit distance, doubling the strain length from 2000 to 4000 quadruples the time (growing by a factor of ~4x, from 30.5 ms to 136.0 ms), which matches its quadratic O(n * m) complexity.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(n * m) | O(n * m) | Full 2D DP table of size (n + 1) x (m + 1) |
| LCS | O(n * m) | O(n * m) | Full 2D DP table of size (n + 1) x (m + 1) |
| Prefix function and KMP | O(n + m) | O(m) | Prefix array of size m; amortized O(1) transitions per character |
| Rabin-Karp (shared words) | O(n + m) avg, O(n * m * k) worst | O(m) | Rolling hash update in O(1) with prime modulus and low collision rate |
| Tree DP | O(v) | O(v) | Tree has v - 1 edges; constant number of operations per node |

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.