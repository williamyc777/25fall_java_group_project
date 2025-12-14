package org.example.backend.util.dataParsers;

import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public interface DataParser <T>{
    T parse(List<String> heads, Row row) throws RuntimeException;
}
