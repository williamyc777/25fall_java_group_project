//package org.example.backend.util;
//
//import com.example.dormitoryselection.util.dataParsers.DataParser;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Spliterator;
//import java.util.logging.Logger;
//
//@Component
//public class ExcelParserUtil {
//    private static final Logger LOGGER = Logger.getLogger(ExcelParserUtil.class.getName());
//
////    public static <T> T parseRow2Object(List<String> heads, Row row, Class<T> clazz) throws ParseException {
////        T object;
////        try {
////            object = clazz.getConstructor().newInstance();
////        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
////            e.printStackTrace();
////            return null;
////        }
////
////        for (Cell cell: row) {
////            StringBuilder head = new StringBuilder(heads.get(cell.getColumnIndex()));
////            if (head.isEmpty()) {
////                continue;
////            }
////            head.setCharAt(0, Character.toUpperCase(head.charAt(0)));
////            try {
////                if (cell.getCellType() == CellType.NUMERIC) {
////                    clazz.getMethod("set" + head, String.class).invoke(object, String.valueOf((long) cell.getNumericCellValue()));
////                } else {
////                    if(head.toString().equals("Sex")){
////                        String temp = cell.getStringCellValue();
////                        if (temp.equals("Male")) {
////                            clazz.getMethod("set" + head, Sex.class).invoke(object, Sex.Male);
////                        } else if (temp.equals("Female")) {
////                            clazz.getMethod("set" + head, Sex.class).invoke(object, Sex.Female);
////                        } else {
////                            throw new ParseException(temp + " Failed to parse Sex", 0);
////                        }
////                    } else if (head.toString().equals("StudentType")) {
////                        String temp = cell.getStringCellValue();
////                        if (temp.equals("Doctoral")) {
////                            clazz.getMethod("set" + head, StudentType.class).invoke(object, StudentType.Doctoral);
////                        } else if (temp.equals("Master")) {
////                            clazz.getMethod("set" + head, StudentType.class).invoke(object, StudentType.Master);
////                        } else {
////                            throw new ParseException(temp + " Failed to parse StudentType", 0);
////                        }
////                    } else {
////                        clazz.getMethod("set" + head, String.class).invoke(object, cell.getStringCellValue());
////                    }
////                }
////            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
////                e.printStackTrace();
////            }
////        }
////        return object;
////    }
////
////    public static <T> List<T> parseExcel2Objects(MultipartFile file, Class<T> clazz){
////        Workbook workbook;
////        try {
////            workbook = WorkbookFactory.create(file.getInputStream());
////        } catch (IOException e) {
////            return null;
////        }
////        List<T> list = new ArrayList<>();
////        Spliterator<Row> spliterator = workbook.getSheetAt(0).spliterator();
////        List<String> heads = new ArrayList<>();
////        spliterator.tryAdvance(row -> {
////            row.cellIterator().forEachRemaining(cell -> heads.add(cell.getStringCellValue()));
////        });
////        spliterator.forEachRemaining(row -> {
////            try {
////                list.add(parseRow2Object(heads, row, clazz));
////            } catch (ParseException e) {
////                e.printStackTrace();
////            }
////        });
////        return list;
////    }
//
//    public static <T> List<T> parseExcel2Objects(MultipartFile file, DataParser<T> parser){
//        Workbook workbook;
//        try {
//            workbook = WorkbookFactory.create(file.getInputStream());
//        } catch (IOException e) {
//            return null;
//        }
//        List<T> list = new ArrayList<>();
//        Spliterator<Row> spliterator = workbook.getSheetAt(0).spliterator();
//        List<String> heads = new ArrayList<>();
//        spliterator.tryAdvance(row -> {
//            row.cellIterator().forEachRemaining(cell -> heads.add(cell.getStringCellValue().toLowerCase()));
//        });
//        spliterator.forEachRemaining(row -> {
//            try {
//                list.add(parser.parse(heads, row));
//            } catch (Exception e) {
//                LOGGER.severe(e.getMessage());
//                LOGGER.severe("ParseExcel2Objects failed");
//            }
//        });
//        return list;
//    }
//}
