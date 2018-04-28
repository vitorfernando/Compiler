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
public class ParamDecl extends Expr{
    private IdExpr id;
    Type type;

    public ParamDecl(IdExpr id, Type type) {
        this.id = id;
        this.type = type;
    }
    
    
    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        type.genC(stream_out);
        try {
            stream_out.write(" ");
        } catch (IOException ex) {
            Logger.getLogger(ParamDecl.class.getName()).log(Level.SEVERE, null, ex);
        }
        id.genC(stream_out,idTable);
        
    }
    
}
