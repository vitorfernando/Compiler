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
public class CallStmt extends Stmt {

    private CallExpr call_expr;

    public CallStmt(CallExpr call_expr) {
        this.call_expr = call_expr;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        if (call_expr != null) {
            call_expr.genC(stream_out,idTable);
        }
        try {
            stream_out.write(";\n");
        } catch (IOException ex) {
            Logger.getLogger(CallStmt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
