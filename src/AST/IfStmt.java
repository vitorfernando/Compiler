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
public class IfStmt extends Stmt {

    private CompositeExpr cond;
    private ArrayList<Stmt> stmt_list;
    private ElseStmt else_part;

    public IfStmt(CompositeExpr cond, ArrayList<Stmt> stmt_list, ElseStmt else_part) {
        this.cond = cond;
        this.stmt_list = stmt_list;
        this.else_part = else_part;
    }

   
}
