/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

/**
 *
 * @author tiagofraga
 */
public class Equipa {
    
    
    private Jogador[] jogadores;
    private Heroi[] avatares;
    private int quantidade;
    
    public Equipa(){
        this.jogadores = new Jogador[5];
        this.avatares = new Heroi[5];
        this.quantidade = 0;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void addJogador(Jogador j) {
        this.jogadores[quantidade]=j;
        this.quantidade++;
    }

    public Jogador getJogador(int i){
        return this.jogadores[i];
    }
    
    public Heroi getHeroi(int i) {
        return this.avatares[i];
    }
    
    public void atualizaRank(){
        for(int i=0; i<this.quantidade; i++){
            this.jogadores[i].updateRank();
        }
    }
    
}
