package fr.univ_tours.li.mdjedaini.ideb.eval.metric;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.li.mdjedaini.ideb.BenchmarkEngine;
import fr.univ_tours.li.mdjedaini.ideb.eval.Exploration;
import fr.univ_tours.li.mdjedaini.ideb.eval.scoring.MetricScore;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.QuerySql;
import fr.univ_tours.li.mdjedaini.ideb.struct.Session;
import fr.univ_tours.li.mdjedaini.ideb.tools.Stats;

public class CommonNumberOfAggregationFunctions extends Metric{

	public CommonNumberOfAggregationFunctions(BenchmarkEngine arg_be) {
        super(arg_be);
        this.name           = "Metric - Common aggregation functions number";
        this.description    = "Computes iteratively the number of common aggregation functions between queries";
    }

    public MetricScore apply(Exploration arg_tr) {
        MetricScore result  = new MetricScore(this, arg_tr);
        
        List<Double> queryScoreList = new ArrayList<>();
        Session workSession         = arg_tr.getWorkSession();
        ArrayList<String> intersec;
        //queryScoreList.add(0.);
        Integer commonAggregationFunctions = 0;
        for(int i = 0; i < arg_tr.getWorkSession().getNumberOfQueries(); i++) {
        	
        	if(i>0) {
            	QuerySql q1 = (QuerySql)workSession.getQueryByPosition(i);
            	QuerySql q2 = (QuerySql)workSession.getQueryByPosition(i-1);
        		intersec = (ArrayList<String>) intersection(q1.getAggregationFunctionList(),q2.getAggregationFunctionList());
        		commonAggregationFunctions   = intersec.size();
        	}
            queryScoreList.add(commonAggregationFunctions.doubleValue());
        }
        
        result.score            = Stats.average(queryScoreList);
        result.addScoreList(queryScoreList);
        
        return result;
    }

    
    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
}
