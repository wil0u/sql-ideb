package fr.univ_tours.li.mdjedaini.ideb.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.univ_tours.li.mdjedaini.ideb.BenchmarkEngine;
import fr.univ_tours.li.mdjedaini.ideb.algo.DistanceBasedFocusDetection;
import fr.univ_tours.li.mdjedaini.ideb.eval.Exploration;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.CommonNumberOfAggregationFunctions;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.CommonNumberOfProjections;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.CommonNumberOfSelections;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.CommonNumberOfTables;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.NumberOfAggregationFunctions;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.NumberOfProjections;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.NumberOfSelections;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.NumberOfTables;
import fr.univ_tours.li.mdjedaini.ideb.eval.scoring.ExplorationScore;
import fr.univ_tours.li.mdjedaini.ideb.eval.scoring.ExplorationScorer;
import fr.univ_tours.li.mdjedaini.ideb.io.XmlSqlLoader;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.Query;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.QuerySql;
import fr.univ_tours.li.mdjedaini.ideb.params.Parameters;
import fr.univ_tours.li.mdjedaini.ideb.struct.Log;
import fr.univ_tours.li.mdjedaini.ideb.struct.Session;

public class TestExplorationSql {
	public static void main(String[] args) throws IOException{
		ArrayList<Session> sessionList = new ArrayList<Session>();
		XmlSqlLoader myLoader = new XmlSqlLoader();
		Log myLog = myLoader.loadLog();
		Parameters params   = new Parameters();
        
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
		
        BenchmarkEngine be  = new BenchmarkEngine(params);
        String SAMPLE_CSV_FILE = "sample.csv";
        
        NumberOfProjections metricNumberOfProjections = new NumberOfProjections(be);
        NumberOfSelections metricNumberOfSelections = new NumberOfSelections(be);
        NumberOfTables metricNumberOfTables = new NumberOfTables(be);
        NumberOfAggregationFunctions metricNumberOfAggregationFunctions = new NumberOfAggregationFunctions(be);
        CommonNumberOfProjections metricCommonNumberOfProjections = new CommonNumberOfProjections(be);
        CommonNumberOfSelections metricCommonNumberOfSelections = new CommonNumberOfSelections(be);
        CommonNumberOfAggregationFunctions metricCommonNumberOfAggregationFunctions = new CommonNumberOfAggregationFunctions(be);
        CommonNumberOfTables metricCommonNumberOfTables = new CommonNumberOfTables(be);
        
        
        ExplorationScorer es    = new ExplorationScorer(be);
        es.addMetric(metricNumberOfProjections);
        es.addMetric(metricNumberOfSelections);
        es.addMetric(metricNumberOfTables);
        es.addMetric(metricNumberOfAggregationFunctions);
        es.addMetric(metricCommonNumberOfProjections);
        es.addMetric(metricCommonNumberOfSelections);
        es.addMetric(metricCommonNumberOfAggregationFunctions);
        es.addMetric(metricCommonNumberOfTables);
        
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID",metricNumberOfProjections.getName(),metricNumberOfTables.getName(),metricNumberOfAggregationFunctions.getName(),metricNumberOfSelections.getName(),metricCommonNumberOfProjections.getName(),metricCommonNumberOfSelections.getName(),metricCommonNumberOfAggregationFunctions.getName(),metricCommonNumberOfTables.getName()));          
        
		for (Session sess : myLog.getSessionList()) {
			Exploration e = new Exploration(be,sess);
			//System.out.println(e.getWorkSession());
			ExplorationScore explorationScore = es.score(e);
			System.out.println(explorationScore);
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
	}
}
