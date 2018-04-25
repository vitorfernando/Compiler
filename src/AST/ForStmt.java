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
public class ForStmt extends Stmt {

    private ArrayList<Stmt> stmt_list;
    private AssignExpr initial_assign_expr;
    private AssignExpr final_assign_expr;
    private CompositeExpr cond;

    public ForStmt(AssignExpr initial_assign_expr, CompositeExpr cond, AssignExpr final_assign_expr, ArrayList<Stmt> stmt_list) {
        this.initial_assign_expr = initial_assign_expr;
        this.cond = cond;
        this.final_assign_expr = final_assign_expr;
        this.stmt_list = stmt_list;
    }

}
