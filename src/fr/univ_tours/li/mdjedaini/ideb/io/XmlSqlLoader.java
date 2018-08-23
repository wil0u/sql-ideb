package fr.univ_tours.li.mdjedaini.ideb.io;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.univ_tours.li.mdjedaini.ideb.olap.query.QuerySql;
import fr.univ_tours.li.mdjedaini.ideb.struct.Log;
import fr.univ_tours.li.mdjedaini.ideb.struct.Session;

public class XmlSqlLoader implements I_LogLoader{
	File inputFile;
	public XmlSqlLoader() {
		this.inputFile = new File(
				"C:\\Users\\wilou\\source\\test.xml");
	}
	public XmlSqlLoader(File inputFile) {
		this.inputFile = inputFile;
	}
	@Override
	public Log loadLog() {
		// TODO Auto-generated method stub
		Log myLog = new Log();
		try {
			int nb_requete = 0;
			ArrayList<QuerySql> queryList = new ArrayList<QuerySql>();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("requête");
			System.out.println("----------------------------");
			NodeList explorations = doc.getElementsByTagName("exploration");
			ArrayList<Session> sessions = new ArrayList<Session>();

			for (int l = 0; l < explorations.getLength(); l++) {
				Session session = new Session();
				Node exploration = explorations.item(l);
				if (exploration.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) exploration;
					NodeList requetes = eElement.getElementsByTagName("request");
					for (int d = 0; d < requetes.getLength(); d++) {
						nb_requete = nb_requete + 1;
						Node requete = requetes.item(d);
						if (requete.getNodeType() == Node.ELEMENT_NODE) {
							Element eElementbis = (Element) requete;
							QuerySql query = new QuerySql();
							ArrayList<String> projectionList = new ArrayList<String>();
							ArrayList<String> selectionList = new ArrayList<String>();
							ArrayList<String> aggregateFunctionList = new ArrayList<String>();
							ArrayList<String> tableList = new ArrayList<String>();
							NodeList projectionsNode = eElementbis.getElementsByTagName("projections");
							NodeList selectionsNode = eElementbis.getElementsByTagName("selections");
							NodeList aggregationFunctionsNode = eElementbis.getElementsByTagName("function_aggregates");
							NodeList fromClauseNode = eElementbis.getElementsByTagName("tables");
							
							if(projectionsNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
								Element ElementProjections = (Element) projectionsNode.item(0);
								NodeList projections = ElementProjections.getElementsByTagName("projection");
								for (int j = 0; j < projections.getLength(); j++) {
									System.out.println(projections.item(j).getTextContent());
								
									projectionList.add((projections.item(j).getTextContent()));

								}
							}
							if(selectionsNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
								Element ElementSelections = (Element) selectionsNode.item(0);
								NodeList selections = ElementSelections.getElementsByTagName("selection");
								for (int j = 0; j < selections.getLength(); j++) {
									System.out.println(selections.item(j).getTextContent());
									selectionList.add((selections.item(j).getTextContent()));

								}
							}
							if(aggregationFunctionsNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
								Element ElementAggregationFunctions = (Element) aggregationFunctionsNode.item(0);
								NodeList aggregationFunctions = ElementAggregationFunctions.getElementsByTagName("function_aggregate");
								for (int j = 0; j < aggregationFunctions.getLength(); j++) {
									System.out.println(aggregationFunctions.item(j).getTextContent());
									aggregateFunctionList.add((aggregationFunctions.item(j).getTextContent()));

								}
							}
							if(fromClauseNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
								Element ElementFromClauses = (Element) fromClauseNode.item(0);
								NodeList tables = ElementFromClauses.getElementsByTagName("table");
								for (int j = 0; j < tables.getLength(); j++) {
									System.out.println(tables.item(j).getTextContent());
									tableList.add((tables.item(j).getTextContent()));

								}
								
							}
							query.setAggregationFunctionList(aggregateFunctionList);
							query.setProjectionList(projectionList);
							query.setSelectionList(selectionList);
							query.setTableList(tableList);
							queryList.add(query);
							session.addQuery(query);
						}
					}
					
				}
				
				myLog.addSession(session);	
			}
			System.out.println("le nombre d'exploration est :"+ sessions.size()+" pour un nombre de requêtes total égal a "+nb_requete);
		} catch (Exception e) {

			e.printStackTrace();

		}
	
		return myLog;
	}

}
