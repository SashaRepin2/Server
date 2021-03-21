import java.util.Date;

public class Message {
    // Поля
    private String dateSend;  // Дата отправки
    private String sender;  // Отправитель
    private String text;    // Текст сообщений


    // Свойства
    // Получаем текст сообщения
    public String getText() {
        return text;
    }

    // Получаем отправителя сообщения
    public String getSender() {
        return sender;
    }

    // Получаем дату отправки
    public String getDateSend() {
        return dateSend;
    }

    public void setDateSend(String dateSend){
        this.dateSend = dateSend.toString();
    }

    // Констурктор без параметров
    public Message() {
        this.text = null;
        this.sender = null;
        this.dateSend = null;
    }

    // Конструктор с параметрами - текст сообщения, отправитель
    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.dateSend = null;
    }


}
