//package org.example.backend.util.dataParsers;
//
//import com.example.dormitoryselection.domain.Student;
//import com.example.dormitoryselection.domain.StudentProfile;
//import com.example.dormitoryselection.domain.enums.Sex;
//import com.example.dormitoryselection.domain.enums.StudentType;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.Row;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class StudentParser implements DataParser<Student>{
//
//    @Override
//    public Student parse(List<String> heads, Row row) throws RuntimeException{
//        Student student = new Student();
//        StudentProfile studentProfile = new StudentProfile();
//        student.setProfile(studentProfile);
//        try {
//            for (int i = 0; i < heads.size(); ++i) {
//                switch (heads.get(i)) {
//                    case "id": student.setId((long) row.getCell(i).getNumericCellValue()); break;
//                    case "username": student.setUsername(row.getCell(i).getCellType().equals(CellType.STRING) ? row.getCell(i).getStringCellValue() : String.valueOf((long) row.getCell(i).getNumericCellValue())); break;
//                    case "password": student.setPassword(row.getCell(i).getCellType().equals(CellType.STRING) ? row.getCell(i).getStringCellValue() : String.valueOf((long) row.getCell(i).getNumericCellValue())); break;
//                    case "name": student.setName(row.getCell(i).getCellType().equals(CellType.STRING) ? row.getCell(i).getStringCellValue() : String.valueOf((long) row.getCell(i).getNumericCellValue())); break;
//                    case "sex": studentProfile.setSex(Sex.getInstance(row.getCell(i).getStringCellValue())); break;
//                    case "type": studentProfile.setType(StudentType.getInstance(row.getCell(i).getStringCellValue())); break;
//                    case "selfintroduction": studentProfile.setSelfIntroduction(row.getCell(i).getStringCellValue()); break;
//                    case "tag": studentProfile.setTags(row.getCell(i).getStringCellValue()); break;
//                    default:
//                }
//            }
//            return student;
//        } catch (Exception e) {
//            throw new RuntimeException("Parse student failed");
//        }
//    }
//}
