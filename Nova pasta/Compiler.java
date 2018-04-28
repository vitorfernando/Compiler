
/**
 *
 * @author vitor silva
 */
import AST.*;
import Lexer.*;
import Error.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
 
public class Compiler {
    public static Hashtable<String, Type> idTable = new Hashtable<String, Type>();
    // para geracao de codigo
    public static final boolean GC = false;

    public void compile(char[] p_input, FileWriter stream_out) {
        error = new CompilerError(lexer);
        lexer = new Lexer(p_input, error);
        error.setLexer(lexer);
        lexer.nextToken();
        Program p = program();
        if (lexer.token != Symbol.EOF) {
            error.signal("nao chegou no fim do arq");
        }
        p.genC(stream_out,idTable);

    }

    public static Hashtable<String, Type> getIdTable() {
        return idTable;
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
        Decl decl = null;

        ArrayList<FuncDecl> func_declarations = null;
        if ((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT)
                || (lexer.token == Symbol.INT))) {
            decl = decl();
        }

        if (lexer.token == Symbol.FUNCTION) {
            func_declarations = func_declarations();
        }
        return new ProgramBody(decl, func_declarations);
    }

    //decl = string_decl_list {decl} | var_decl_list {decl} | empty
    private Decl decl() {
        Decl decl = null;
        if (lexer.token == Symbol.STRING) {
            ArrayList<StringDecl> string_decl_list = string_decl_list();
            if ((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT))) {
                decl = decl();

            }
            return new Decl(string_decl_list, null, decl);
        }
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            ArrayList<VarDecl> var_decl_list = var_decl_list();
            if ((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT))) {
                decl = decl();
            }
            return new Decl(null, var_decl_list, decl);
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
            ArrayList<IdExpr> idList = null;

            if (lexer.token == Symbol.IDENT) {
                idList = id_list();
                if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();
                } else {
                    error.signal("Faltou o ;");
                }
            }
            for (IdExpr idExpr : idList) {
                idTable.put(idExpr.getId(), type);
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
            return new IntType();
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
//str -> STRINGLITERAL

    private StrExpr str() {
        StrExpr str = null;
        if (lexer.token == Symbol.STRINGLITERAL) {
            str = new StrExpr(lexer.getStringValue());
            lexer.nextToken();
        }
        return str;
    }

    //string_decl:= STRING id := str ; | empty
    private StringDecl string_decl() {
        if (lexer.token == Symbol.STRING) {
            lexer.nextToken();
            IdExpr id = null;
            StrExpr str = null;
            if (lexer.token == Symbol.IDENT) {
                id = id();
                if (lexer.token == Symbol.ASSIGN) {
                    lexer.nextToken();
                    if (lexer.token == Symbol.STRINGLITERAL) {
                        str = str();
                        if (lexer.token == Symbol.SEMICOLON) {
                            lexer.nextToken();
                        } else {
                            error.signal("Faltou o simbolo ;");
                        }
                    } else {
                        error.signal("É esperado uma string");
                    }
                } else {
                    error.signal("Faltou o simbolo :=");
                }
            } else {
                error.signal("Faltou um identificador válido");
            }
            idTable.put(id.getId(),new TypeString());
            return new StringDecl(id, str);
        }
        return null;
    }

    //param_decl_list -> param_decl param_decl_tail
    private ArrayList<ParamDecl> param_decl_list() {
        ArrayList<ParamDecl> param_decl_list = new ArrayList<ParamDecl>();
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            param_decl_list.add(param_decl());
            if (lexer.token == Symbol.COMMA) {
                param_decl_list.addAll(param_decl_tail());
            }
        }

        return param_decl_list;
    }

    //param_decl -> var_type id
    private ParamDecl param_decl() {
        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
            Type type = var_type();
            if (lexer.token == Symbol.IDENT) {
                IdExpr id = new IdExpr(lexer.getStringValue());
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
        FuncBody funcBody = null;
        IdExpr id = null;

        if (lexer.token == Symbol.FUNCTION) {
            lexer.nextToken();
            if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)
                    || (lexer.token == Symbol.VOID)) {
                type = any_type();
                if (lexer.token == Symbol.IDENT) {
                    id = id();

                    if (lexer.token == Symbol.LPAR) {
                        lexer.nextToken();
                        if ((lexer.token == Symbol.FLOAT) || (lexer.token == Symbol.INT)) {
                            param_decl_list = param_decl_list();
                        }
                        if (lexer.token == Symbol.RPAR) {
                            lexer.nextToken();
                            if (lexer.token == Symbol.BEGIN) {
                                lexer.nextToken();
                                if (((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT)
                                        || (lexer.token == Symbol.INT)))
                                        || ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                                        || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                                        || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR))) {
                                    funcBody = func_body();
                                }
                                if (lexer.token == Symbol.END) {
                                    lexer.nextToken();
                                }
                            }
                        }
                    }
                }
            }

        }
        return new FuncDecl(type, id, param_decl_list, funcBody);
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
        Decl decl = null;
        ArrayList<Stmt> stmt_list = new ArrayList<Stmt>();

        if ((lexer.token == Symbol.STRING) || ((lexer.token == Symbol.FLOAT)
                || (lexer.token == Symbol.INT))) {
            decl = decl();

        }
        if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
            stmt_list = stmt_list();
        }

        return new FuncBody(decl, stmt_list);

    }

    //stmt -> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_stmt | call_stmt ;
    private Stmt stmt() {
        if (lexer.token == Symbol.IDENT) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.backToken();
                return call_stmt();
            }
            lexer.backToken();
        }
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
        if (lexer.token == Symbol.FOR) {
            return for_stmt();
        }
        return null;
    }

    //stmt_list -> stmt stmt_tail | empty
    private ArrayList<Stmt> stmt_list() {
        ArrayList<Stmt> stmt_list = new ArrayList<Stmt>();
        if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
            stmt_list.add(stmt());
            if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                    || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                    || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
                stmt_list.addAll(stmt_list_tail());
            }
        }
        return stmt_list;
    }

    //stmt_tail -> stmt stmt_tail | empty
    private ArrayList<Stmt> stmt_list_tail() {
        ArrayList<Stmt> stmt_list = new ArrayList<Stmt>();
        if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
            stmt_list.add(stmt());
            if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                    || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                    || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
                stmt_list.addAll(stmt_list_tail());
            }
        }
        return stmt_list;
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
            id = id();

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

        if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
            factor = factor();
            if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.MINUS)) {
                composite_expr = expr_tail();

            }
            return new CompositeExpr(factor, null, composite_expr, false);

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
                    return new CompositeExpr(factor, oper, tail, true);
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
        CompositeExpr expr_list_tail = null;
        if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
            e = expr();
            if (lexer.token == Symbol.COMMA) {
                expr_list_tail = expr_list_tail();
            }
            return new CompositeExpr(e, null, expr_list_tail, false);
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
                    return new CompositeExpr(e, null, expr_list_tail, false);
                }
            }
        }
        return null;
    }

    private CallStmt call_stmt() {
        if (lexer.token == Symbol.IDENT) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.backToken();
                CallStmt stmt = new CallStmt(call_expr());
                if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();
                }
                return stmt;
            }
            lexer.backToken();
        }
        return null;
    }

    //call_expr -> id ( {expr_list} )
    private CallExpr call_expr() {
        CompositeExpr expr_list = null;
        IdExpr id = null;

        if (lexer.token == Symbol.IDENT) {
            id = id();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                        || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                    expr_list = expr_list();
                }
                if (lexer.token == Symbol.RPAR) {
                    lexer.nextToken();
                }
            }
        }
        return new CallExpr(id, expr_list);
    }

    //factor -> postfix_expr factor_tail
    private Factor factor() {
        Factor f = null;
        if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
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
                if (lexer.token == Symbol.RPAR) {
                    lexer.nextToken();
                }
            }
            return e;
        }
        if (lexer.token == Symbol.IDENT) {

            return id();
        }
        if (lexer.token == Symbol.INTLITERAL) {
            IntExpr i = new IntExpr(lexer.getNumberValue());
            lexer.nextToken();
            return i;
        }
        if (lexer.token == Symbol.FLOATLITERAL) {
            FloatExpr f = new FloatExpr(lexer.getNumberValue());
            lexer.nextToken();
            return f;
        }
        return null;
    }

    //id -> IDENTIFIER
    private IdExpr id() {
        IdExpr id = null;
        if (lexer.token == Symbol.IDENT) {
            id = new IdExpr(lexer.getStringValue());
            lexer.nextToken();
        }
        return id;
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
        ArrayList<IdExpr> id_list = null;

        if (lexer.token == Symbol.WRITE) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if (lexer.token == Symbol.IDENT) {
                    id_list = id_list();
                    if (lexer.token == Symbol.RPAR) {
                        lexer.nextToken();
                        if (lexer.token == Symbol.SEMICOLON) {
                            lexer.nextToken();
                        } else {
                            error.signal("Faltou o ;");
                        }
                    }
                }
            }

        }
        return new WriteStmt(id_list);
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
        CompositeExpr cond = null;
        ArrayList<Stmt> stmt_list = null;
        ElseStmt else_part = null;

        if (lexer.token == Symbol.IF) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                        || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                    cond = cond();
                    if (lexer.token == Symbol.RPAR) {
                        lexer.nextToken();
                        if (lexer.token == Symbol.THEN) {
                            lexer.nextToken();
                            if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                                    || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                                    || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
                                stmt_list = stmt_list();
                            }
                            if (lexer.token == Symbol.ELSE) {
                                else_part = else_part();
                            }
                            if (lexer.token == Symbol.ENDIF) {
                                lexer.nextToken();
                            }
                        }

                    }

                }
            }
        }
        return new IfStmt(cond, stmt_list, else_part);
    }

    //for_stmt -> FOR ({assign_expr}; {cond}; {assign_expr}) stmt_list ENDFOR
    private ForStmt for_stmt() {
        ArrayList<Stmt> stmt_list = null;
        AssignExpr initial_assign_expr = null;
        AssignExpr final_assign_expr = null;
        CompositeExpr cond = null;

        if (lexer.token == Symbol.FOR) {
            lexer.nextToken();
            if (lexer.token == Symbol.LPAR) {
                lexer.nextToken();
                if (lexer.token == Symbol.IDENT) {
                    initial_assign_expr = assign_expr();
                }
                if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();
                    if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                            || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                        cond = cond();
                    }
                    if (lexer.token == Symbol.SEMICOLON) {
                        lexer.nextToken();
                        if (lexer.token == Symbol.IDENT) {
                            final_assign_expr = assign_expr();
                        }
                        if (lexer.token == Symbol.RPAR) {
                            lexer.nextToken();
                            if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                                    || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                                    || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
                                stmt_list = stmt_list();
                                if (lexer.token == Symbol.ENDFOR) {
                                    lexer.nextToken();
                                }
                            }
                        }
                    } else {
                        error.signal("Faltou o ;");
                    }
                } else {
                    error.signal("Faltou o ;");
                }
            }
        }
        return new ForStmt(initial_assign_expr, cond, final_assign_expr, stmt_list);
    }

    //cond -> expr compop expr
    private CompositeExpr cond() {
        Expr le = null;
        Oper oper = null;
        Expr re = null;

        if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
            le = expr();
            if ((lexer.token == Symbol.LT) || (lexer.token == Symbol.GT)
                    || (lexer.token == Symbol.EQUAL)) {
                oper = compop();
                if ((lexer.token == Symbol.LPAR) || (lexer.token == Symbol.LPAR) || (lexer.token == Symbol.IDENT)
                        || (lexer.token == Symbol.INTLITERAL) || (lexer.token == Symbol.FLOATLITERAL)) {
                    {
                        re = expr();

                    }
                }
            }
        }
        return new CompositeExpr(le, oper, re, false);
    }
    //compop -> < | > | =

    private Oper compop() {
        Oper oper;
        if (lexer.token == Symbol.LT) {
            oper = new Oper(Symbol.LT);
            lexer.nextToken();
            return oper;
        }
        if (lexer.token == Symbol.GT) {
            oper = new Oper(Symbol.GT);
            lexer.nextToken();
            return oper;
        }
        if (lexer.token == Symbol.EQUAL) {
            oper = new Oper(Symbol.EQUAL);
            lexer.nextToken();
            return oper;
        }
        return null;
    }

    //else_part -> ELSE stmt_list | empty
    private ElseStmt else_part() {
        if (lexer.token == Symbol.ELSE) {
            lexer.nextToken();
            if ((lexer.token == Symbol.IDENT) || (lexer.token == Symbol.READ)
                    || (lexer.token == Symbol.WRITE) || (lexer.token == Symbol.RETURN)
                    || (lexer.token == Symbol.IF) || (lexer.token == Symbol.FOR)) {
                return new ElseStmt(stmt_list());
            }
        }
        return null;
    }

    // VarDecList ::= Variable | Variable ',' VarDecList
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
