/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oginol.codigos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vilson
 */
public class AnalisadorLexico {

    private String endereco;
    private String enderecoCompleto;
    private List<Token> tokens;
    
    public AnalisadorLexico() {
        this.endereco = "";
        this.enderecoCompleto = "";
        this.tokens = new ArrayList<>();
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public List<Token> getTokens() {
        return tokens;
    }
    
    public void reconhecedor(List<String> arquivo)
    {
        for(int i = 0; i < arquivo.size(); i++)
        {
            reconheceToken(arquivo.get(i), i);
        }
        
        palavraReservada();
        
        /*for(int i = 0; i < tokens.size(); i++)
        {
            System.out.println("{" + tokens.get(i).getClasse() + "} - Valor = " + tokens.get(i).getValor());
        }*/
    }
    
    private void palavraReservada()
    {
        for(int i = 0; i < tokens.size(); i++)
        {
            String tipo = tokens.get(i).getClasse();
            
            if(tipo.equals("ID"))
            {
                String verifica = reservada(tokens.get(i).getValor());
                
                if(!verifica.equals("NonExiste"))
                {
                    Token token = new Token();
                    
                    token.setClasse(verifica);
                    token.setColuna(tokens.get(i).getColuna());
                    token.setLinha(tokens.get(i).getLinha());
                    token.setValor(tokens.get(i).getValor());
                    
                    tokens.set(i, token);
                }
            }
        }
    }
    
    private String reservada(String classe)
    {
        String palavra;
        switch (classe)
        {
            case "Errte": palavra = "ERRTE"; break;
            case "Liendo": palavra = "LIENDO"; break;
            case "mail": palavra = "MAIL"; break;
            case "for": palavra = "FOR"; break;
            case "while": palavra = "WHILE"; break;
            case "if": palavra = "IF"; break;
            case "else": palavra = "ELSE"; break;
            case "switch": palavra = "SWITCH"; break;
            case "case": palavra = "CASE"; break;
            case "break": palavra = "BREAK"; break;
            case "int": palavra = "TIPO_NUM_INTEGER"; break;
            case "float": palavra = "TIPO_NUM_FLOAT"; break;
            case "char": palavra = "TIPO_NUM_CHAR"; break;
            default: palavra = "NonExiste"; break;
        }
        
        return palavra;
    }
    
    private void reconheceToken(String texto, int linha)
    {
        Token token = new Token();
        char[] text = texto.toCharArray();
        int tamanho = texto.length();
        
        String palavra = "";
        for(int i = 0; i < tamanho; i++)
        {
            char digito = text[i];
            token = new Token();
            if(digito == ' ')
            {
                if(!palavra.equals(""))
                {
                    PalavraAnterior(linha, palavra, i);
                    palavra = "";
                }
                
                continue;
            }
            
            if(palavra.equals(""))
            {
                if(Character.isDigit(digito))
                {
                    palavra += digito;
                }
                else
                {
                    palavra = primeiraLetra(digito, linha, palavra, i);
                }
            }
            else
            {
                if(digito == '=')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        palavra = Igual(digito, linha, palavra, i);
                    }
                }
                else if(digito == '|')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        if(palavra.length() == 1 && palavra.equals("|"))
                        {
                            token.setLinha(linha);
                            token.setColuna(i);
                            token.setClasse("OP_LOGICO_OU_LOGICO");
                            token.setValor("||");
                            tokens.add(token);
                            palavra = "";
                        }
                        else
                        {
                            PalavraAnterior(linha, palavra, i);
                            palavra = "|";
                        }
                    }
                }
                else if(digito == '&')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        if(palavra.length() == 1 && palavra.equals("&"))
                        {
                            token.setLinha(linha);
                            token.setColuna(i);
                            token.setClasse("OP_LOGICO_E_LOGICO");
                            token.setValor("&&");
                            tokens.add(token);
                            palavra = "";
                        }
                        else
                        {
                            PalavraAnterior(linha, palavra, i);
                            palavra = "&";
                        }
                    }
                }
                else if(digito == '"')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        if(palavra.length() == 1)
                        {
                            token.setLinha(linha);
                            token.setColuna(i);
                            token.setClasse("NUM_CHAR");
                            token.setValor(palavra + digito);
                            tokens.add(token);
                            palavra = "";
                        }
                        else if(palavra.length() == 2)
                        {
                            token.setLinha(linha);
                            token.setColuna(i);
                            token.setClasse("NUM_CHAR");
                            token.setValor(palavra + digito);
                            tokens.add(token);
                            palavra = "";
                        }
                        else
                        {
                            token.setLinha(linha);
                            token.setColuna(i);
                            token.setClasse("ERRO");
                            token.setValor("X");
                            tokens.add(token);

                            palavra = "";
                        }
                    }
                }
                else if(digito == '.')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        String tipo = TipoDePalavra(palavra);
                        if(tipo.equals("int"))
                        {
                            palavra += digito;
                        }
                        else
                        {
                            token.setLinha(linha);
                            token.setColuna(i);
                            token.setClasse("ERRO");
                            token.setValor("X");
                            tokens.add(token);

                            palavra = "";
                        }
                    }
                }
                else if(digito == '!')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        palavra = "!";
                    }
                }
                else if(digito == '+')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_+");
                        token.setValor("+");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == '-')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_-");
                        token.setValor("-");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == '*')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_*");
                        token.setValor("*");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == '/')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_/");
                        token.setValor("/");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == '%')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_%");
                        token.setValor("%");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == '{')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("ABRE_CHAVE");
                        token.setValor("{");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == '}')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("FECHA_CHAVE");
                        token.setValor("}");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == '(')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("ABRE_PARENT");
                        token.setValor("(");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == ')')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("FECHA_PARENT");
                        token.setValor(")");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == ':')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("DOIS_PONTOS");
                        token.setValor(":");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == ';')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("PONTO_VIRGULA");
                        token.setValor(";");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else if(digito == ',')
                {
                    char[] auxilio = palavra.toCharArray();
                    if(palavra.length() == 1 && auxilio[0] == '"')
                    {
                        palavra +=digito;
                    }
                    else
                    {
                        PalavraAnterior(linha, palavra, i);
                    
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("VIRGULA");
                        token.setValor(",");
                        tokens.add(token);

                        palavra = "";
                    }
                }
                else
                {
                    palavra = NumeroOuLetra(digito, linha, palavra, i);
                }
            }
        }
        if(!palavra.equals(""))
        {
            PalavraAnterior(linha, palavra, tamanho);
        }
    }
    
    private String NumeroOuLetra(char digito, int linha, String palavra, int i)
    {
        Token token = new Token();
        char[] aux = palavra.toCharArray();
        
        if(Character.isDigit(digito) || Character.isLetter(digito))
        {
            if(palavra.length() == 1)
            {
                switch (aux[0]) {
                    case '+': 
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_+");
                        token.setValor("+");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '-':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_-");
                        token.setValor("-");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '*':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_*");
                        token.setValor("*");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '/':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_/");
                        token.setValor("/");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '%':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_ARIT_%");
                        token.setValor("%");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '<':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_REL_<");
                        token.setValor("<");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '>':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_REL_>");
                        token.setValor(">");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '=':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("ATRIBUICAO");
                        token.setValor("=");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '!':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_LOGICO_NAO_LOGICO");
                        token.setValor("!");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '^':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("OP_LOGICO_OU_EXCLUSIVO");
                        token.setValor("^");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '(':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("ABRE_PARENT");
                        token.setValor("(");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case ')':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("FECHA_PARENT");
                        token.setValor(")");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '{':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("ABRE_CHAVE");
                        token.setValor("{");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '}':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("FECHA_CHAVE");
                        token.setValor("}");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case ';':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("PONTO_VIRGULA");
                        token.setValor(";");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case ':':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("DOIS_PONTOS");
                        token.setValor(":");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case ',':
                        token.setLinha(linha);
                        token.setColuna(i);
                        token.setClasse("VIRGULA");
                        token.setValor(",");
                        tokens.add(token);
                        palavra = "" + digito;
                        break;
                    case '"':
                        palavra += digito;
                        break;
                    default: 
                        if(Character.isLetter(digito) || Character.isDigit(digito))
                        {
                            palavra += digito;
                        }
                        else
                        {
                            token.setLinha(linha);
                            token.setColuna(i);
                            token.setClasse("ERRO");
                            token.setValor("X");
                            tokens.add(token);
                        }
                        break;
                }
            }
            else
            {
                if(Character.isDigit(aux[0]) && Character.isLetter(digito))
                {
                    token.setLinha(linha);
                    token.setColuna(i);
                    token.setClasse("ERRO");
                    token.setValor("X");
                    tokens.add(token);
                    palavra = "";
                }
                else
                {
                    palavra += digito;
                }
            }
        }
        else
        {
            token.setLinha(linha);
            token.setColuna(i);
            token.setClasse("ERRO");
            token.setValor("X");
            tokens.add(token);
            palavra = "";
        }
        
        return palavra;
    }
    
    private void PalavraAnterior(int linha, String palavra, int i)
    {
        Token token = new Token();
        
        if(palavra.length() == 1)
        {
            if(palavra.equals("<"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_REL_<");
                token.setValor("<");
                tokens.add(token);
                return;
            }
            else if(palavra.equals(">"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_REL_>");
                token.setValor(">");
                tokens.add(token);
                return;
            }
            else if(palavra.equals("!"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_LOGICO_NAO_LOGICO");
                token.setValor("!");
                tokens.add(token);
                return;
            }
            else if(palavra.equals("="))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("ATRIBUICAO");
                token.setValor("=");
                tokens.add(token);
                return;
            }
        }
        
        String tipo = TipoDePalavra(palavra);
                    
        if(tipo.equals("int"))
        {
            token.setLinha(linha);
            token.setColuna(i);
            token.setClasse("NUM_INT");
            token.setValor(palavra);
            tokens.add(token);
        }
        else if(tipo.equals("float"))
        {
            token.setLinha(linha);
            token.setColuna(i);
            token.setClasse("NUM_FLOAT");
            token.setValor(palavra);
            tokens.add(token);
        }
        else if(tipo.equals("char"))
        {
            token.setLinha(linha);
            token.setColuna(i);
            token.setClasse("NUM_CHAR");
            token.setValor(palavra);
            tokens.add(token);
        }
        else if(tipo.equals("id"))
        {
            token.setLinha(linha);
            token.setColuna(i);
            token.setClasse("ID");
            token.setValor(palavra);
            tokens.add(token);
        }
        else
        {
            token.setLinha(linha);
            token.setColuna(i);
            token.setClasse("ERRO");
            token.setValor("X");
            tokens.add(token);
        }
    }
    
    private String Igual(char digito, int linha, String palavra, int i)
    {
        Token token = new Token();
        
        if (palavra.length() == 1)
        {
            if(palavra.equals("="))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_REL_==");
                token.setValor("==");
                tokens.add(token);
                palavra = "";
            }
            else if(palavra.equals(">"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_REL_>=");
                token.setValor(">=");
                tokens.add(token);
                palavra = "";
            }
            else if(palavra.equals("<"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_REL_<=");
                token.setValor("<=");
                tokens.add(token);
                palavra = "";
            }
            else if(palavra.equals("!"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_REL_!=");
                token.setValor("!=");
                tokens.add(token);
                palavra = "";
            }
            else
            {
                char[] auxilio = palavra.toCharArray();
                
                if(Character.isDigit(auxilio[0]))
                {
                    token.setLinha(linha);
                    token.setColuna(i);
                    token.setClasse("NUM_INT");
                    token.setValor("" + auxilio[0]);
                    tokens.add(token);
                }
                else if(Character.isLetter(auxilio[0]))
                {
                    token.setLinha(linha);
                    token.setColuna(i);
                    token.setClasse("NUM_CHAR");
                    token.setValor("" + auxilio[0]);
                    tokens.add(token);
                }
                palavra = "=";
            }
        }
        else if(palavra.length() > 1)
        {
            String tipo = TipoDePalavra(palavra);
            if(tipo.equals("float"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("NUM_FLOAT");
                token.setValor(palavra);
                tokens.add(token);
                palavra = "=";
            }
            else if(tipo.equals("int"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("NUM_INT");
                token.setValor(palavra);
                tokens.add(token);
                palavra = "=";
            }
            else if(tipo.equals("id"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("ID");
                token.setValor(palavra);
                tokens.add(token);
                palavra = "=";
            }
            else if(tipo.equals("char"))
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("NUM_CHAR");
                token.setValor(palavra);
                tokens.add(token);
                palavra = "=";
            }
            else
            {
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("ERRO");
                token.setValor("X");
                tokens.add(token);

                palavra = "=";
            }
        }
        return palavra;
    }
    
    private String TipoDePalavra(String palavra)
    {
        String tipo = "";
        
        char[] aux = palavra.toCharArray();
        
        int quantidadeLetras = 0, quantidadeNumero = 0, quantidadePonto = 0;
        
        if((palavra.length() == 3) && (aux[0] == '"') && (aux[2] == '"'))
        {
            tipo = "char";
        }
        else
        {
            for(int i = 0; i < palavra.length(); i++)
            {
                if(aux[i] == '.')
                {
                    quantidadePonto++;
                }
                else if(Character.isDigit(aux[i]))
                {
                    quantidadeNumero++;
                }
                else if(Character.isLetter(aux[i]))
                {
                    quantidadeLetras++;
                }
            }
            
            if(Character.isLetter(aux[0]))
            {
                tipo = "id";
            }
            else
            {
                if(quantidadePonto > 0)
                {
                    tipo = "float";
                }
                else
                {
                    tipo = "int";
                }
            }
        }
        
        return tipo;
    }
    
    private String primeiraLetra(char digito, int linha, String palavra, int i)
    {
        Token token = new Token();
        switch (digito) {
            case '+': 
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_ARIT_+");
                token.setValor("+");
                tokens.add(token);
                break;
            case '-':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_ARIT_-");
                token.setValor("-");
                tokens.add(token);
                break;
            case '*':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_ARIT_*");
                token.setValor("*");
                tokens.add(token);
                break;
            case '/':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_ARIT_/");
                token.setValor("/");
                tokens.add(token);
                break;
            case '%':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_ARIT_%");
                token.setValor("%");
                tokens.add(token);
                break;
            case '<':
                palavra += digito;
                break;
            case '>':
                palavra += digito;
                break;
            case '=':
                palavra += digito;
                break;
            case '!':
                palavra += digito;
                break;
            case '|':
                palavra += digito;
                break;
            case '&':
                palavra += digito;
                break;
            case '^':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("OP_LOGICO_OU_EXCLUSIVO");
                token.setValor("^");
                tokens.add(token);
                break;
            case '(':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("ABRE_PARENT");
                token.setValor("(");
                tokens.add(token);
                break;
            case ')':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("FECHA_PARENT");
                token.setValor(")");
                tokens.add(token);
                break;
            case '{':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("ABRE_CHAVE");
                token.setValor("{");
                tokens.add(token);
                break;
            case '}':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("FECHA_CHAVE");
                token.setValor("}");
                tokens.add(token);
                break;
            case ';':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("PONTO_VIRGULA");
                token.setValor(";");
                tokens.add(token);
                break;
            case ':':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("DOIS_PONTOS");
                token.setValor(":");
                tokens.add(token);
                break;
            case ',':
                token.setLinha(linha);
                token.setColuna(i);
                token.setClasse("VIRGULA");
                token.setValor(",");
                tokens.add(token);
                break;
            case '"':
                palavra += digito;
                break;
            default: 
                if(Character.isLetter(digito))
                {
                    palavra += digito;
                }
                else
                {
                    token.setLinha(linha);
                    token.setColuna(i);
                    token.setClasse("ERRO");
                    token.setValor("X");
                    tokens.add(token);
                }
                break;
        }
        return palavra;
    }
}
