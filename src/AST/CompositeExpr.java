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
public class CompositeExpr extends Expr{
    private Oper oper;
    private Expr eleft;
    private Expr eright;

    public CompositeExpr( Expr eleft ,Oper oper, Expr eright) {
        this.eleft = eleft;
        this.oper = oper;
        this.eright = eright;
    }
    
    
    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
