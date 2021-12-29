package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View; //базовый класс для создания элементов пользовательского интерфейса
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.io.BufferedReader;//это подкласс Reader, который используется для упрощения чтения текста из потоков ввода символов
import java.io.InputStreamReader;//для конвертирования байтов в символы
import java.io.OutputStream; //для записи байтов в целевой объект
import java.net.HttpURLConnection;//для указания веб-сайта
import java.net.URL; // используется для запроса ресурсов в Интернете,метод запроса - это метод GET
import java.nio.charset.StandardCharsets;// класс определяет константы для каждого из стандартных наборов символов
import android.os.Bundle; //хранит состояние activity


//Обработка события нажатия с помощью интерфейса OnClickListener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start;
    EditText pulse1;
    EditText pulse2;
    EditText year;
    Spinner month;
    Spinner day;
    Spinner sex;
    int onePulse, twoPulse, selected_day, threeYear;
    int finalMonth = -1;
    String selected_sex;
    String[] result = {"good","norm", "bad", "error"};
    @Override //переопределения методов
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //обращение к суперклассу, расширение метода родительского класса
        setContentView(R.layout.activity_main); //заполнения окна с помощью пользовательского интерфейса
        start = findViewById(R.id.start);
        start.setOnClickListener(this); //обработчик событий
        pulse1 = findViewById(R.id.pulse1);
        pulse2 = findViewById(R.id.pulse2);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
        sex = findViewById(R.id.sex);


    }

    public void number_sex(){ //метод, чтобы вместо М и Ж записать 1 или 2
        if (selected_sex.equals("М")){
            selected_sex = "1";
        } else {
            selected_sex = "2";
        }
    }

    @Override
    public void onClick(View view) {
        onePulse = Integer.parseInt(String.valueOf(pulse1.getText()));
        twoPulse = Integer.parseInt(String.valueOf(pulse2.getText()));
        threeYear = Integer.parseInt(String.valueOf(year.getText()));
        selected_day = Integer.parseInt(day.getSelectedItem().toString());
        selected_sex = sex.getSelectedItem().toString();
        finalMonth = (int) (month.getSelectedItemId()+1);
        number_sex();
        getThread();

    }
    public void getThread() {
        Thread thread = new Thread(new Runnable() { //создание потока
            @Override
            public void run() {
                try {
                    //объект класса java.net.URL
                    URL url = new URL("http://abashin.ru/cgi-bin/ru/tests/burnout");
                    //метод openConnection() возвращает HttpUrlConnection.
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //определние типа запроса
                    //POST предназначен для запроса, при котором веб-сервер принимает данные, заключённые в тело сообщения
                    connection.setRequestMethod("POST");
                    //общие свойства запроса изменяются, используя метод setRequestProperty
                    connection.setRequestProperty("Host", "abashin.ru");
                    //остаётся ли сетевое соединение активным после завершения текущей транзакции
                    connection.setRequestProperty("Connection", "close");
                    // предпочтительные форматы
                    connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3);q=0.9");
                    //Сообщает допустимые языки содержания
                    connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
                    // указания типа ресурса.
                    // Формат кодирования application/x-www-form-urlencoded（Соедините параметры пары ключ-значение с &)
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    //параметры которые отправляем
                    String parameters = "day=" + selected_day + "&month=" + finalMonth + "&year=" + threeYear + "&sex=" + selected_sex + "&m1=" + onePulse+ "&m2=" + twoPulse;
                    //размер отправленного получателю тела объекта
                    connection.setRequestProperty("Content-Length", String.valueOf(parameters.length()));
                    connection.setDoInput(true); //отправляем запрос
                    connection.setDoOutput(true);//получаем данные
                    OutputStream outputStream = connection.getOutputStream(); //передаем данные, которые записаны в потоке
                    //записываем тело запроса write()
                    outputStream.write(parameters.getBytes(StandardCharsets.UTF_8));
                    //запись закончилась
                    outputStream.close();
                    // Читает текст из потока ввода символов
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String infdata;
                    // изменяемый класс с временными данными
                    StringBuilder response = new StringBuilder();
                    // читает строку текста пока не null
                    while ((infdata = bufferedReader.readLine()) != null) {
                        response.append(infdata);
                    }
                    // закрытие потока
                    bufferedReader.close();
                    //Response - текущий ответ, replaceAll метод позволяет заменить слово в строке.
                    String parsedData = new String(response.toString().getBytes(), StandardCharsets.UTF_8).replaceAll("<.*?>", ""); //Регулярное выражение, замена
                    //System.out.println(parsedData);
                    String finalRez;
                    if (parsedData.contains("отсутствию переутомления")) {
                        finalRez = result[0];
                    } else if (parsedData.contains("небольшому переутомлению")) {
                        finalRez = result[1];
                    } else if (parsedData.contains("высокому уровню переутомления")) {
                        finalRez = result[2];
                    } else {
                        finalRez = result[3];
                    }
                    Intent intent = new Intent("com.res.myapplication.action.mainactivity2");
                    //putExtra()добавляет ключ и связанное с ним значение
                    intent.putExtra("result", finalRez);
                    startActivity(intent);
                } catch (Exception exception) { //Перехват исключений
                    exception.printStackTrace(); //выявление причины проблемы
                }

            }

        });
        thread.start();// запустить поток
    }
}