package org.bigorange.game.dialogue;

import com.badlogic.gdx.utils.Array;

public class ConversationGraphSubject {
    private Array<ConversationGraphObserver> observers;

    public ConversationGraphSubject(){
        observers = new Array<>();
    }

    public void addObserver(ConversationGraphObserver observer){
        observers.add(observer);
    }

    public void removeAllObservers(){
        for (ConversationGraphObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    public void notify(final ConversationGraph graph, ConversationGraphObserver.ConversationCommandEvent event){
        for (ConversationGraphObserver observer : observers) {
            observer.onNotify(graph, event);
        }
    }
}
