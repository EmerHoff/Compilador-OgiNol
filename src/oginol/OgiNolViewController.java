/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oginol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import oginol.codigos.*;

/**
 * FXML Controller class
 *
 * @author Vilson
 */
public class OgiNolViewController implements Initializable {

    @FXML
    private Button buttonAbrir;

    @FXML
    private Button buttonCompilar;
    
    String enderecoPasta;
    String enderecoCompleto;
    
    @FXML
    void abrirAction(ActionEvent event) {
        FileChooser open = new FileChooser();
        open.setTitle("Selecione o Arquivo");
        FileChooser.ExtensionFilter extensoes = new FileChooser.ExtensionFilter("Arquivos OgiNol", "*.txt");
        open.getExtensionFilters().add(extensoes);
        File end = open.showOpenDialog(null);
        
        String endereco = end.getPath();
        System.out.println("Endereco: " + endereco);
        
        String nome = end.getName();
        int tamanho = endereco.length() - nome.length();
        String enderecoP = endereco.substring(0, tamanho);
        System.out.println("EnderecoPasta: " + enderecoP);
        
        enderecoCompleto = endereco;
        enderecoPasta = enderecoP;
    }

    @FXML
    void actionCompila(ActionEvent event) {
        List<String> arqCod = new ArrayList<>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(enderecoCompleto));
            
            while(br.ready()){
                String linha = br.readLine();
                arqCod.add(linha);
            }
            
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        AnalisadorLexico lexico = new AnalisadorLexico();
        lexico.setEndereco(enderecoPasta);
        lexico.setEnderecoCompleto(enderecoCompleto);
        
        lexico.reconhecedor(arqCod);
        System.out.println("--------------------------");
        String enderecoNovo = enderecoPasta + "\\arquivoAnalisadorLexico.txt";
        Arquivo arq = new Arquivo();
        arq.setTokens(lexico.getTokens());
        arq.setEndereco(enderecoNovo);
        arq.Salvar();
        System.out.println("----------------------------------");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
