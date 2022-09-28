# UselessWordleSolver
Spring boot wordle solver

## Requirements
Java - 1.8+
A Java IDE with embedded maven

## How to run the application
Run the Main class WordleApplication.java

## How to get guesses
Hit the url **http://localhost:8081/getGuess1** to get the initial guess  
Hit the url **http://localhost:8081/getNextGuess?pattern1=<pattern>** to get the subsequent guesses  
Note : <pattern> is the sequence of color patterns returned by wordle after a guess is made.  
Enter b when it is grey , y when yellow ,g when green.  
