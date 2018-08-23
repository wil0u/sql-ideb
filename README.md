# Interactive Database Exploration Benchmark (modified version)

Initially, this project is a proof of concept realised by Mahfoud DJEDAINI in the context of his Phd. The original project is located at https://github.com/mdjedaini/ideb. 
It basacally allows the user to create new Interactive Database Exploration systems and compare them to analoguous systems that already exist within the benchmark. This comparison is done thanks to pre-defined or user defined metrics.

The project has been built in the form of a java API that the user can use as he/she wants. 

The original support for the explorations are sequences of OLAP based queries. More precisely, MDX queries and ROLAP queries are abstracted in QueryTriplet with respect to the Golfarelli modeling.
In this modified version, the java API has been changed in order to support explorations in the form of SQL query. 

Therefore the modifications concern only a small part of this quite large project. A detail of the changes is described further in this documentation.

## Getting Started

You have to clone this repository on your local machine and import it inside your favorite java development enviroment. Then you are ready to go. 

### Modifications
Not all classes of the project are represented on this diagram but only important ones for the sql evaluation.
The blue color signifies an internal modification and the green color means a complete creation.
![untitled diagram 1](https://user-images.githubusercontent.com/15943103/44458052-46f04980-a605-11e8-9c9b-9b1482c3f28e.png)

The modification by packages : 

- fr.univ_tours.li.mdjedaini.ideb.olap.query

First of all an adding of an empty constructor has been made in the "Query" abstract class in order to instanciate a query without any parameters. Specially, a SQL query doesn't recquire a cube and the other constructors kind of force the initialization of a query with such parameter. 

Then, in the same package, a "QuerySql" class has been added. It allows to represent an SQL query in the form of a quadruplet : set of projections, selections, tables and aggregate functions. This class inherits charecteristic from the "Query" abstract class.

- fr.univ_tours.li.mdjedaini.ideb.io

A new class has been added to this package : XmlSqlLoader. It takes as input a xml file containing Sql explorations, transforms all this explorations into a session list and all query within these sessions into a quadruplet (cf: SqlQuery). At the end it returns a Log object. This class implements the interface I_LogLoader.


- fr.univ_tours.li.mdjedaini.ideb.struct

The "Log" class has been modified and more precisely its function addSession that converted all query as a "QueryTriplet" in all circumstances. However in the context of Sql query evaluation it is an unwanted effect because the "QueryTriplet" is a special object for Olap queries. To tweak this a check has been added before this conversion : if it is an SQL query then nothing is done because there is no conversion to do (the query is already a quadruplet)


- fr.univ_tours.li.mdjedaini.ideb.eval.scoring

A modification has been made in the function score of the ExplorationScorer class. It's a tweak similar to the one made in the log class. A statement was generating an undesirable effect in the context of SQL evaluation and it was corrected thanks to a check on the type of the query before the execution of the statement.


 
- fr.univ_tours.li.mdjedaini.ideb.eval.metric

8 new metrics has been add to this package : NumberOfAggregationFunctions, NumberOfProjections, NumberOfTables, NumberOfSelections, CommonNumberOfAggregationFunctions, CommonNumberOfProjections, CommonNumberOfSelections, CommonNumberOfTables. Their names are quite explicit. 


- fr.univ_tours.li.mdjedaini.ideb.examples

The class TestExplorationSql has been add to this package. It is a main class that uses all the object needed to perform an SQL query evaluation. It generates a file where each query of each exploration is represented as a list of metrics and specially the eight ones defined above. It is an example on how to use this java API in order to generate features for SQL queries. 

If you want an exhaustif list of the code changes check this : https://github.com/wil0u/sql-ideb/commit/1c166761ea46fc5b677b580528d5eb3d3a76c9f5


### How to use

Create a new main class in the java project. Then, in this main you can start use the built-in object and method that are present in the API. 

For example let's say that my use case is to evaluate SQL exploration thanks to predefined metrics and then generate a file that gather this metrics for each query.

- First of all you need to instanciate an XmlSqlLoader that generate a Log object containing this SQL explorations :

```

File inputFile = new File("C:\\Users\\wilou\\source\\test.xml");
XmlSqlLoader myLoader = new XmlSqlLoader(inputFile);
Log myLog = myLoader.loadLog();

```

- Then instanciate all the metrics that you want for the evaluation :

```

NumberOfProjections metricNumberOfProjections = new NumberOfProjections(be);
NumberOfSelections metricNumberOfSelections = new NumberOfSelections(be);
NumberOfTables metricNumberOfTables = new NumberOfTables(be);
NumberOfAggregationFunctions metricNumberOfAggregationFunctions = new NumberOfAggregationFunctions(be);
CommonNumberOfProjections metricCommonNumberOfProjections = new CommonNumberOfProjections(be);
CommonNumberOfSelections metricCommonNumberOfSelections = new CommonNumberOfSelections(be);
CommonNumberOfAggregationFunctions metricCommonNumberOfAggregationFunctions = new CommonNumberOfAggregationFunctions(be);
CommonNumberOfTables metricCommonNumberOfTables = new CommonNumberOfTables(be);
        

```
- Add them to an object ExplorationScorer :

```
Parameters params   = new Parameters();
BenchmarkEngine be  = new BenchmarkEngine(params);
ExplorationScorer es    = new ExplorationScorer(be);
es.addMetric(metricNumberOfProjections);
es.addMetric(metricNumberOfSelections);
es.addMetric(metricNumberOfTables);
es.addMetric(metricNumberOfAggregationFunctions);
es.addMetric(metricCommonNumberOfProjections);
es.addMetric(metricCommonNumberOfSelections);
es.addMetric(metricCommonNumberOfAggregationFunctions);
es.addMetric(metricCommonNumberOfTables);
```

- Evaluate each query in each exploration and write it in a file :

```
String SAMPLE_CSV_FILE = "sample.csv";
BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));

CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID",metricNumberOfProjections.getName(),metricNumberOfTables.getName(),metricNumberOfAggregationFunctions.getName(),metricNumberOfSelections.getName(),metricCommonNumberOfProjections.getName(),metricCommonNumberOfSelections.getName(),metricCommonNumberOfAggregationFunctions.getName(),metricCommonNumberOfTables.getName()));          
        
int queryId;
double valueProjectionsMetric;
double valueTablesMetric;
double valueAggregationFunctionsMetric;
double valueSelectionsMetric;
double valueCommonProjectionsNumber;
double valueCommonSelectionsNumber;
double valueCommonAggregationFunctionsNumber;
double valueCommonTablesNumber;
QuerySql q;
		
for (Session sess : myLog.getSessionList()) {
   			Exploration e = new Exploration(be,sess);
			ExplorationScore explorationScore = es.score(e);
			for(int k = 0; k < explorationScore.getExploration().getWorkSession().queryList.size(); k++) {
				q = (QuerySql) explorationScore.getExploration().getWorkSession().queryList.get(k);
				queryId = explorationScore.getExploration().getWorkSession().queryList.get(k).getQid();
				valueProjectionsMetric = explorationScore.queryToMetricScoreList.get(k).get(metricNumberOfProjections);
				valueTablesMetric = explorationScore.queryToMetricScoreList.get(k).get(metricNumberOfTables);
				valueAggregationFunctionsMetric = explorationScore.queryToMetricScoreList.get(k).get(metricNumberOfAggregationFunctions);
				valueSelectionsMetric = explorationScore.queryToMetricScoreList.get(k).get(metricNumberOfSelections);
				valueCommonProjectionsNumber = explorationScore.queryToMetricScoreList.get(k).get(metricCommonNumberOfProjections);
				valueCommonSelectionsNumber = explorationScore.queryToMetricScoreList.get(k).get(metricCommonNumberOfSelections);
				valueCommonAggregationFunctionsNumber = explorationScore.queryToMetricScoreList.get(k).get(metricCommonNumberOfAggregationFunctions);
				valueCommonTablesNumber = explorationScore.queryToMetricScoreList.get(k).get(metricCommonNumberOfTables);
				csvPrinter.printRecord(queryId, valueProjectionsMetric,valueTablesMetric,valueAggregationFunctionsMetric,valueSelectionsMetric,valueCommonProjectionsNumber,valueCommonSelectionsNumber,valueCommonAggregationFunctionsNumber,valueCommonTablesNumber);	            
			}
		}
		csvPrinter.flush();
```

## Authors

* **VERDEAUX Willeme** - *Initial work* - [ideb](https://github.com/mdjedaini/ideb) made by Mahfoud DJEDAINI
