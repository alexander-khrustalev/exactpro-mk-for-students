/*
 * Простое сообщение чата, содержащее толко поле text
 */
public class ChatMessage {
    private String text;

    public String getText() {
        return text;
    }

    /*
     * Такой пустой конструктор нужен для того, чтобы библиотека смогла построить JSON из объекта и наоборот
     */
    private ChatMessage() {
        text = null;
    }
}
