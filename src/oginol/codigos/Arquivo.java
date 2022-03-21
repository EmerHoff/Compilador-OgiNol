/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oginol.codigos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vilson
 */
public class Arquivo {
    private String endereco;
    private List<Token> tokens;

    public Arquivo() {
        this.endereco = "";
        this.tokens = new ArrayList<>();
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
    
    public void Salvar()
    {
        FileWriter arq;
        try {
            arq = new FileWriter(endereco);
            
            PrintWriter gravarArq = new PrintWriter(arq);
        
            for (int i = 0; i < tokens.size(); i++)
            {
                gravarArq.printf("{%s} - Valor: %s%n", tokens.get(i).getClasse(), tokens.get(i).getValor());
            }

            arq.close();
            System.out.println("Arquivo gravado com sucesso");
        } catch (IOException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
