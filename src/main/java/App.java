
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.sse.SseClient;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONObject;


import static j2html.TagCreator.*;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class App {

    // Создаем временное храниище сообщений
    private static MessagesStorage msgStorage = new MessagesStorage();

    // Храним сессии клиентов
    private static Queue<SseClient> clients = new ConcurrentLinkedQueue<>();

    // Порт, который "слушает" сервер
    private static int port = 8080;

    private static DataBase dataBase;

    public static void main(String[] args) {

        try {
            // Создаем базу данных
            dataBase = new DataBase();

            // Создаем и запускаем сервер
            Javalin app = Javalin.create(config -> {
                config.enableCorsForAllOrigins();
            }).start(port);


            app.post("/auth-user", ctx -> {

                User user = ctx.bodyAsClass(User.class);


                if (user.getLogin().length() < 3 || user.getPassword().length() < 3) {
                    System.out.println("Некорректная длина");
                    ctx.status(500);
                    return;

                }
                int uid = user.getLogin().hashCode() + user.getPassword().hashCode();

                System.out.printf("New user has been added:\n" +
                                " uid: %d\n" +
                                " login: %s\n" +
                                " password: %s\n",
                        uid, user.getLogin(), user.getPassword());

                dataBase.insert(uid, user.getLogin(), user.getPassword());


            });

            app.get("/users", ctx -> ctx.result("dsad")
            );

            // Отправка сообщения
            app.post("/send-message", ctx -> {

                System.out.println(ctx.body());
                ctx.body();
                // Десирилизация json
                Message message = ctx.bodyAsClass(Message.class);

                // Добавляем сообщение в хранилище
                msgStorage.AddMessage(message);

                // Выводим полученное сообщение в консоль
                System.out.printf("-------------\n" +
                        "Date: %s\n" +
                        "Sender: %s\n" +
                        "Message received: %s\n" +
                        "-------------\n", message.getDateSend(), message.getSender(), message.getText()
                );

                // Отправляем каждому клиенту сессии событие
                clients.forEach(client -> {
                    client.sendEvent("message", ctx.body());
                });
            });

            // Получаем сообщения из хранилища
            app.get("/storage-messages", ctx -> ctx.json(msgStorage.getMessages()));

            // Получаем сообщение по его индексу (id)
            app.get("/message", ctx -> {
                try {
                    // Получаем индекс сообщения
                    Integer index = ctx.queryParam("index", Integer.class).get();

                    // Находим нужное сообщение по индексу
                    ctx.json(msgStorage.getMessage(index));

                } catch (IndexOutOfBoundsException ex) {
                    ctx.status(404);
                }
            });


            app.sse("sse", newClient -> {
                // Добавляем клиента
                clients.add(newClient);

                // Отправляем событие, что новый пользователь подключился
                clients.forEach(client -> {
                    client.sendEvent("connected", "Client connected");
                    Gson gson = new Gson();
                    String jsonList = gson.toJson(msgStorage.getMessages());
                    client.sendEvent("getMessages", jsonList);
                });

                newClient.onClose(() -> {
                    System.out.println("Client disconnected");
                });

                System.out.println(clients.toString());
            });

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}

