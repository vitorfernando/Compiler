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
public class IfStmt extends Stmt {

    private CompositeExpr cond;
    private ArrayList<Stmt> stmt_list;
    private ElseStmt else_part;

    public IfStmt(CompositeExpr cond, ArrayList<Stmt> stmt_list, ElseStmt else_part) {
        this.cond = cond;
        this.stmt_list = stmt_list;
        this.else_part = else_part;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        try {
            stream_out.write("if(");
            if (cond != null) {
                cond.genC(stream_out,idTable);
            }
            stream_out.write("){\n");
            if (stmt_list != null) {
                for (Stmt stmt : stmt_list) {
                    stmt.genC(stream_out,idTable);
                }
            }
            stream_out.write("}\n");
            if (else_part != null) {
                else_part.genC(stream_out,idTable);
            }
        } catch (IOException ex) {
            Logger.getLogger(IfStmt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
