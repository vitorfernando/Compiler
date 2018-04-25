/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author vitor
 */
public class FuncBody extends Expr {

    private Decl decl;
    private ArrayList<Stmt> stmt_list;

    public FuncBody(Decl decl, ArrayList<Stmt> stmt_list) {
        this.decl = decl;
        this.stmt_list = stmt_list;
    }

    @Override
    public void genC(FileWriter stream_out) {
        if (decl != null) {
            decl.genC(stream_out);
        }
        if (stream_out != null) {
            for (Stmt stmt : stmt_list) {
                stmt.genC(stream_out);
            }
        }
    }

}
