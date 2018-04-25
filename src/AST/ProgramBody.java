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
public class ProgramBody {

    private Decl decl;
    private FuncDecl func_decl;

    public ProgramBody(Decl decl, FuncDecl func_decl) {
        this.decl = decl;
        this.func_decl = func_decl;
    }

}
