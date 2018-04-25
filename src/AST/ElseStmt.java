/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitor
 */
public class ElseStmt extends Stmt {

    private ArrayList<Stmt> stmt_list;

    public ElseStmt(ArrayList<Stmt> stmt_list) {
        this.stmt_list = stmt_list;
    }

    @Override
    public void genC(FileWriter stream_out) {
        try {
            stream_out.write("else{\n");
            if(stmt_list != null){
                for (Stmt stmt : stmt_list) {
                    stmt.genC(stream_out);
                }
            }
            stream_out.write("}\n");
        } catch (IOException ex) {
            Logger.getLogger(ElseStmt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
