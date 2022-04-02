# CodewarsJavaKata
Submissions to the Java Kata on Codewars.

## Longest Slide Down A Pyramid
I am particularly proud of my version of [LongestSlideDown](/src/main/java/LongestSlideDown.java)
as it had to be optimal for very high pyramids!

## Befunge Interpreter
I had fun with the [Befunge Interpreter](/src/main/java/BefungeInterpreter.java).  
It was a wierd experience to get my head around, but fun to code!

## Binomial Expansion
I had fun with the [Binomial Expansion](/src/main/java/KataSolution.java).  
Most difficult was doing the vector multiplication and resolution, 
but the second was getting the output format right !!! 

## Simple Assembler?
These one is multipart.  It starts [simple](https://www.codewars.com/kata/simple-assembler-interpreter/),
but there's a [second part](https://www.codewars.com/kata/assembler-interpreter-part-ii/),
which sounds more challenging!

### Part 1: Simple Assembler
So we start with the [Simple Assembler](/src/main/java/SimpleAssembler.java).
To discover where I was going wrong, I added some debug (now commented out)
to "spy" on the hidden tests that were failing and so create extra tests
to cover them.  This is the [final test suite](/src/test/java/SimpleAssemblerTest.java) I used.

### Part 2: Assembler Interpreter
So we will complete the [Assembler Interpreter](/src/main/java/AssemblerInterpreter.java).
And test it with the [test suite](/src/test/java/AssemblerInterpreterTest.java)

## Closest pair of points in linearithmic time
For my next trick: [Closest pair of points in linearithmic time](https://www.codewars.com/kata/5376b901424ed4f8c20002b7).  
This will get me into the dark realm of computer science and big O notation.  See the Wikipedia entry for the 
[Closest pair of points problem](https://en.wikipedia.org/wiki/Closest_pair_of_points_problem)!
Solution (if I find it) will be [Closest Pair], along with their provided class of [Point](src/main/java/Point.java), 
and my version of [their test suite](src/test/java/ClosestPairTest.java).

## Symbolic differentiation of prefix expressions
This one looks fascinating and challenging to say the least.  
[Symbolic differentiation of prefix expressions](https://www.codewars.com/kata/584daf7215ac503d5a0001ae) requires both
the differentiation of expressions, but also the rationalisation of expressions before and after differentiation. 
The [PrefixDiff class](/src/main/java/PrefixDiff.java) will do the work, but for now just a shell with documentation.