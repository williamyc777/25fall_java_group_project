//package org.example.backend.util.dataParsers;
//
//import com.example.dormitoryselection.domain.Building;
//import com.example.dormitoryselection.domain.Room;
//import com.example.dormitoryselection.domain.enums.RoomType;
//import com.example.dormitoryselection.service.BuildingService;
//import org.apache.poi.ss.usermodel.Row;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class RoomParser implements DataParser<Room> {
//    @Autowired
//    public BuildingService buildingService;
//    @Override
//    public Room parse(List<String> heads, Row row) throws RuntimeException {
//        Room room = new Room();
//        try {
//            for (int i = 0; i < heads.size(); ++i) {
//                switch (heads.get(i)) {
//                    case "id": room.setId((long) row.getCell(i).getNumericCellValue()); break;
//                    case "roomfloor": room.setRoomFloor((int) row.getCell(i).getNumericCellValue()); break;
//                    case "roomnumber": room.setRoomNumber(String.valueOf((int) row.getCell(i).getNumericCellValue())); break;
//                    case "roomtype": {
//                        room.setRoomType(RoomType.getInstance(row.getCell(i).getStringCellValue()));
//                        room.setRoomCapacity(room.getRoomType().ordinal() + 1);
//                        room.setRemainCapacity(room.getRoomCapacity());
//                        break;
//                    }
//                    case "roomcapacity": {
//                        room.setRoomCapacity((int) row.getCell(i).getNumericCellValue());
//                        room.setRoomType(RoomType.values()[room.getRoomCapacity() - 1]);
//                        room.setRemainCapacity(room.getRoomCapacity());
//                        break;
//                    }
//                    case "roomdescription": room.setRoomDescription(row.getCell(i).getStringCellValue()); break;
//                    case "building": {
//                        Building building = buildingService.findBuildingByBuildingName(row.getCell(i).getStringCellValue());
//                        if (building == null) {
//                            throw new RuntimeException();
//                        }
//                        room.setBuilding(building);
//                        break;
//                    }
//                    default:
//                }
//            }
//            return room;
//        } catch (Exception e) {
//            throw new RuntimeException("Room parse failed");
//        }
//    }
//}
