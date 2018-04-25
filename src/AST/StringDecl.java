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
public class StringDecl extends Expr{

    private IdExpr id;
    private StrExpr str;

    public StringDecl(IdExpr id, StrExpr str) {
        this.id = id;
        this.str = str;
    }

    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
