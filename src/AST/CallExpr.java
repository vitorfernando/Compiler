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
public class CallExpr extends Expr {

    private IdExpr id;
    private CompositeExpr compositeExpr;

    public CallExpr(IdExpr id, CompositeExpr expr_list) {
        this.id = id;
        this.compositeExpr = compositeExpr;
    }

    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
