/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.mdjedaini.ideb.eval.metric.sut;

import fr.univ_tours.li.mdjedaini.ideb.BenchmarkEngine;
import fr.univ_tours.li.mdjedaini.ideb.eval.Exploration;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.Metric;
import fr.univ_tours.li.mdjedaini.ideb.eval.metric.tasksuccess.MetricDiscoveryPrecision;
import fr.univ_tours.li.mdjedaini.ideb.eval.scoring.MetricScore;

/**
 * This metric evaluates the evolution of the access area.
 * Access area is defined as all the primary cells (tuples) accessed.
 * It evaluates how rich is the access area provided by the SUT.
 * @author mahfoud
 */
public class MetricTaskSuccessSecondary extends Metric {

    Metric metric;
    
    /**
     * 
     * @param arg_be 
     */
    public MetricTaskSuccessSecondary(BenchmarkEngine arg_be) {
        super(arg_be);
        this.metric         = new MetricDiscoveryPrecision(arg_be);
    }
    
    /**
     * 
     * @param arg_tr
     * @return 
     */
    @Override
    public MetricScore apply(Exploration arg_tr) {
        return this.metric.apply(arg_tr);
    }
    
    /**
     * 
     * @return 
     */
    public String getName() {
        return this.metric.getName();
    }
    
}
