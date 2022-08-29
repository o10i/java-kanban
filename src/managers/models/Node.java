package managers.models;

public class Node<E> {
    public E item;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, E element, Node<E> next) {
        this.prev = prev;
        this.item = element;
        this.next = next;
    }
}