/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;

/**
 *
 * @author vitor
 */
public class Oper extends Expr{
    private Symbol oper;

    public Oper(Symbol oper) {
        this.oper = oper;
    }
    
    
    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
