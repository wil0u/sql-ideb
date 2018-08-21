package fr.univ_tours.li.mdjedaini.ideb.olap.query;

import java.util.ArrayList;

import fr.univ_tours.li.mdjedaini.ideb.olap.EAB_Cube;
import fr.univ_tours.li.mdjedaini.ideb.olap.result.Result;

public class QuerySql extends Query{

	ArrayList<String> projectionList = new ArrayList<String>();
	ArrayList<String> selectionList = new ArrayList<String>();
	ArrayList<String> aggregationFunctionList = new ArrayList<String>();
	ArrayList<String> tableList = new ArrayList<String>();
	
	public QuerySql() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public Result execute(Boolean arg_store) {
		// TODO Auto-generated method stub
		return null;
	}


	public ArrayList<String> getProjectionList() {
		return projectionList;
	}


	public void setProjectionList(ArrayList<String> projectionList) {
		this.projectionList = projectionList;
	}


	public ArrayList<String> getSelectionList() {
		return selectionList;
	}


	public void setSelectionList(ArrayList<String> selectionList) {
		this.selectionList = selectionList;
	}


	public ArrayList<String> getAggregationFunctionList() {
		return aggregationFunctionList;
	}


	public void setAggregationFunctionList(ArrayList<String> aggregationFunctionList) {
		this.aggregationFunctionList = aggregationFunctionList;
	}


	public ArrayList<String> getTableList() {
		return tableList;
	}


	public void setTableList(ArrayList<String> tableList) {
		this.tableList = tableList;
	}
	
	
}
