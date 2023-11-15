package com.mmbc.demo.schemas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileChangeRequest {
    int width; // : int,  - Чётное число больше 20
    int height;// : int,  - Чётное число больше 20
}
