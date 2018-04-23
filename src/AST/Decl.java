/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author vitor
 */
public class Decl extends Expr{
    private ArrayList<StringDecl> decl_list;
    private ArrayList<VarDecl> var_decl_list;
    private Decl decl;
    
    public Decl(ArrayList<StringDecl> string_decl_list,ArrayList<VarDecl> var_decl_list, Decl decl) {
        this.decl_list = string_decl_list;
        this.var_decl_list = var_decl_list;
        this.decl = decl;
    }
    
    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
