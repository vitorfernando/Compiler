/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.util.Hashtable;

/**
 *
 * @author vitor
 */
public abstract class Stmt{
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable){}
}
