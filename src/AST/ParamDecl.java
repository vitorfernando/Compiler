/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author vitor
 */
public class ParamDecl extends Expr{
    private String id;
    Type type;

    public ParamDecl(String id, Type type) {
        this.id = id;
        this.type = type;
    }
    
    
    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
