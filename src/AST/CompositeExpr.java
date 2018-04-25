/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;

/**
 *
 * @author vitor
 */
public class CompositeExpr extends Expr {

    private boolean flag_lm;
    private Oper oper;
    private Expr eleft;
    private Expr eright;

    public CompositeExpr(Expr eleft, Oper oper, Expr eright, boolean flag_lm) {
        this.eleft = eleft;
        this.oper = oper;
        this.eright = eright;
        this.flag_lm = flag_lm;
    }

    @Override
    public void genC(FileWriter stream_out) {
        if (!flag_lm) {
            if (eleft != null) {
                eleft.genC(stream_out);
            }
            if (oper != null) {
                oper.genC(stream_out);
            }
            if (eright != null) {
                eright.genC(stream_out);
            }
        } else {
            if (oper != null) {
                oper.genC(stream_out);
            }
            if (eleft != null) {
                eleft.genC(stream_out);
            }
            if (eright != null) {
                eright.genC(stream_out);
            }
        }
    }

}
