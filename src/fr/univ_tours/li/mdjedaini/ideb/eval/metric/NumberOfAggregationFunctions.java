package fr.univ_tours.li.mdjedaini.ideb.eval.metric;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.li.mdjedaini.ideb.BenchmarkEngine;
import fr.univ_tours.li.mdjedaini.ideb.eval.Exploration;
import fr.univ_tours.li.mdjedaini.ideb.eval.scoring.MetricScore;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.Query;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.QuerySql;
import fr.univ_tours.li.mdjedaini.ideb.struct.Session;
import fr.univ_tours.li.mdjedaini.ideb.tools.Stats;

public class NumberOfAggregationFunctions extends Metric {

	public NumberOfAggregationFunctions(BenchmarkEngine arg_be) {
		super(arg_be);
		this.name           = "Metric - Number of aggregation functions";
        this.description    = "Number of aggregation functions in the current SQL query.";
	}
	
	
	public MetricScore apply(Exploration arg_tr) {
        MetricScore result  = new MetricScore(this, arg_tr);
        
        List<Double> queryScoreList = new ArrayList<>();
        Session workSession         = arg_tr.getWorkSession();

        for(Query q_tmp : workSession.getQueryList()) {
            QuerySql qt = (QuerySql)q_tmp;
            queryScoreList.add(new Double(qt.getAggregationFunctionList().size()));
        }
        
        result.score            = Stats.average(queryScoreList);
        result.addScoreList(queryScoreList);
        
        return result;
    }
}
