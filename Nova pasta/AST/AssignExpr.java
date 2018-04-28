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
public class AssignExpr extends Expr {

    private IdExpr id;
    private Expr e;

    public AssignExpr(IdExpr id, Expr e) {
        this.id = id;
        this.e = e;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        id.genC(stream_out,idTable);
        try {
            stream_out.write(" = ");
        } catch (IOException ex) {
            Logger.getLogger(AssignExpr.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (e != null) {
            e.genC(stream_out,idTable);
        }
    }

}
