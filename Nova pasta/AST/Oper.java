/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitor
 */
public class Oper extends Expr {

    private Symbol oper;
    
    public Oper(Symbol oper) {
        this.oper = oper;
    }
    
    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        try {
            stream_out.write(oper.toString());
        } catch (IOException ex) {
            Logger.getLogger(Oper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
