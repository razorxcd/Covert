/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.vit.cassema.dataobject;

import java.util.List;

/**
 *
 * @author SanjayV
 */
public class StatementStore {
    
    private int count;
    private String rowIndex;
    private List<String> colValue;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<String> getColValue() {
        return colValue;
    }

    public void setColValue(List<String> colValue) {
        this.colValue = colValue;
    }
    
    
}
