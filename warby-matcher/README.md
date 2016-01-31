Warby Matcher
====


Compiling
----

The program is written in java, and a pom file is included. Aside from the core
java API, the only external dependency is Junit 4.10, only for compiling and 
running the unit tests. This is described in the pom file so everything should 
work fine as long as you have maven configured correctly. 

To compile, run ```mvn clean package``` in the project directory. This will 
clean any existing binaries, compile all the source files, compile and run unit
tests, then finally create a jar file, warby-matcher-1.0.0-SNAPSHOT.jar 

Running
----

The main class is ```com.awmcdaniel.warbyparker.WPMatcher```. 

The program expects no arguments, simply reading data from stdin as specified, 
and outputting to stdout. One note: If the input data is malformed or not 
otherwise conforming to what the assignment specification described, it may 
print an error message to stderr. Since the specification requested to "Don't 
print extraneous or superfluous stuff to stdout" this is the only accommodation 
I have made to error handling.

To run, simply include the compiled jar file in your classpath. For example:

```cat src/main/resources/data1 | java -cp target/warby-matcher-1.0.0-SNAPSHOT.jar com.awmcdaniel.warbyparker.WPMatcher > target/output_file```

I have included some text files with sample data in ```src/main/resources/```

Algorithm Discussion
----

The class WPPatterTrie implements a variation 'trie', a special type of search 
tree that allows for very fast searching of the pattern space against queries of matching paths.
Patterns are implemented in the class WPPattern, which stores a tokenized 
version of each pattern as a String array, along with some code to implement
comparisons in order to evaluate the "best" match as described in the 
specification. 

When a WPPatternTrie is instantiated, it has only an empty root node and will
match only a null, zero token path. Patterns are inserted into the trie using 
the ```insertPattern``` method. This method recursively transverses the pattern
tokens, creating or retrieving a child node for each token. Special handling is
used for the "*" string, where a "wild child" node is created/retrieved to represent
a token that can match any corresponding token of a query path. 

Paths are matched using the ```search``` and ```searchForBest``` methods. This 
method first trims and strips any leading or trailing slashes ("/"), then 
tokenizes the path string and begins recursively searching the trie for matching
patterns. For each recursion, it checks if (a) the current node has a matching child 
node for the current token, and if (b) the current node has a wildcard child node. 
For each acceptable child node, it then recurses on that node for the next token in the
query sequence. 

The recursion terminates when (a) there are no acceptable child nodes, or (b) the last 
token in the query sequence has been reached. In the case of (a), a dead end has been 
found in the search, and a ```null``` value is returned. In the case of (b), if a 
pattern object was stored at this node, a match has been found and it is returned, 
otherwise a ```null``` object is returned.    

As the recursion unwinds, there are 0 - 2 values that can be returned from each "subtree"
of the recursion (the subtree matching a token, and the wildcard subtree). For 0 results, 
the search was a dead end and ```null``` result is continued to be returned upward. For
1 result, that result is returned upward. For 2 results, A comparison is made (WPPattern 
implements the ```Comparable``` interface) to find which WPPattern object is "less", meaning 
better in this implementation. The better pattern is returned upward. When the recursion has
finished unwinding, the best match will be the WPPattern instance returned.

The complexity of inserting a pattern into the search trie is O(t) operations, where t is the
number of tokens in the pattern. The complexity for building the whole trie is O(t*n), where n is the 
number of patterns to be processed and t is the average number of tokens per pattern. It is not 
unreasonable to assume that for some very large pattern sets, n >> t, that is the
number of patterns could be in millions of records, but the average number of 
tokens per pattern is considerably smaller (say 10-20 tokens). In this case, we can say 
that the complexity of building the whole tree is O(n). 

For searching, the worst case complexity is a tree where every single node has both a match 
for that token, and a wildcard node. This then requires that an exhaustive search is made, 
essentially returning every single pattern as a match. Finding a single pattern match is 
on O(t) operations, where t is the number of tokens in a query. For a query of 
t tokens, there are 2^t possible patterns that can match it. An example of the worst case
pattern set would be something like:

- a,b,c,d
- a,b,c,*
- a,b,*,d
- a,b,*,*
- a,*,c,d
... etc ...
- *,*,c,*
- *,*,*,d
- *,*,*,*

A single query of a/b/c/d would match 2^4 = 16 patterns. The total number of operations 
therefore is equal to  2^(t+1)-1, in this case, 2^(4+1)-1 = 31. Therefore, the worst case
complexity of a query is O(2^t) to find all matching patterns.

Conversely, in pattern sets with sparser wildcards (and therefore matches), the query time is 
considerably faster. The best case scenario is a set where no patterns have wild cards, 
in which case the lookup time is O(t). Pattern sets in-between those two extremes depend 
on both the number of wild cards, and how heavily weighted to the left those wildcards 
occur. That is to say, branching that occurs early in the search due to wildcards is 
considerably more costly. 

The good news is that as with building the tree, if the the number of tokens in a path is bounded,
then the complexity of processing all queries scales linearly only with the number of queries.
For example, lets consider only queries than can have exactly 8, single, lower case letters, a-z. 
(for example: a/b/c/d/d/c/b/a/ or f/f/f/f/f/f/g/g) The total number possible queries 
is 26^8 = ~208 billion, and the total number of possible patterns is 27^8 = ~282 billion. 
However, each individual query can take at worst 2^(9) - 1 = 511 operations, because
despite a huge number of nodes and patterns, the number of potential paths on the search
trie for each query is relatively small. If the  total number of paths is very large, 
say 100 million or more, then for n paths, the total complexity is O(n + 511) --> O(n). 

 
