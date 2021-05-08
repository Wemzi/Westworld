package Model.Blocks;

import Model.People.Visitor;

import java.util.Queue;

/**
 * Olyan osztályokat jelöl, amelyeknél sorba lehet állni.
 */
public interface Queueable {
    Queue<Visitor> getQueue();
    void addVisitor(Visitor v);
}
