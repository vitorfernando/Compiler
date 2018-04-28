/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitor
 */
public class CallExpr extends Expr {

    private IdExpr id;
    private CompositeExpr compositeExpr;

    public CallExpr(IdExpr id, CompositeExpr expr_list) {
        this.id = id;
        this.compositeExpr = expr_list;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        id.genC(stream_out,idTable);
        try {
            stream_out.write("(");
            if (compositeExpr != null) {
                compositeExpr.genC(stream_out,idTable);
            }
            stream_out.write(")");
        } catch (IOException ex) {
            Logger.getLogger(CallExpr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
