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
public class Barreira {
    
    private int maxElem;
    private int counterElem;
    private int counterEtapa;

    public Barreira(int n){
        this.maxElem=n;
        this.counterElem=0;
	this.counterEtapa=0;
    }

    public int getEtapa(){
        return counterEtapa;
    }

    public synchronized void esperar(){
        this.counterElem++;

	int cur_etapa = this.counterEtapa;

	
	if(this.counterElem==this.maxElem){
            this.notifyAll();
            this.counterEtapa += 1;
            this.counterElem = 0;
	}
	
        else{ 
            while(cur_etapa == this.counterEtapa){
		System.out.println("A barreira tem " + this.counterElem + "/"+this.maxElem+" elementos na etapa "+counterEtapa+" -> Thread "+Thread.currentThread().getName()+" espera");
                try {
                    this.wait();
		} catch (InterruptedException e) {
                    e.printStackTrace();
		}
                
            }
	}
    }
}
