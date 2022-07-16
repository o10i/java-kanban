package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyList.removeNode(historyList.hashMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    static class CustomLinkedList<E> {
        private final Map<Integer, Node<E>> hashMap = new HashMap<>();
        private Node<E> first;
        private Node<E> last;
        private int size = 0;

        void linkLast(E e) {
            int id = ((Task) e).getId();
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

        ArrayList<E> getTasks() {
            ArrayList<E> arrayList = new ArrayList<>();
            for (Node<E> x = first; x != null; x = x.next) {
                arrayList.add(x.item);
            }
            return arrayList;
        }

        void removeNode(Node<E> x) {
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
