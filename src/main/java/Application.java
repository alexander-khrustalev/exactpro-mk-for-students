import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.sse.SseClient;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Application {
    public static void main(String[] args) {
        //  Создаем экземпляр сервиса
        //  JavalinConfig::enableCorsForAllOrigins нужен для доступа с другого домена
        Javalin app = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(8080);

//        Коллекция, содержащая информацию о клиентах подписки
        Queue<SseClient> clients = new ConcurrentLinkedQueue<>();

//          Простой обработчик get-запроса на url '/hello'
        app.get("/hello", ctx -> ctx.result("Hello, world!"));

        /*
         * Обработчик post-запроса на url '/send-simple-message'
         * Получает реквест и выводит его данные
         */
        app.post("/send-simple-message", ctx -> {
            System.out.println("String has been received: " + ctx.body());
        });

        ChatStorage storage = new ChatStorage();

        /*
         * Обработчик post-запроса на url '/send-message'
         * Получает реквест, пытается сериализовать содержимое реквеста и сохранить в хранилище
         */
        app.post("/send-message", ctx -> {
            ChatMessage message = ctx.bodyAsClass(ChatMessage.class);
            storage.addMessage(message);

//            Отсылаем полученное сообщение всем подключенным клиентам
            clients.forEach(client -> client.sendEvent("message", ctx.body()) );
        });

        /*
         * Обработчик get-запроса на url '/messages'
         * Возвращает список сообщений из хранилища
         */
        app.get("/messages", ctx -> ctx.json(storage.getMessages()));

        /*
         * Обработчик get-запроса на url '/message'
         * Возвращает сообщение с указанным индексом
         * Если элемента с таким индексом нет, то вернем 404 ошибку
         */
        app.get("/message", ctx -> {
            Integer index = ctx.queryParam("index", Integer.class).get();
            try {
                ctx.json(storage.getMessage(index));
            } catch (IndexOutOfBoundsException exc) {
                ctx.status(404);
            }
        });

        /*
        * Пример обработки sse-подписки
        */
        app.sse("/sse", client -> {
            clients.add(client);
            client.sendEvent("connected", "Test sse");
            client.onClose(() -> System.out.println("client disconnected"));
        });
    }
}
