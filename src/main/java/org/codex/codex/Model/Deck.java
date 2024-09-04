package org.codex.codex.Model;

import java.util.ArrayList;

public class Deck<T> {
    private ArrayList<T> deck;

    public Deck() {this.deck = new ArrayList<T>();}

    public ArrayList<T> get() {return this.deck;}
    public T getCard(int index) throws IndexOutOfBoundsException {return this.deck.get(index);}

    public void addCard(T card) {this.deck.add(card);}
    public void addCard(int index, T card) {this.deck.add(index, card);}

    public void shuffle() { // Shuffle the deck
        int randomIndex; int i = this.deck.size() - 1; T temp;
        while (i >= 0) {
            randomIndex = (int)Math.floor(Math.random() * i); temp = this.deck.get(i);
            this.deck.set(i, this.deck.get(randomIndex)); this.deck.set(randomIndex, temp);
            i--;
        }
    }

}
