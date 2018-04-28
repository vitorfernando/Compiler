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
public class VoidType extends Type{
    @Override
    public void genC(FileWriter stream_out){
        try {
            stream_out.write("void");
        } catch (IOException ex) {
            Logger.getLogger(FloatType.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
