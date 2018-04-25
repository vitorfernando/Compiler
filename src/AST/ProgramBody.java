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
public class ProgramBody {

    private Decl decl;
    private ArrayList<FuncDecl> func_declarations;

    public ProgramBody(Decl decl, ArrayList<FuncDecl> func_decl) {
        this.decl = decl;
        this.func_declarations = func_decl;
    }

    public void genC(FileWriter stream_out) {
        if (decl != null) {
            decl.genC(stream_out);
        }
        if (func_declarations != null) {
            for (FuncDecl funcDecl : func_declarations) {
                funcDecl.genC(stream_out);
            }

        }
    }

}
