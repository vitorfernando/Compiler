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
public class Factor extends Expr {

    private Oper oper;
    Expr e;
    Factor f;
    
    public Factor(Oper oper, Expr e, Factor f) {
        this.oper = oper;
        this.e = e;
        this.f = f;
    }
    
    @Override
    public void genC(FileWriter stream_out) {
        if (e != null) {
            e.genC(stream_out);
        }
        if(oper != null){
            oper.genC(stream_out);
        }
        if(f != null){
            f.genC(stream_out);
        }
    }
    
}
