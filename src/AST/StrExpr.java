/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitor
 */
public class StrExpr extends Expr {

    private String str;

    public StrExpr(String str) {
        this.str = str;
    }

    @Override
    public void genC(FileWriter stream_out) {
        try {
            stream_out.write("\""+str+"\"");
        } catch (IOException ex) {
            Logger.getLogger(StrExpr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
