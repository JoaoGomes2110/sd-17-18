/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

public class Jogo {
    
    private String nome;
    private Equipa casa;
    private Equipa fora;
     
    public Jogo (String nome){
        this.nome = nome;
        this.casa = new Equipa();
        this.fora = new Equipa();
    }
    
    public Jogo (Equipa casa, Equipa fora){
        this.casa = casa;
        this.fora = fora;
        
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public Equipa getEquipaCasa(){
        return this.casa;
    }
    
    public Equipa getEquipaFora(){
        return this.fora;
    }
    
    
    
     
    public Equipa simularJogo(){
        int rCasa =0;
        int rFora = 0;
        
        for(int i=0;i<this.casa.getQuantidade();i++){
            rCasa += this.casa.getHeroi(i).getOverall();
        }
        
        for(int i=0; i<this.fora.getQuantidade();i++){
            rFora += this.casa.getHeroi(i).getOverall();
        }
        
        if(rCasa>rFora){
            this.casa.atualizaRank();
            return this.casa;
        } else if(rFora>rCasa){
            this.fora.atualizaRank();
            return this.fora;
        } else{
            this.fora.atualizaRank();
            return this.fora;
        }
    }

    public String addJogador(Jogador jogador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getQuantidade() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
   
    
}
