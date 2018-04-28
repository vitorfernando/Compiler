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
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        if (e != null) {
            e.genC(stream_out,idTable);
        }
        if(oper != null){
            oper.genC(stream_out,idTable);
        }
        if(f != null){
            f.genC(stream_out,idTable);
        }
    }
    
}
