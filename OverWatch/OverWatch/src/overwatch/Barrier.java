/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overwatch;

/**
 *
 * @author
 */
public class Barrier {
    
    private int maxElem;
    private int counterElem;
    private int counterStage;

    public Barrier(int n){
        this.maxElem=n;
        this.counterElem=0;
	this.counterStage=0;
    }

    public int getStage(){
        return counterStage;
    }

    public synchronized void waiting(){
        this.counterElem++;

	int cur_etapa = this.counterStage;

	
	if(this.counterElem==this.maxElem){
            this.notifyAll();
            this.counterStage += 1;
            this.counterElem = 0;
	}
	
        else{ 
            while(cur_etapa == this.counterStage){
                try {
                    this.wait();
		} catch (InterruptedException e) {
                    e.printStackTrace();
		}
                
            }
	}
    }
}
