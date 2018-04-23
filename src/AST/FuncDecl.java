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
public class FuncDecl extends Expr {

    private Type type;
    private ArrayList<ParamDecl> param_decl_list;
    private String id;
    private FuncBody funcBody;

    public FuncDecl(Type type, String id, ArrayList<ParamDecl> param_decl_list, FuncBody funcBody) {
        this.type = type;
        this.param_decl_list = param_decl_list;
        this.id = id;
        this.funcBody = funcBody;
    }

    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
