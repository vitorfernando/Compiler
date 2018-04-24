
/**
 *
 * @author vitor silva
 */
import AST.*;
import Lexer.*;
import Error.*;
import java.util.ArrayList;

public class Compiler {

    // para geracao de codigo
    public static final boolean GC = false;

    public void compile(char[] p_input) {
        error = new CompilerError(lexer);
        lexer = new Lexer(p_input, error);
        error.setLexer(lexer);
        //lexer.nextToken();
        Program p = program();
//        while(lexer.token != Symbol.EOF){
//            lexer.nextToken();
//        }
        if (lexer.token != Symbol.EOF) {
            error.signal("nao chegou no fim do arq");
        }
    }

    // Program ::= PROGRAM id BEGIN pgm_body END
    public Program program() {
        if (lexer.token != Symbol.PROGRAM) {
            error.signal("Programa deve iniciar com a palavra PROGRAM");
        }
        lexer.nextToken();
        if (lexer.token != Symbol.IDENT) {
            error.signal("Programa deve ter um identificador válido");
        }
        String id = lexer.getStringValue();
        lexer.nextToken();
        if (lexer.token != Symbol.BEGIN) {
            error.signal("faltou begin");
        }
        lexer.nextToken();
        ProgramBody pgm_body = programBody();

        if (lexer.token != Symbol.END) {
            error.signal("faltou end");
        } else {
            lexer.nextToken();
        }
        return new Program(id, pgm_body);
    }

    //pgm_body := decl func_declarations
    private ProgramBody programBody() {

        return new ProgramBody();
    }

    //decl = string_decl_list {decl} | var_decl_list {decl} | empty
    private Decl decl() {
        if (lexer.token == Symbol.STRING) {
            ArrayList<StringDecl> string_decl_list = string_decl_list();
            if ((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT))) {
                Decl decl = decl();
                return new Decl(string_decl_list, null, decl);
            }
        }
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            ArrayList<VarDecl> var_decl_list = var_decl_list();
            if ((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT))) {
                Decl decl = decl();
                return new Decl(null, var_decl_list, decl);
            }
        }
        return null;
    }
    //string_decl_list := string_decl {string_decl_tail}

    private ArrayList<StringDecl> string_decl_list() {
        ArrayList<StringDecl> strDeclList = new ArrayList<StringDecl>();
        if (lexer.token == Symbol.STRING) {
            strDeclList.add(string_decl());
        }
        if (lexer.token == Symbol.STRING) {
            strDeclList.addAll(string_decl_tail());
        }
        return strDeclList;
    }

    //var_decl_list := var_decl {var_decl_tail}
    private ArrayList<VarDecl> var_decl_list() {
        ArrayList<VarDecl> varDeclList = new ArrayList<VarDecl>();
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            varDeclList.add(var_decl());
        }
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            varDeclList.addAll(var_decl_tail());
        }
        return varDeclList;
    }

    //var_decl_tail := var_decl {var_decl_tail}
    private ArrayList<VarDecl> var_decl_tail() {
        ArrayList<VarDecl> varDeclList = new ArrayList<VarDecl>();
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            varDeclList.add(var_decl());
        }
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            varDeclList.addAll(var_decl_tail());
        }
        return varDeclList;
    }

    //var_decl := var_type id_list ; | empty
    private VarDecl var_decl() {
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            Type type = var_type();
            ArrayList<String> idList = null;
            if (lexer.token == Symbol.IDENT) {
                idList = id_list();
            }
            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();
            }
            return new VarDecl(type, idList);
        }
        return null;
    }

    //id_list := id id_tail
    private ArrayList<IdExpr> id_list() {
        ArrayList<IdExpr> idList = new ArrayList<IdExpr>();
        if (lexer.token == Symbol.IDENT) {
            idList.add(new IdExpr(lexer.getStringValue()));
            lexer.nextToken();
            if (lexer.token == Symbol.COMMA) {
                idList.addAll(id_tail());
            }
            return idList;
        }
        return null;
    }

    //id_tail := , id id_tail | empty
    private ArrayList<IdExpr> id_tail() {
        ArrayList<IdExpr> idList = new ArrayList<IdExpr>();
        if (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            if (lexer.token == Symbol.IDENT) {
                idList.add(new IdExpr(lexer.getStringValue()));
                lexer.nextToken();
                if (lexer.token == Symbol.COMMA) {
                    idList.addAll(id_tail());
                }
                return idList;
            }
        }
        return null;
    }

    //var_type : = FLOAT | INT
    private Type var_type() {
        if (lexer.token == Symbol.FLOAT) {
            lexer.nextToken();
            return new FloatType();
        }
        if (lexer.token == Symbol.INT) {
            lexer.nextToken();
            return new TypeInt();
        }
        return null;
    }

    //string_decl_tail := string_decl {string_decl_tail}
    private ArrayList<StringDecl> string_decl_tail() {
        ArrayList<StringDecl> strDeclList = new ArrayList<StringDecl>();
        if (lexer.token == Symbol.STRING) {
            strDeclList.add(string_decl());
        }
        if (lexer.token == Symbol.STRING) {
            strDeclList.addAll(string_decl_tail());
        }
        return strDeclList;
    }

    //string_decl:= STRING id := str ; | empty
    private StringDecl string_decl() {
        if (lexer.token == Symbol.STRING) {
            lexer.nextToken();
            if (lexer.token != Symbol.IDENT) {
                error.signal("Faltou um identificador válido");
            }
            String id = lexer.getStringValue();
            lexer.nextToken();
            if (lexer.token != Symbol.ASSIGN) {
                error.signal("Faltou o simbolo :=");
            }
            lexer.nextToken();
            if (lexer.token != Symbol.STRINGLITERAL) {
                error.signal("É esperado uma string");
            }
            String str = lexer.getStringValue();
            if (lexer.token != Symbol.SEMICOLON) {
                error.signal("Faltou o simbolo ;");
            }
            lexer.nextToken();
            return new StringDecl(id, str);
        }
        return null;
    }

    //param_decl_list -> param_decl param_decl_tail
    private ArrayList<ParamDecl> param_decl_list() {
        ArrayList<ParamDecl> param_decl_list = new ArrayList<ParamDecl>();
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            param_decl_list.add(param_decl());
        }
        if (lexer.token == Symbol.COMMA) {
            param_decl_list.addAll(param_decl_tail());
        }

        return param_decl_list;
    }

    //param_decl -> var_type id
    private ParamDecl param_decl() {
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            Type type = var_type();
            if (lexer.token == Symbol.IDENT) {
                String id = lexer.getStringValue();
                lexer.nextToken();
                return new ParamDecl(id, type);
            }
        }
        return null;
    }

    //param_decl_tail -> , param_decl param_decl_tail | empty
    private ArrayList<ParamDecl> param_decl_tail() {
        ArrayList<ParamDecl> param_decl_list = new ArrayList<ParamDecl>();
        if (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
                param_decl_list.add(param_decl());
                if (lexer.token == Symbol.COMMA) {
                    param_decl_list.addAll(param_decl_list());
                }
                return param_decl_list;
            }
        }
        return null;
    }

//    func_declarations -> func_decl {func_decl_tail}
    private ArrayList<FuncDecl> func_declarations() {
        ArrayList<FuncDecl> funcDecl_list = new ArrayList<FuncDecl>();
        if (lexer.token == Symbol.FUNCTION) {
            funcDecl_list.add(func_decl());
            if (lexer.token == Symbol.FUNCTION) {
                funcDecl_list.addAll(func_decl_tail());
            }
        }
        return funcDecl_list;
    }
//func_decl -> FUNCTION any_type id ({param_decl_list}) BEGIN func_body END | empty

    private FuncDecl func_decl() {
        Type type = null;
        ArrayList<ParamDecl> param_decl_list = null;
        FuncBody funcBody;
        String id;
        if (lexer.token == Symbol.FUNCTION) {
            lexer.nextToken();
            if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)
                    || (lexer.token == Symbol.VOID)) {
                type = any_type();
            }

            id = lexer.getStringValue();
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
            }
            if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
                param_decl_list = param_decl_list();
            }
            if (lexer.token == Symbol.RPAR) {
                lexer.nextToken();
            }

            if (lexer.token == Symbol.BEGIN) {
                lexer.nextToken();
            }
            funcBody = func_body();
            if (lexer.token == Symbol.END) {
                lexer.nextToken();
            }
            return new FuncDecl(type, id, param_decl_list, funcBody);
        }
        return null;
    }

    //any_type -> var_type | VOID
    private Type any_type() {
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            return var_type();
        } else if (lexer.token == Symbol.VOID) {
            lexer.nextToken();
            return new VoidType();
        }
        return null;
    }

    //func_decl_tail -> func_decl {func_decl_tail}
    private ArrayList<FuncDecl> func_decl_tail() {
        ArrayList<FuncDecl> funcDecl_list = new ArrayList<FuncDecl>();
        if (lexer.token == Symbol.FUNCTION) {
            funcDecl_list.add(func_decl());
            if (lexer.token == Symbol.FUNCTION) {
                funcDecl_list.addAll(func_decl_tail());
            }
        }
        return funcDecl_list;
    }

    //func_body -> decl stmt_list
    private FuncBody func_body() {
        Decl decl;
        ArrayList<Stmt> stmt_list = new ArrayList<Stmt>();

        if ((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT)
                || (lexer.token == Symbol.INT))) {
            decl = decl();
            stmt_list = stmt_list();
        }

        return null;

    }

    private ArrayList<Stmt> stmt_list() {
        ArrayList<Stmt> stmt_list = new ArrayList<Stmt>();

        return stmt_list;
    }

    //stmt -> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_stmt | call_expr ;
    private Stmt stmt() {
        if (lexer.token == Symbol.IDENT) {
            return assign_stmt();
        }
        if (lexer.token == Symbol.READ) {
            return read_stmt();
        }
        if (lexer.token == Symbol.WRITE) {
            return write_stmt();
        }
        if (lexer.token == Symbol.RETURN) {
            return return_stmt();
        }
        if (lexer.token == Symbol.IF) {
            return ifStmt();
        }

    }

    //assign_stmt -> assign_expr ;
    private AssignStmt assign_stmt() {
        if (lexer.token == Symbol.IDENT) {
            AssignStmt assignStmt = new AssignStmt(assign_expr());
            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();
            }
            return assignStmt;
        }
        return null;
    }

    //assign_expr -> id := expr
    private AssignExpr assign_expr() {
        IdExpr id;
        Expr e;
        if (lexer.token == Symbol.IDENT) {
            id = new IdExpr(lexer.getStringValue());
            lexer.nextToken();

            if (lexer.token == Symbol.ASSIGN) {
                lexer.nextToken();
            }
            e = expr();
            return new AssignExpr(id, e);
        }
        return null;
    }

    //expr -> factor expr_tail
    private Expr expr() {
        Factor factor;
        CompositeExpr composite_expr = null;
        if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
            factor = factor();
            if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS)) {
                composite_expr = expr_tail();
                return new CompositeExpr(factor, null, composite_expr);
            }

        }
        return null;
    }

    //expr_tail -> addop factor expr_tail | empty
    private CompositeExpr expr_tail() {
        Factor factor;
        Oper oper;
        Expr tail;
        if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS)) {
            oper = addop();
            if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                    || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                factor = factor();
                if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS)) {
                    tail = expr_tail();
                    return new CompositeExpr(factor, oper, tail);
                }
            }
        }
        return null;
    }

    //postfix_expr -> primary | call_expr
    private Expr postfix_expr() {
        if (lexer.token == Symbol.IDENT) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.backToken();
                return call_expr();
            }
            lexer.backToken();
            if (lexer.token == Symbol.IDENT) {
                return primary();
            }
        } else if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.INTLITERAL)
                || (lexer.token == Symbol.FLOATLITERAL)) {
            return primary();
        }
        return null;
    }

    //expr_list -> expr expr_list_tail
    private CompositeExpr expr_list() {
        Expr e;
        CompositeExpr expr_list_tail;
        if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
            e = expr();
            if (lexer.token == Symbol.COMMA) {
                expr_list_tail = expr_list_tail();
                return new CompositeExpr(e, null, expr_list_tail);
            }
        }
        return null;
    }

    //expr_list_tail -> , expr expr_list_tail | empty
    private CompositeExpr expr_list_tail() {
        Expr e;
        CompositeExpr expr_list_tail;
        if (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                    || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                e = expr();
                if (lexer.token == Symbol.COMMA) {
                    expr_list_tail = expr_list_tail();
                    return new CompositeExpr(e, null, expr_list_tail);
                }
            }
        }
        return null;
    }

    //call_expr -> id ( {expr_list} )
    private CallExpr call_expr() {
        CompositeExpr expr_list = null;
        if (lexer.token == Symbol.IDENT) {
            IdExpr id = id();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                        || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                    expr_list = expr_list();
                }
                if (lexer.token == Symbol.RPAR) {
                    lexer.nextToken();
                }
                return new CallExpr(id, expr_list);
            }

        }
        return null;
    }

    //factor -> postfix_expr factor_tail
    private Factor factor() {
        Factor f = null;
        if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
            Expr e = postfix_expr();
            if ((lexer.token == Symbol.MULT) || (lexer.token == Symbol.DIV)) {
                f = factor_tail();
            }
            return new Factor(null, e, f);
        }
        return null;
    }

    //factor_tail -> mulop postfix_expr factor_tail | empty
    private Factor factor_tail() {
        Oper oper;
        Expr postfix_expr;
        Factor f = null;
        if ((lexer.token == Symbol.MULT) || (lexer.token == Symbol.DIV)) {
            oper = mulop();
            if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                    || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                postfix_expr = postfix_expr();
                if ((lexer.token == Symbol.MULT) || (lexer.token == Symbol.DIV)) {
                    f = factor_tail();
                }
                return new Factor(oper, postfix_expr, f);
            }
        }
        return null;
    }

    //addop -> + | -
    private Oper addop() {
        Symbol oper = null;
        if (lexer.token == Symbol.PLUS) {
            lexer.nextToken();
            oper = Symbol.PLUS;
        }
        if (lexer.token == Symbol.MINUS) {
            lexer.nextToken();
            oper = Symbol.MINUS;
        }
        return new Oper(oper);
    }

    //primary -> (expr) | id | INTLITERAL | FLOATLITERAL
    private Expr primary() {
        if (lexer.token == Symbol.LPAR) {
            lexer.nextToken();
            Expr e = null;
            if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                    || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                e = expr();
            }

            if (lexer.token == Symbol.RPAR) {
                lexer.nextToken();
            }
            return e;
        }
        if (lexer.token == Symbol.IDENT) {
            IdExpr id = new IdExpr(lexer.getStringValue());
            lexer.nextToken();
            return id();
        }
        if (lexer.token == Symbol.INTLITERAL) {
            int i = lexer.getNumberValue();
            lexer.nextToken();
            return new IntExpr(i);
        }
        if (lexer.token == Symbol.FLOATLITERAL) {
            int f = lexer.getNumberValue();
            lexer.nextToken();
            return new FloatExpr(f);
        }
        return null;
    }

    //id -> IDENTIFIER
    private IdExpr id() {
        String id = null;
        if (lexer.token == Symbol.IDENT) {
            id = lexer.getStringValue();
            lexer.nextToken();

        }

        return new IdExpr(id);
    }

    //mulop -> * | /
    private Oper mulop() {
        Symbol oper = null;
        if (lexer.token == Symbol.MULT) {
            lexer.nextToken();
            oper = Symbol.MULT;
        }
        if (lexer.token == Symbol.DIV) {
            lexer.nextToken();
            oper = Symbol.DIV;
        }
        return new Oper(oper);
    }

    //read_stmt -> READ ( id_list );
    private ReadStmt read_stmt() {
        ArrayList<IdExpr> id_list = new ArrayList<IdExpr>();

        if (lexer.token == Symbol.READ) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if (lexer.token == Symbol.IDENT) {
                    id_list = id_list();
                }

                if (lexer.token == Symbol.RPAR) {
                    lexer.nextToken();
                }
                if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();
                }
                return new ReadStmt(id_list);
            }

        }
        return null;
    }

    //write_stmt -> WRITE ( id_list );
    private WriteStmt write_stmt() {
        ArrayList<IdExpr> id_list = new ArrayList<IdExpr>();

        if (lexer.token == Symbol.WRITE) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if (lexer.token == Symbol.IDENT) {
                    id_list = id_list();
                }

                if (lexer.token == Symbol.RPAR) {
                    lexer.nextToken();
                }
                if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();
                }
                return new WriteStmt(id_list);
            }

        }
        return null;
    }

    //return_stmt -> RETURN expr ;
    private ReturnStmt return_stmt() {
        Expr e = null;
        if (lexer.token == Symbol.RETURN) {
            lexer.nextToken();
            if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                    || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                e = expr();
            }
            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            }
            return new ReturnStmt(e);
        }
        return null;
    }

    //if_stmt -> IF ( cond ) THEN stmt_list else_part ENDIF
    private IfStmt ifStmt() {
        if (lexer.token == Symbol.IF) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if (lexer.token == Symbol.RPAR) {
                    lexer.nextToken();
                    if (lexer.token == Symbol.THEN) {
                        lexer.nextToken();

                        stmt_list();

                        else_part();

                        if (lexer.token == Symbol.ENDIF) {
                            lexer.nextToken();
                        }
                    }

                }

            }
        }
    }

    else_part -> ELSE stmt_list | empty cond -> expr compop expr compop -> < | > | =
for_stmt

    -> FOR( {assign_expr
    }

    ;

    {cond}; {assign_expr
    }
    ) stmt_list ENDFOR //    // VarDecList ::= Variable | Variable ',' VarDecList
            //    public void varDecList() {
            //        if (lexer.token != Symbol.IDENT) {
            //            error.signal("ident nao encontrado");
            //        }
            //        lexer.nextToken();
            //
            //        if (lexer.token == Symbol.COMMA) {
            //            lexer.nextToken();
            //            varDecList();
            //        }
            //
            //    }
            //
            //    //AssignStatement ::= Variable '=' Expr ';'
            //    public void assignStatement() {
            //        if (lexer.token != Symbol.IDENT) {
            //            error.signal("ident nao encontrato");
            //        }
            //        lexer.nextToken();
            //
            //        if (lexer.token != Symbol.ASSIGN) {
            //            error.signal(" = nao encontrato");
            //        }
            //        lexer.nextToken();
            //
            ////        expr(); //verifica a expressao
            //        if (lexer.token != Symbol.SEMICOLON) {
            //            error.signal("ponto e virgula nao encontrado");
            //        }
            //
            //        lexer.nextToken();
            //    }
            // Expr ::= Oper  Expr Expr  | Number | Variable
            //    public void expr() {
            //        if (lexer.token == Symbol.NUMBER || lexer.token == Symbol.IDENT) {
            //            lexer.nextToken();
            //        } else if (oper(lexer.token)) {
            //            expr();
            //            expr();
            //        } else {
            //            error.signal("expressao nao valida");
            //        }
            //
            //    }
            //    //verifica se o token eh um oper + - / *
            //    public boolean oper(Symbol token) {
            //        if (token == Symbol.PLUS || token == Symbol.MINUS
            //                || token == Symbol.MULT || token == Symbol.DIV) {
            //            lexer.nextToken();
            //            return true;
            //        }
            //        return false;
            //    }
    private Lexer lexer;
    private CompilerError error;

}
