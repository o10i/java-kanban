package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyList.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

/*    private void checkLengthOfHistoryList() {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
    }*/

    static class CustomLinkedList<E> {
        private Node<E> first;
        private Node<E> last;
        private int size = 0;
        private final Map<Integer, Node<E>> hashMap = new HashMap<>();

        void linkLast(E e) {
            final Node<E> l = last;
            final Node<E> newNode = new Node<>(l, e, null);
            last = newNode;
            if (l == null)
                first = newNode;
            else
                l.next = newNode;
            size++;
            hashMap.put(((Task) newNode.item).getId(), newNode);
        }

        ArrayList<E> getTasks() {
            ArrayList<E> arrayList = new ArrayList<>();
            for (Node<E> x = first; x != null; x = x.next) {
                arrayList.add(x.item);
            }
            return arrayList;
        }

        void removeNode(Node<E> node) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (node.item.equals(x.item)) {
                    unlink(x);
                    break;
                }
            }
        }

        private void unlink(Node<E> x) {
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
