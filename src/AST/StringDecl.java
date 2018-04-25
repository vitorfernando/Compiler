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
public class StringDecl extends Expr {

    private IdExpr id;
    private StrExpr str;

    public StringDecl(IdExpr id, StrExpr str) {
        this.id = id;
        this.str = str;
    }

    @Override
    public void genC(FileWriter stream_out) {
        try {
            stream_out.write("char ");
        } catch (IOException ex) {
            Logger.getLogger(StringDecl.class.getName()).log(Level.SEVERE, null, ex);
        }
        id.genC(stream_out);
        try {
            stream_out.write("[] = ");
        } catch (IOException ex) {
            Logger.getLogger(StringDecl.class.getName()).log(Level.SEVERE, null, ex);
        }
        str.genC(stream_out);
        try {
            stream_out.write(";\n");
        } catch (IOException ex) {
            Logger.getLogger(StringDecl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
