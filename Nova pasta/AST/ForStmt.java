/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitor
 */
public class ForStmt extends Stmt {

    private ArrayList<Stmt> stmt_list;
    private AssignExpr initial_assign_expr;
    private AssignExpr final_assign_expr;
    private CompositeExpr cond;

    public ForStmt(AssignExpr initial_assign_expr, CompositeExpr cond, AssignExpr final_assign_expr, ArrayList<Stmt> stmt_list) {
        this.initial_assign_expr = initial_assign_expr;
        this.cond = cond;
        this.final_assign_expr = final_assign_expr;
        this.stmt_list = stmt_list;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        try {
            stream_out.write("for(");
            if (initial_assign_expr != null) {
                initial_assign_expr.genC(stream_out,idTable);
                stream_out.write("; ");
            }
            if (cond != null) {
                cond.genC(stream_out,idTable);
                stream_out.write("; ");
            }
            if (final_assign_expr != null) {
                final_assign_expr.genC(stream_out,idTable);
            }
            stream_out.write("){\n");
            if (stmt_list != null) {
                for (Stmt stmt : stmt_list) {
                    stmt.genC(stream_out,idTable);
                }
            }
            stream_out.write("}\n");
        } catch (IOException ex) {
            Logger.getLogger(ForStmt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
