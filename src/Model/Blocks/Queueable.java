package Model.Blocks;

import Model.People.Visitor;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Marks classes where visitors can wait in a queue
 */
public interface Queueable {
    ArrayBlockingQueue<Visitor> getQueue();
}
