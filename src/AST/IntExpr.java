/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.util.Hashtable;

/**
 *
 * @author vitor
 */
public class IntExpr extends Expr {

    private int i;

    public IntExpr(int i) {
        this.i = i;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
