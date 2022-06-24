package org.bigorange.game.ui;

public interface StatusSubject {
    void addObserver(StatusObserver statusObserver);
    void removeObserver(StatusObserver statusObserver);
    void removeAllObservers();
    void notify(final int value, StatusObserver.StatusEvent event);
}
