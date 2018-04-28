/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vitor
 */
public class FuncDecl extends Expr {

    private Type type;
    private ArrayList<ParamDecl> param_decl_list;
    private IdExpr id;
    private FuncBody funcBody;

    public FuncDecl(Type type, IdExpr id, ArrayList<ParamDecl> param_decl_list, FuncBody funcBody) {
        this.type = type;
        this.param_decl_list = param_decl_list;
        this.id = id;
        this.funcBody = funcBody;
    }

    @Override
    public void genC(FileWriter stream_out,Hashtable<String, Type> idTable) {
        type.genC(stream_out);
        try {
            stream_out.write(" ");
            id.genC(stream_out,idTable);
            stream_out.write("(");
            if (param_decl_list != null) {
                if (param_decl_list.size() > 1) {
                    param_decl_list.get(0).genC(stream_out,idTable);
                    for(int i = 1; i< param_decl_list.size(); i++){
                        stream_out.write(", ");
                        param_decl_list.get(i).genC(stream_out,idTable);
                    }
                } else {
                    for (ParamDecl paramDecl : param_decl_list) {
                        paramDecl.genC(stream_out,idTable);
                    }
                }

            }
            stream_out.write("){\n");
            if (funcBody != null) {
                funcBody.genC(stream_out,idTable);
            }
            stream_out.write("}\n");
        } catch (IOException ex) {
            Logger.getLogger(FuncDecl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
