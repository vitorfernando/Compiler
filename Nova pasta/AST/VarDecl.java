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
public class VarDecl extends Expr {

    private Type type;
    private ArrayList<IdExpr> idList;

    public VarDecl(Type type, ArrayList<IdExpr> idList) {
        this.idList = idList;
        this.type = type;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        type.genC(stream_out);
        try {
            stream_out.write(" ");
        } catch (IOException ex) {
            Logger.getLogger(VarDecl.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (idList.size() > 1) {
            idList.get(0).genC(stream_out,idTable);

            for (int i = 1; i < idList.size(); i++) {
                try {
                    stream_out.write(",");
                } catch (IOException ex) {
                    Logger.getLogger(VarDecl.class.getName()).log(Level.SEVERE, null, ex);
                }
                idList.get(i).genC(stream_out,idTable);
            }
        } else {
            for (IdExpr idExpr : idList) {
                idExpr.genC(stream_out,idTable);
            }
        }

    }

}
