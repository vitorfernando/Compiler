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
public class Decl extends Expr {

    private ArrayList<StringDecl> string_decl_list;
    private ArrayList<VarDecl> var_decl_list;
    private Decl decl;

    public Decl(ArrayList<StringDecl> string_decl_list, ArrayList<VarDecl> var_decl_list, Decl decl) {
        this.string_decl_list = string_decl_list;
        this.var_decl_list = var_decl_list;
        this.decl = decl;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        if (string_decl_list != null) {
            for (StringDecl str_decl : string_decl_list) {
                str_decl.genC(stream_out,idTable);
            }
        }

        if (var_decl_list != null) {
            for (VarDecl var_decl : var_decl_list) {
                var_decl.genC(stream_out,idTable);
            }
            try {
                stream_out.write(";\n");
            } catch (IOException ex) {
                Logger.getLogger(Decl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (decl != null) {
            decl.genC(stream_out,idTable);
        }
    }

}
