/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author vitor
 */
public class VarDecl extends Expr{
    private Type type;
    private ArrayList<String> idList;
    
    public VarDecl(Type type, ArrayList<String> idList) {
        this.idList = idList;
        this.type = type;
    }

    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
