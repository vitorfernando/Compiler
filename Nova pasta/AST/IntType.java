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
public class IntType extends Type{
    @Override
    public void genC(FileWriter stream_out){
        try {
            stream_out.write("int");
        } catch (IOException ex) {
            Logger.getLogger(FloatType.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getType() {
        return "d"; //To change body of generated methods, choose Tools | Templates.
    }
    
}
