/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.mdjedaini.ideb.olap.result;

import fr.univ_tours.li.mdjedaini.ideb.BenchmarkEngine;
import fr.univ_tours.li.mdjedaini.ideb.ext.falseto.FalsetoQueryConverter;
import fr.univ_tours.li.mdjedaini.ideb.olap.EAB_Cube;
import fr.univ_tours.li.mdjedaini.ideb.olap.EAB_Hierarchy;
import fr.univ_tours.li.mdjedaini.ideb.olap.EAB_Measure;
import fr.univ_tours.li.mdjedaini.ideb.olap.EAB_Member;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.MeasureFragment;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.ProjectionFragment;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.Query;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.QueryTriplet;
import fr.univ_tours.li.mdjedaini.ideb.olap.query.SelectionFragment;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mondrian.olap.Axis;
import mondrian.olap.Cell;
import mondrian.olap.Member;
import mondrian.olap.Position;

/**
 *
 * @author mahfoud
 */
public class EAB_Cell {
    
    //
    mondrian.olap.Cell mondrianCell;
    org.olap4j.Cell olap4jCell;
    
    /**
     *
     */
    public static Integer cidIterator  = 0;
    
    //

    /**
     *
     */
    public Integer cellId;
    Result result;
    
    // cell data

    /**
     *
     */
    public EAB_Measure measure;

    /**
     *
     */
    public Map<EAB_Hierarchy, EAB_Member> memberByHierarchy;
    
    // value of the cell
    Object value;
    
    /**
     *
     */
    public EAB_Cell() {
        this.cellId     = EAB_Cell.cidIterator;
        EAB_Cell.cidIterator++;
    }
    
//    /**
//     * 
//     * @param arg_r
//     * @param arg_olap4jCell 
//     */
//    public EAB_Cell(Result arg_r, org.olap4j.Cell arg_olap4jCell) {
//        this();
//        this.result         = arg_r;
//        this.olap4jCell     = arg_olap4jCell;
//        
//        //String sqlQuery = arg_mondrianCell.getDrillThroughSQL(true);
//        
//        // set the measure
//        mondrian.olap.Hierarchy mh  = this.getCube().getRandomMeasure().getHierarchy().getMondrianHierarchy();
//        Member mm       = arg_olap4jCell.getCellSet().getMetaData().getContextMember(mh);
//        EAB_Measure mea = this.getCube().getMeasureByMondrianMember(mm);
//        this.measure    = mea;
//        
//        // set the positions
//        this.memberByHierarchy  = new HashMap<>();
//        for(EAB_Hierarchy h_tmp : this.getCube().getHierarchyList()) {
//            mondrian.olap.Member m_tmp  = arg_mondrianCell.getContextMember(h_tmp.getMondrianHierarchy());
//            EAB_Member eam              = this.getCube().getMemberByMondrianMember(m_tmp);
//            this.setMemberForHierarchy(h_tmp, eam);
//        }
//        
////        System.out.println("valeur de la cellule: " + arg_mondrianCell.getValue());
//        
//        // I check if everything is ok in the mondrian cell
//        if(this.isOK()) {
//            
//            if(this.mondrianCell.getValue().equals(0)) {
//                this.value  = 0.;
//            } else {
//                this.value  = (Double)this.mondrianCell.getValue();
//            }
//            
//        }
//        
//    }
    
    /**
     * Creates a cell from a Mondrian cell...
     * @param arg_r
     * @param arg_mondrianCell 
     */
    public EAB_Cell(Result arg_r, mondrian.olap.Cell arg_mondrianCell) {
        this();
        this.result         = arg_r;
        this.mondrianCell   = arg_mondrianCell;
        this.value          = arg_mondrianCell.getValue();
        
        //String sqlQuery = arg_mondrianCell.getDrillThroughSQL(true);
        
        // set the measure
        mondrian.olap.Hierarchy mh  = this.getCube().getRandomMeasure().getHierarchy().getMondrianHierarchy();
        Member mm                   = arg_mondrianCell.getContextMember(mh);
        EAB_Measure mea             = this.getCube().getMeasureByMondrianMember(mm);
        this.measure                = mea;
        
        // set the positions
        this.memberByHierarchy  = new HashMap<>();
        for(EAB_Hierarchy h_tmp : this.getCube().getHierarchyList()) {
            mondrian.olap.Member m_tmp  = arg_mondrianCell.getContextMember(h_tmp.getMondrianHierarchy());
            EAB_Member eam              = this.getCube().getMemberByMondrianMember(m_tmp);
            this.setMemberForHierarchy(h_tmp, eam);
        }
        
//        System.out.println("valeur de la cellule: " + arg_mondrianCell.getValue());
        
        // I check if everything is ok in the mondrian cell
//        if(this.isOK()) {
//            
//            if(this.mondrianCell.getValue().equals(0)) {
//                this.value  = 0.;
//            } else {
//                this.value  = (Double)this.mondrianCell.getValue();
//            }
//            
//        }
        
        
        
    }
    
    /**
     * 
     * @param arg_r
     * @param arg_measure 
     */
    public EAB_Cell(Result arg_r, EAB_Measure arg_measure) {
        this();
        this.result     = arg_r;
        this.measure    = arg_measure;
        this.memberByHierarchy  = new HashMap<>();
    }
    
    /**
     * 
     * @param arg_cell 
     */
    public EAB_Cell(EAB_Cell arg_cell) {
        this();
        this.result             = arg_cell.result;
        this.measure            = arg_cell.measure;
        this.memberByHierarchy  = new HashMap<>();
        
        for(EAB_Hierarchy h_tmp : arg_cell.getResult().getCube().getHierarchyList()) {
            this.memberByHierarchy.put(h_tmp, arg_cell.getMemberByHierarchy(h_tmp));
        }
                
    }

    /**
     * 
     * @param arg_c
     * @param arg_h
     * @return 
     */
    public Boolean isChildOfCellOnHierarchy(EAB_Cell arg_c, EAB_Hierarchy arg_h) {
        return this.getMemberByHierarchy(arg_h).isChildOf(arg_c.getMemberByHierarchy(arg_h));
    }
    
    /**
     * 
     * @param arg_c
     * @param arg_h
     * @return 
     */
    public Boolean isDirectChildOfCellOnHierarchy(EAB_Cell arg_c, EAB_Hierarchy arg_h) {
        return this.getMemberByHierarchy(arg_h).isDirectChildOf(arg_c.getMemberByHierarchy(arg_h));
    }
    
    /**
     * 
     * @param arg_c
     * @return 
     */
    public Boolean isDirectChildOfCell(EAB_Cell arg_c) {
        Boolean result  = true;
        
        Set<EAB_Hierarchy> hierarchyDifferential    = this.getDifferentialHierarchyList(arg_c);
        
        if(hierarchyDifferential.size() != 1) {
            return false;
        }

        // must be direct child on differental hierarchy
        EAB_Hierarchy h_tmp = (EAB_Hierarchy)hierarchyDifferential.iterator().next();
        result  = result && this.isDirectChildOfCellOnHierarchy(arg_c, h_tmp);
        
        // must have the same measure
        result  = result && arg_c.getMeasure().equals(this.getMeasure());
        
        return result;
    }
    
    /**
     * Performs a drill down for a current hierarchy...
     * @param arg_h
     * @return 
     */
    public Collection<EAB_Cell> rollOnHierarchy(EAB_Hierarchy arg_h) {
        HashSet<EAB_Cell> res = new HashSet<>();
        
        for(EAB_Member m_tmp : this.getMemberByHierarchy(arg_h).getParents()) {
            
            EAB_Cell c_tmp  = new EAB_Cell(this);
            c_tmp.setMemberForHierarchy(arg_h, m_tmp);
            
            res.add(c_tmp);
            
        }
        
        return res;
    }
    
    /**
     * Performs a drill down for a current hierarchy...
     * @param arg_h
     * @return 
     */
    public Collection<EAB_Cell> drillOnHierarchy(EAB_Hierarchy arg_h) {
        HashSet<EAB_Cell> res = new HashSet<>();
        
        for(EAB_Member m_tmp : this.getMemberByHierarchy(arg_h).getChildren()) {
            
            EAB_Cell c_tmp  = new EAB_Cell(this);
            c_tmp.setMemberForHierarchy(arg_h, m_tmp);
            
            res.add(c_tmp);
            
        }
        
        return res;
    }
    
    /**
     * Performs a drill down for a current hierarchy...
     * @param arg_h
     * @return 
     */
    public Collection<EAB_Cell> switchOnHierarchy(EAB_Hierarchy arg_h) {
        HashSet<EAB_Cell> res = new HashSet<>();
        
        for(EAB_Member m_tmp : this.getMemberByHierarchy(arg_h).getSiblings()) {
            
            EAB_Cell c_tmp  = new EAB_Cell(this);
            c_tmp.setMemberForHierarchy(arg_h, m_tmp);
            
            res.add(c_tmp);
            
        }
        
        return res;
    }
    
    /**
     * 
     * @return 
     */
    public EAB_Measure getMeasure() {
        return this.measure;
    }
    
    /**
     * 
     * @param arg_hierarchy
     * @return 
     */
    public EAB_Member getMemberByHierarchy(EAB_Hierarchy arg_hierarchy) {
        return this.memberByHierarchy.get(arg_hierarchy);
    }
    
    /**
     * 
     * @return 
     */
    public Cell getMondrianCell() {
        return this.mondrianCell;
    }
    
    /**
     * Returns the value of the cell.
     * If the value actually exists and has a value, we return the value.
     * If the cell has value Null, or it is an error, the cell value is then 0.
     * @return 
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Returns the value of the cell as Double.
     * If it is a type compatible with Double, conversion is done by this function.
     * If the type of the cell value is not compatible, we return null...
     * @return 
     */
    public Double getValueAsDouble() {
        if(this.value instanceof Double) {
            return (Double)this.value;
        } else if(this.value instanceof Integer) {
            return ((Integer)this.value).doubleValue();
        } else {
            // @todo a revoir ici on retourne 0 si la cellule est nulle
            //System.err.println("La cellule n'est ni un Double, ni un Integer...");
            return 0.;
        }
    }
    
    /**
     * 
     * @return 
     */
    public Boolean isOK() {
        if(this.mondrianCell == null ) {
            Integer m = 1;
        }
        return !this.mondrianCell.isNull() && !this.mondrianCell.isError();
    }
    
    /**
     * 
     */
    public void computeValue() {
        
        BenchmarkEngine be          = this.getCube().getBencharkEngine();
        mondrian.olap.Connection mc = be.getConnection().getMondrianConnection();
        
        FalsetoQueryConverter fc    = new FalsetoQueryConverter(be);
        mondrian.olap.Query mq      = fc.convertQuery(this.getResult().getQuery()).toMDX();
        
        System.out.println("Query begin executed:");
        System.out.println(this);
        System.out.println("MDX: " + mq);
        
        mondrian.olap.Result r      = mc.execute(mq);
        
//        mondrian.olap.Cell[][] matrix = Utils.getCellMatrix(r);
        
        for(Axis a : r.getAxes()) {
            
            
            for(Position p : a.getPositions()) {
                
                for(Member m : p) {
                    
                    System.out.println("Member: " + m);
                    
                }
                
            }
        }
        
        int[] coord = {0, 0};
        Object v    = r.getCell(coord).getValue();
        Object fv   = r.getCell(coord).getFormattedValue();
        
        this.value  = (Double)v;
        
    }
    
    /**
     * 
     * @return 
     */
    public EAB_Cube getCube() {
        return this.getResult().getCube();
    }
    
    /**
     * 
     * @param arg_hierarchy
     * @param arg_member 
     */
    public void setMemberForHierarchy(EAB_Hierarchy arg_hierarchy, EAB_Member arg_member) {
        this.memberByHierarchy.put(arg_hierarchy, arg_member);
    }
    
    /**
     * 
     * @param arg_member 
     */
    public void addMember(EAB_Member arg_member) {
        this.memberByHierarchy.put(arg_member.getLevel().getHierarchy(), arg_member);
    }

    /**
     * 
     * @return 
     */
    public Result getResult() {
        return result;
    }
    
    /**
     * Returns a query for this cell.
     * @return 
     */
    public Query getQueryForCell() {
        EAB_Cube cube       = this.getResult().getCube();
        QueryTriplet result = new QueryTriplet(cube);
        
        // ! do not forget measure!
        MeasureFragment mf_tmp  = new MeasureFragment(result, this.measure);
        result.addMeasure(mf_tmp);
        
        // now add the member for each hierarchy
        for(EAB_Hierarchy h_tmp : cube.getHierarchyList()) {
            EAB_Member m_tmp        = this.getMemberByHierarchy(h_tmp);
            SelectionFragment sf    = new SelectionFragment(result, m_tmp);
            result.addSelection(sf);
            
            if(!m_tmp.isAll()) {
                ProjectionFragment pf   = new ProjectionFragment(result, m_tmp.getLevel());
                result.addProjection(pf);
            }
        }
        
        return result;
    }
    
    /**
     * 
     * @param arg_c2
     * @return 
     */
    public Set<EAB_Hierarchy> getDifferentialHierarchyList(EAB_Cell arg_c2) {
        EAB_Cube cube                       = this.getResult().getCube();
        Set<EAB_Hierarchy> h_differential   = new HashSet<>();
        
        // return false if they differ on at least one hierarchy (measure included)
        for(EAB_Hierarchy h_tmp : cube.getHierarchyList()) {
            
            EAB_Member m1   = this.getMemberByHierarchy(h_tmp);
            EAB_Member m2   = arg_c2.getMemberByHierarchy(h_tmp);
            
            // continue if members are equal
            if(!m1.equals(m2)) {
                h_differential.add(h_tmp);
            }
        }
        
        return h_differential;
    }
    
    /**
     * todo
     * @return 
     */
    public String stringHash() {
        String result   = "";
        
        result  += this.measure.getUniqueName();
        
        for(EAB_Hierarchy h_tmp : this.result.getQuery().getCube().getHierarchyList()) {
            EAB_Member m_tmp    = this.getMemberByHierarchy(h_tmp);
            result  += ", " + m_tmp.getUniqueName();
        }
        
        return result;
    }
    
    /**
     * Returns true if the cell match the criteria provided as arguments...
     * @param arg_criteria
     * @return 
     */
    public boolean matchCriteria(Map<EAB_Hierarchy, EAB_Member> arg_criteria) {
       
        for(EAB_Hierarchy h_tmp : arg_criteria.keySet()) {
            if(!this.memberByHierarchy.get(h_tmp).equals(arg_criteria.get(h_tmp))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * this function must return the value of the given cell.
     * It has to:
     * 1/ build a query that retrieves this only cell
     * 2/ actually retrieve the cell
     * @return 
     */
    public Double retrieveCellValue() {
        Double result   = 0.;
        
        return result;
    }
    
    // tostring useful for the graph display
    public String toString() {
        String result   = "ID: " + this.cellId + " - ";
        
        if(this.value != null) {
            result  += "(" + this.value + ") - ";
        }
        
        result  += this.measure.getUniqueName();
        
        for(EAB_Hierarchy h_tmp : this.result.getQuery().getCube().getHierarchyList()) {
            EAB_Member m_tmp    = this.getMemberByHierarchy(h_tmp);
            if(!m_tmp.isAll()) {
                result  += ", " + m_tmp.getUniqueName();
            }
        }
        

        
        return result;
    }
   
    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int result  = 5;
        
        result      = result + this.measure.hashCode();
        
        for(EAB_Hierarchy h_tmp : this.getResult().getCube().getHierarchyList()) {
            result  = result + this.getMemberByHierarchy(h_tmp).getName().hashCode();
        }
        
        return result;
    }
    
    /**
     * 
     * @param arg_cell
     * @return 
     */
    @Override
    public boolean equals(Object arg_cell) {
        EAB_Cell c_tmp  = (EAB_Cell)arg_cell;
        
        boolean result  = true;
        
        for(EAB_Hierarchy h_tmp : this.getResult().getCube().getHierarchyList()) {
            result  = result && this.getMemberByHierarchy(h_tmp).equals(c_tmp.getMemberByHierarchy(h_tmp));
        }
        
        result  = result && this.measure.equals(c_tmp.getMeasure());
        
        return result;
    }
    
    /**
     * 
     */
//    public int hashCode() {
//        int hash    = 7;
//        hash        = 19 * hash + this.measureMember.toString().hashCode();
//        for(String h_name : Globals.benchmarkEngine.getInternalCube().getHierarchyNameList(true)) {
//            Member m_tmp    = this.getMemberByHierarchy(h_name);
//            hash            = 19 * hash + EAB_Cube.getMemberName(m_tmp).toString().hashCode();
//        }
//        return hash;
//    }
    
    public static class Comparators {

        /**
         *
         */
        public static Comparator<EAB_Cell> VALUE = new Comparator<EAB_Cell>() {
            @Override
            public int compare(EAB_Cell arg_c1, EAB_Cell arg_c2) {
                if(arg_c1.getValueAsDouble() - arg_c2.getValueAsDouble() > 0) return 1;
                if(arg_c1.getValueAsDouble() - arg_c2.getValueAsDouble() < 0) return -1;
                return 0;
            }
        };
        
    }
    
}
