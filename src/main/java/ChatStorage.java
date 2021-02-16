import java.util.LinkedList;
import java.util.List;

/*
 * Имитация хранилища
 */
public class ChatStorage {
    /*
     * Список сообщений
     */
    private List<ChatMessage> messages = new LinkedList<>();

    /*
     * Метод, добавляющий сообщение в хранилище
     */
    public void addMessage(ChatMessage message) {
        messages.add(message);
    }

    /*
     * Метод, извлекающий сообщения из хранилища
     */
    public List<ChatMessage> getMessages() {
        return messages;
    }

    /*
     * Метод, извлекающий одно сообщение из хранилища
     */
    public ChatMessage getMessage(Integer index) {
        return messages.get(index);
    }
}
