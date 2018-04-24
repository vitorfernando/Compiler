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
public class WriteStmt extends Stmt{
 private ArrayList<IdExpr> id_list;

    public WriteStmt(ArrayList<IdExpr> id_list) {
        this.id_list = id_list;
    }
}
