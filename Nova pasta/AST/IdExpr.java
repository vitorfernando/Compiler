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
public class IdExpr extends Expr {

    private String id;

    public IdExpr(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        try {
            stream_out.write(id);
        } catch (IOException ex) {
            Logger.getLogger(IdExpr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
