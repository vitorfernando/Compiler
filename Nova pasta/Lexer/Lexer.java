/**
 *
 * @author vitor silva
 */
package Lexer;

import java.util.*;
import Error.*;

public class Lexer {

    // apenas para verificacao lexica
    public static final boolean DEBUGLEXER = true;

    public Lexer(char[] input, CompilerError error) {
        this.input = input;
        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
        // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        this.error = error;
    }

    // contains the keywords - palavras reservadas da linguagem
    static private Hashtable<String, Symbol> keywordsTable;

    // this code will be executed only once for each program execution
    static {
        keywordsTable = new Hashtable<String, Symbol>();
        keywordsTable.put("begin", Symbol.BEGIN);
        keywordsTable.put("end", Symbol.END);
        keywordsTable.put("program", Symbol.PROGRAM);
        keywordsTable.put("return", Symbol.RETURN);
        keywordsTable.put("function", Symbol.FUNCTION);
        keywordsTable.put("if", Symbol.IF);
        keywordsTable.put("else", Symbol.ELSE);
        keywordsTable.put("for", Symbol.FOR);
        keywordsTable.put("endfor", Symbol.ENDFOR);
        keywordsTable.put("endif", Symbol.ENDIF);
        keywordsTable.put("then", Symbol.THEN);
        keywordsTable.put("read", Symbol.READ);
        keywordsTable.put("write", Symbol.WRITE);
        keywordsTable.put("end", Symbol.END);
        keywordsTable.put("float", Symbol.FLOAT);
        keywordsTable.put("int", Symbol.INT);
        keywordsTable.put("void", Symbol.VOID);
        keywordsTable.put("string", Symbol.STRING);
    }

    public void nextToken() {
        //pula espaços em branco, quebras de linhas e tabulações
        while (input[tokenPos] == ' '
                || input[tokenPos] == '\n' || input[tokenPos] == '\t' || input[tokenPos] == '\r') {
            if (input[tokenPos] == '\n') {
                lineNumber++;
            }
            tokenPos++;
        }
        //chegou no final do arq
        if (input[tokenPos] == '\0') {
            lastToken = token;
            lastTokenPos = tokenPos;
            token = Symbol.EOF;
            return;
        }

        //verificar se eh comentario
        if (input[tokenPos] == '-' && input[tokenPos + 1] == '-') {
            while (input[tokenPos] != '\n' && input[tokenPos] != '\0') {
                tokenPos++;
            }
            nextToken(); //importante
            return;
        }

        //quero reconhecer o token
        //o token sera um dos Symbol
        StringBuffer aux = new StringBuffer();
        while (Character.isDigit(input[tokenPos])) {
            aux = aux.append(input[tokenPos]); //vai concatenando o numero, ainda eh string
            tokenPos++;
        }
        //aux eh um digito
        if (aux.length() > 0) {
            if (input[tokenPos] != '.') {
                if (Character.isLetter(input[tokenPos])) {
                    error.signal("identificador deve iniciar com uma letra seguida de no máximo 30 letras ou numeros(ex: aaa3333dede44).");
                } else {
                    //converte string para inteiro
                    numberValue = Integer.parseInt(aux.toString());
                    if (numberValue > MaxValueInteger) {
                        error.signal("estourou o numero int");
                    }
                    lastToken = token;
                    lastTokenPos = tokenPos;
                    token = Symbol.INTLITERAL;
                }
            } else {//é um float 676767.67678
                aux = aux.append(input[tokenPos]); //concatena o ponto
                tokenPos++;
                int lenght = aux.length();
                //concatena o resto dos digitos
                while (Character.isDigit(input[tokenPos])) {
                    aux = aux.append(input[tokenPos]);
                    tokenPos++;
                }
                if (aux.length() <= lenght) {
                    error.signal("Expressão de float mal escrita, (float deve ser no formato ex: .765 ou 211.211).");
                } else {
                    floatValue = Float.parseFloat(aux.toString());
                }
                lastToken = token;
                lastTokenPos = tokenPos;
                token = Symbol.FLOATLITERAL;
            }
            //eh um float no formato .4455
        } else if (input[tokenPos] == '.') {
            aux = new StringBuffer();
            aux = aux.append(input[tokenPos]); //concatena o ponto
            tokenPos++;
            int lenght = aux.length();
            //concatena o resto dos digitos
            while (Character.isDigit(input[tokenPos])) {
                aux = aux.append(input[tokenPos]);
                tokenPos++;
            }
            if (aux.length() <= lenght) {
                error.signal("Expressão de float mal escrita, (float deve ser no formato ex: .765 ou 211.211).");
            } else {
                floatValue = Float.parseFloat("0" + aux.toString());
            }
            lastToken = token;
            lastTokenPos = tokenPos;
            token = Symbol.FLOATLITERAL;
        } else {     //nao é um digito
            while (Character.isLetter(input[tokenPos])) {
                aux = aux.append(input[tokenPos]); //vai concatenando todas as letras, ainda eh string
                tokenPos++;
                //concatena numeros apos a primeira letra se houverem
                while (Character.isDigit(input[tokenPos])) {
                    aux = aux.append(input[tokenPos]);
                    tokenPos++;
                }
            }

            if (aux.length() > 0) {
                Symbol temp;
                temp = keywordsTable.get(aux.toString().toLowerCase()); //verifica na key word hash
                if (temp == null) { //nao eh palavra
                    if (aux.length() > 30) {
                        error.signal("Identificador com mais que 30 digitos");
                    }
                    lastToken = token;
                    lastTokenPos = tokenPos;
                    token = Symbol.IDENT;
                    lastStringValue = stringValue;
                    stringValue = aux.toString();
                } else {
                    lastToken = token;
//                    lastTokenPos = tokenPos;
                    token = temp;
                }
            } else {
                switch (input[tokenPos]) {
                    case '+':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.PLUS;
                        break;
                    case '-':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.MINUS;
                        break;
                    case '/':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.DIV;
                        break;
                    case '*':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.MULT;
                        break;
                    case '=':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.EQUAL;
                        break;
                    case ':':
                        if (input[tokenPos + 1] != '=') {
                            String msg = "" + input[tokenPos];
                            tokenPos++;
                            while (input[tokenPos] != ' ') {
                                msg = msg.concat("" + input[tokenPos]);
                                tokenPos++;
                            }
                            error.signal("Erro lexico, simbolo '" + msg + "' não reconhecido.");
                        } else {
                            lastTokenPos = tokenPos;
                            tokenPos= tokenPos + 2;
                            lastToken = token;                           
                            token = Symbol.ASSIGN;
                        }
                        break;
                    case '<':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.LT;
                        break;
                    case '>':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.GT;
                        break;
                    case '(':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.LPAR;
                        break;
                    case ')':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.RPAR;
                        break;
                    case ',':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.COMMA;
                        break;
                    case ';':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.SEMICOLON;
                        break;
                    case '"':
                        tokenPos++;

                        StringBuffer str = new StringBuffer();
                        while (input[tokenPos] != '"') {
                            str = str.append(input[tokenPos]);
                            tokenPos++;
                        }

                        if (str.length() > 0) {
                            lastStringValue = stringValue;
                            stringValue = str.toString();
                        } else {
                            lastStringValue = stringValue;
                            stringValue = "";
                        }
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.STRINGLITERAL;
                        break;
                    case '\'':
                        lastToken = token;
                        lastTokenPos = tokenPos;
                        token = Symbol.MARKS;
                        break;
                    default:
                        error.signal("erro lexico, simbolo '" + input[tokenPos] + "' não reconhecido.");
                }
                tokenPos++;
            }
        }

//        if (DEBUGLEXER) {
//            System.out.println(token.toString());
//        }
        System.out.println(token.toString());
    }

    public void backToken() {
        token = lastToken;
        tokenPos = lastTokenPos;
    }

    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }

    public String getCurrentLine() {
        int i = lastTokenPos;
        if (i == 0) {
            i = 1;
        } else if (i >= input.length) {
            i = input.length;
        }

        StringBuffer line = new StringBuffer();
        // go to the beginning of the line
        while (i >= 1 && input[i] != '\n') {
            i--;
        }
        if (input[i] == '\n') {
            i++;
        }
        // go to the end of the line putting it in variable line
        while (input[i] != '\0' && input[i] != '\n' && input[i] != '\r') {
            line.append(input[i]);
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public char getCharValue() {
        return charValue;
    }

    public float getFloatValue() {
        return floatValue;
    }
    // current token
    public Symbol token;
    public Symbol lastToken;
    private String stringValue;
    private String lastStringValue;
    private int numberValue;
    private float floatValue;

    private char charValue;

    private int tokenPos;
    //  input[lastTokenPos] is the last character of the last token
    private int lastTokenPos;
    // program given as input - source code
    private char[] input;

    // number of current line. Starts with 1
    private int lineNumber;

    private CompilerError error;
    private static final int MaxValueInteger = 32768;
}
