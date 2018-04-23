/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

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
    
}
