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
public class Factor extends Expr{
    private Oper oper;
    Expr e;
    Factor f;

    public Factor(Oper oper, Expr e, Factor f) {
        this.oper = oper;
        this.e = e;
        this.f = f;
    }
    
    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
