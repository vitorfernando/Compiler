/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;

/**
 *
 * @author vitor
 */
public abstract class Type {

    private String type;

    public void genC(FileWriter stream_out) {
    }

    public String getType() {
        return type;
    }
}
