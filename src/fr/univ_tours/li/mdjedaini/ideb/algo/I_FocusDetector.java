/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.mdjedaini.ideb.algo;

import fr.univ_tours.li.mdjedaini.ideb.eval.Exploration;
import java.util.List;

/**
 *
 * @author mahfoud
 */
public interface I_FocusDetector {
    
    /**
     * 
     * @param arg_exp
     * @return 
     */
    public List<FocusZone> detectFocusZones(Exploration arg_exp);
    
}
