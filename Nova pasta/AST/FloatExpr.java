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
public class FloatExpr extends Expr {

    private float f;

    public FloatExpr(float f) {
        this.f = f;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        try {
            stream_out.write(Float.toString(f));
        } catch (IOException ex) {
            Logger.getLogger(StrExpr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
