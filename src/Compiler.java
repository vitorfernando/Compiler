/**
 *
 * @author vitor silva
 */
import Lexer.*;
import Error.*;

public class Compiler {

    // para geracao de codigo
    public static final boolean GC = false;

    public void compile(char[] p_input) {
        error = new CompilerError(lexer);
        lexer = new Lexer(p_input, error);
        error.setLexer(lexer);
        //lexer.nextToken();
        //program();
        while(lexer.token != Symbol.EOF){
            lexer.nextToken();
        }
//        if (lexer.token != Symbol.EOF) {
//            error.signal("nao chegou no fim do arq");
//        }
    }

    // Program ::= 'begin' VarDecList ';' AssignStatement 'end'
    public void program() {
        if (lexer.token != Symbol.BEGIN) {
            error.signal("faltou begin");
        }
        lexer.nextToken();

        varDecList();

        if (lexer.token != Symbol.SEMICOLON) {
            error.signal("faltou ;");
        }
        lexer.nextToken();

        assignStatement();

        if (lexer.token != Symbol.END) {
            error.signal("faltou end");
        }
        lexer.nextToken();

    }

    // VarDecList ::= Variable | Variable ',' VarDecList
    public void varDecList() {
        if (lexer.token != Symbol.IDENT) {
            error.signal("ident nao encontrado");
        }
        lexer.nextToken();

        if (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            varDecList();
        }

    }

    //AssignStatement ::= Variable '=' Expr ';'
    public void assignStatement() {
        if (lexer.token != Symbol.IDENT) {
            error.signal("ident nao encontrato");
        }
        lexer.nextToken();

        if (lexer.token != Symbol.ASSIGN) {
            error.signal(" = nao encontrato");
        }
        lexer.nextToken();

//        expr(); //verifica a expressao

        if (lexer.token != Symbol.SEMICOLON) {
            error.signal("ponto e virgula nao encontrado");
        }

        lexer.nextToken();
    }

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

    //verifica se o token eh um oper + - / *
    public boolean oper(Symbol token) {
        if (token == Symbol.PLUS || token == Symbol.MINUS
                || token == Symbol.MULT || token == Symbol.DIV) {
            lexer.nextToken();
            return true;
        }
        return false;
    }

    private Lexer lexer;
    private CompilerError error;

}
