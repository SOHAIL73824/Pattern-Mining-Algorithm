# Pattern-Mining-Algorithm
In this project I have implemented a pattern mining algorithm based on the Apriori Algorithm .

"src" Folder contain two class files 

"ProjectOne.java" is the main program where all algorithm is programmed. It implements the A-priori Algorithm.Once input file is loaded it creates 

"Itemset.java" is the class file for creating Itemset Object to store an item set value and its respective support.


How to run Project :

ProjectOne.java has the main class so we have to compile and then run this file to get frequent Itemset output. This file receives the dataset file path, support, K value and output file path from the
command line and it takes in the order as follows :

if "args[]" is the input array of command arguments then

args[0]=support(Example: args[0]=8);
args[1]=K value(Example: args[0]=2);
args[2]= Data File path (Example: args[0]="E:\\Data Mining\\Project\\transactionDB.txt");
args[3]=Output File path (Example: args[0]="E:\\Data Mining\\Project\\outputDB.txt");
