Knight Moves
===

To Compile
---

There is a pom.xml file included, with a fairly standard maven project layout. Otherwise, all the source files are in src/main/java. There are no dependencies other than the standard java API.  

To Run
---

The main class is com.awmcdaniel.knightmoves.KnightMoves. As stated in the problem description, the first command line argument is expected to be an integer n.
It will then compute the number of possible valid paths for the board of length n. There is some very basic error checking but nothing more elaborate than that. 

Demo
---

There is a demo class, com.awmcdaniel.knightmoves.KnightMovesDemo. This class has a main() method that ignores all parameters, and simply runs a quick demo/diagnostic of the code for testing. Specifically, it

- Prints out the game 'board' in the form LETTER/VALID_MOVES/IS_VOWEL
- Runs a single threaded 'brute force' search for each value of n from 1 - 16 (slow, bad implementation)
- Runs a single threaded 'cached computation' search for each value of n from 1 - 32
- Runs a multi-threaded 'cached computation' search for each value of n from 1 - 32
- Prints out the cached computation table (several thousand lines).

 