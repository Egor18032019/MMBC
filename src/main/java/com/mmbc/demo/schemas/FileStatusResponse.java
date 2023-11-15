package com.mmbc.demo.schemas;

public class FileStatusResponse {
        //TODO в чем разница между id и именем файла ? id это идишник из бд 
    String id;//: uid,
    String filename;
    Boolean processing;//- идёт ли процесс обработки
    String processingSuccess;// Enum сделать: null | true | false  - отображает успешность последней операции над видео. Дефолтное значение null.

}
