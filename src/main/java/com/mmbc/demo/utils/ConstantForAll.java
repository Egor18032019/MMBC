package com.mmbc.demo.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConstantForAll {
    private static final Path path = Paths.get(System.getProperty("user.dir") + "/fileStorage");
}
