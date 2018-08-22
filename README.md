# Interactive Database Exploration Benchmark (modified version)

Initially, this project is a proof of concept realised by Mahfoud DJEDAINI in the context of his Phd. The original project is located at https://github.com/mdjedaini/ideb. 
It basacally allows the user to create new Interactive Database Exploration systems and compare them to analoguous systems that already exist within the benchmark. This comparison is done thanks to pre-defined or user defined metrics.
The project has been built in the form of a java API that the user can use as he/she wants. 
On the version of this project, the java API has been modified in order to take into account the SQL query evaluation. In other words, the modifications concern only a small part of this quite large project. A detail of the changes is described further in this documentation.

## Getting Started

You have to clone this repository on your local machine and import it inside your favorite java development enviroment. Then you are ready to go. 

### Modifications
Not all classes of the project are represented on this diagram but only important ones for the sql evaluation 
The blue color signifies an internal modification and the green color means a complete creation.
![untitled diagram 1](https://user-images.githubusercontent.com/15943103/44458052-46f04980-a605-11e8-9c9b-9b1482c3f28e.png)

- fr.univ_tours.li.mdjedaini.ideb.olap.query

First of all an adding of an empty constructor has been made in the "Query" abstract class in order to instanciate a query without any parameters. Specially, a SQL query doesn't recquire a cube and the other constructors kind of force the initialization of a query with such parameter. 

Then, in the same package, a "QuerySql" class has been added. It allows to represent an SQL query in the form of a quadruplet : set of projections, selections, tables and aggregate functions. This class inherits charecteristic from the "Query" abstract class.

- fr.univ_tours.li.mdjedaini.ideb.io
A new class has been added to this package : XmlSqlLoader. It takes as input a xml file containing Sql explorations, transforms all this explorations into a session and all query within these exploration into a quadruplet (cf: SqlQuery). At the end it returns a Log object following the same logic than the other loaders that can be found in this package. This class implements the interface I_LogLoader.


- fr.univ_tours.li.mdjedaini.ideb.struct

The "Log" class has been modified and more precisely its function addSession that converted all query as a "QueryTriplet" in all circumstances. However in the context of Sql query evaluation it is an unwanted effect because the "QueryTriplet" is a special object for Olap queries. To tweak this a check has been added before this conversion : if it is an SQL query then nothing is done because there is no conversion to do (the query is already a quadruplet)


- fr.univ_tours.li.mdjedaini.ideb.eval.scoring

A modification has been made in the function score of the ExplorationScorer class. It's a tweak similar to the one made in the log class. A statement was generating an undesirable effect in the context of SQL evaluation and it was corrected thanks to a check on the type of the query before the execution of the statement.


 
- fr.univ_tours.li.mdjedaini.ideb.eval.metric

8 new metrics has been add to this package : NumberOfAggregationFunctions, NumberOfProjections, NumberOfTables, NumberOfSelections, CommonNumberOfAggregationFunctions, CommonNumberOfProjections, CommonNumberOfSelections, CommonNumberOfTables. Their names are quite explicit. 


- fr.univ_tours.li.mdjedaini.ideb.examples
The class TestExplorationSql has been add to this package. It is a main class that uses all the object needed to perform an SQL query evaluation. It generates a file where each query of each exploration is represented as a list of metrics and specially the eight ones defined above. It is an example on how to use this java API in order to generate features for SQL queries. 

If you want an exhaustif list of the code changes check this : https://github.com/wil0u/sql-ideb/commit/1c166761ea46fc5b677b580528d5eb3d3a76c9f5



## Authors

* **VERDEAUX Willeme** - *Initial work* - [ideb](https://github.com/mdjedaini/ideb) made by Mahfoud DJEDAINI
