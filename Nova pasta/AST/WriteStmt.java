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
public class WriteStmt extends Stmt {

    private ArrayList<IdExpr> id_list;

    public WriteStmt(ArrayList<IdExpr> id_list) {
        this.id_list = id_list;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        try {
            stream_out.write("printf(\"");
            if (id_list != null) {
                for (IdExpr idExpr : id_list) {
                    stream_out.write("\"%");
                    stream_out.write(((Type)idTable.get(idExpr.getId())).getType());
                    stream_out.write(" ");
                }
                stream_out.write("\"");
                for(IdExpr idExpr: id_list){
                    stream_out.write(",");
                    stream_out.write(idExpr.getId());
                }
            }
            stream_out.write(");\n");
        } catch (IOException ex) {
            Logger.getLogger(ReadStmt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
