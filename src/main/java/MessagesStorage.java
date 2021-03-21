import java.util.LinkedList;
import java.util.List;

public class MessagesStorage {

    // Список сообщений
    private List<Message> storage;

    // Конструктор
    public MessagesStorage() {
        storage = new LinkedList<>();
    }

    // Методы
    // Добавить сообщение в "хранилище"
    public void AddMessage(Message msg) {
        storage.add(msg);
    }

    // Получить все сообщения из "хранилища"
    public List<Message> getMessages() {
        return storage;
    }

    // Получить сообщение по index(id)
    public Message getMessage(int index) {
        return storage.get(index);
    }
}
