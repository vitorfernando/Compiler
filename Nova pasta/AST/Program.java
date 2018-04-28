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
public class Program {

    private String id;
    private ProgramBody pgm_body;

    public Program(String id, ProgramBody pgm_body) {
        this.id = id;
        this.pgm_body = pgm_body;
    }

    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        try {
            stream_out.write("#include<stdio.h>\n");
            stream_out.write("#include<string.h>\n");
            stream_out.write("#include<stdlib.h>\n");
            pgm_body.genC(stream_out,idTable);
        } catch (IOException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
