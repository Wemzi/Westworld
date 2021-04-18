package Model.Blocks;

import Model.People.Visitor;

import java.util.Queue;

/**
 * Marks classes where visitors can wait in a queue
 */
public interface Queueable {
    Queue<Visitor> getQueue();
    void addVisitor(Visitor v);
}
