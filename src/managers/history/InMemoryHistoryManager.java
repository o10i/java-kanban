package managers.history;

import managers.models.Node;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyList.hashMap.containsKey(id)) {
            historyList.removeNode(historyList.hashMap.get(id));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(historyList, that.historyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyList);
    }

    private static class CustomLinkedList<E extends Task> {
        private final Map<Integer, Node<E>> hashMap = new HashMap<>();
        private Node<E> first;
        private Node<E> last;
        private int size = 0;

        private void linkLast(E e) {
            int id = e.getId();
            if (hashMap.get(id) != null) {
                removeNode(hashMap.get(id));
            }
            final Node<E> l = last;
            final Node<E> newNode = new Node<>(l, e, null);
            last = newNode;
            if (l == null) {
                first = newNode;
            } else {
                l.next = newNode;
            }
            size++;

            hashMap.put(id, newNode);
        }

        private ArrayList<E> getTasks() {
            ArrayList<E> arrayList = new ArrayList<>();
            for (Node<E> x = first; x != null; x = x.next) {
                arrayList.add(x.item);
            }
            return arrayList;
        }

        private void removeNode(Node<E> x) {
            final E element = x.item;
            final Node<E> next = x.next;
            final Node<E> prev = x.prev;

            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.item = null;
            size--;
        }
    }
}