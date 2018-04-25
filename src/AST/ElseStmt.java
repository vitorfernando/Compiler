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
public class ElseStmt extends Stmt {

    private ArrayList<Stmt> stmt_list;

    public ElseStmt(ArrayList<Stmt> stmt_list) {
        this.stmt_list = stmt_list;
    }

}
