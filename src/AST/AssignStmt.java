/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitor
 */
public class AssignStmt extends Stmt {

    private AssignExpr assign_expr;

    public AssignStmt(AssignExpr assign_expr) {
        this.assign_expr = assign_expr;
    }

    @Override
    public void genC(FileWriter stream_out) {
        assign_expr.genC(stream_out);
        try {
            stream_out.write(";\n");
        } catch (IOException ex) {
            Logger.getLogger(AssignStmt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
