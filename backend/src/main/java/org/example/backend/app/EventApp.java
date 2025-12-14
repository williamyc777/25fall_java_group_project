package org.example.backend.app;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.backend.config.MyException;
import org.example.backend.domain.*;
import org.example.backend.dto.DefinedFormDto;
import org.example.backend.dto.EventBriefDto;
import org.example.backend.dto.EventDto;
import org.example.backend.dto.EventPostDto;
import org.example.backend.service.AbstractEnrollmentService;
import org.example.backend.service.AbstractUserService;
import org.example.backend.service.EventService;
import org.example.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于处理活动相关的请求
 * @author wangyr
 * @version 1.0
 */
@RestController
@RequestMapping("/event")
public class EventApp {
    @Autowired
    EventService eventService;
    @Autowired
    AbstractEnrollmentService abstractEnrollmentService;
    @Autowired
    org.example.backend.api.EnrollFormRepository enrollFormRepository;
    @Autowired
    org.example.backend.service.AbstractUserService abstractUserService;
    @Autowired
    jakarta.persistence.EntityManager entityManager;

    /**
     * @param token 用户token
     * @param eventPostDto 活动信息
     * @return 是否创建成功
     */
    @PostMapping("/create")
//    public boolean releaseEvent(@RequestHeader("Authorization") String token, @RequestParam String title, @RequestParam String name, @RequestParam String enrollmentType, @RequestParam String applyStartTime, @RequestParam String applyEndTime, @RequestParam String startTime, @RequestParam String endTime, @RequestParam String imageUrl, @RequestParam String introduction, @RequestParam String mdText, @RequestParam(required = false) long limitCount, @RequestParam(required = false) List<DefinedFormDto> definedForm) {
    public boolean releaseEvent(@RequestHeader("Authorization") String token, @RequestBody EventPostDto eventPostDto) {
        System.out.println("=== releaseEvent Start ===");
        User user = (User) JwtUtil.verifyToken(token);
        System.out.println("releaseEvent - User ID: " + user.getId());
        System.out.println("releaseEvent - Username: " + user.getUsername());
        
        Permission permission = user.getPermission();
        if (permission == null) {
            System.out.println("releaseEvent - ERROR: User has no permission!");
            throw new MyException(-1, "Permission denied: No permission found");
        }
        
        System.out.println("releaseEvent - Permission - canCreate: " + permission.isCanCreate() + ", canEnroll: " + permission.isCanEnroll() + ", canComment: " + permission.isCanComment());
        
        if (!permission.isCanCreate()) {
            System.out.println("releaseEvent - Permission denied: User cannot create events");
            throw new MyException(-1, "Permission denied: User cannot create events");
        }
        
        System.out.println("releaseEvent - Permission check passed");
        Event event = new Event();
        event.setTitle(eventPostDto.getTitle());
        event.setName(eventPostDto.getName());
        event.setAuthor(user);
        event.setIntroduction(eventPostDto.getIntroduction());
        event.setPosterUrl(eventPostDto.getImageUrl());
        event.setText(eventPostDto.getMdText());
        
        // 解析日期时间，添加错误处理
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println("Parsing startTime: " + eventPostDto.getStartTime());
            event.setStartTime(LocalDateTime.parse(eventPostDto.getStartTime(), formatter));
            System.out.println("Parsing endTime: " + eventPostDto.getEndTime());
            event.setEndTime(LocalDateTime.parse(eventPostDto.getEndTime(), formatter));
        } catch (Exception e) {
            System.err.println("Failed to parse event times: " + e.getMessage());
            System.err.println("StartTime: " + eventPostDto.getStartTime());
            System.err.println("EndTime: " + eventPostDto.getEndTime());
            throw new MyException(-1, "日期时间格式错误: " + e.getMessage() + ". 格式应为: yyyy-MM-dd HH:mm:ss");
        }
        
        switch (eventPostDto.getEnrollmentType()) {
            case "count":
                CountEnrollment countEnrollment = new CountEnrollment();
                try {
                    System.out.println("Parsing applyStartTime: " + eventPostDto.getApplyStartTime());
                    countEnrollment.setStartTime(LocalDateTime.parse(eventPostDto.getApplyStartTime(), formatter));
                    System.out.println("Parsing applyEndTime: " + eventPostDto.getApplyEndTime());
                    countEnrollment.setEndTime(LocalDateTime.parse(eventPostDto.getApplyEndTime(), formatter));
                } catch (Exception e) {
                    System.err.println("Failed to parse enrollment times: " + e.getMessage());
                    throw new MyException(-1, "报名时间格式错误: " + e.getMessage());
                }
                countEnrollment.setCapacity(eventPostDto.getLimitCount());
                countEnrollment.setEvent(event);
                event.setAbstractEnrollment(countEnrollment);
                break;
            case "form":
                FormEnrollment formEnrollment = new FormEnrollment();
                try {
                    System.out.println("Parsing applyStartTime: " + eventPostDto.getApplyStartTime());
                    formEnrollment.setStartTime(LocalDateTime.parse(eventPostDto.getApplyStartTime(), formatter));
                    System.out.println("Parsing applyEndTime: " + eventPostDto.getApplyEndTime());
                    formEnrollment.setEndTime(LocalDateTime.parse(eventPostDto.getApplyEndTime(), formatter));
                } catch (Exception e) {
                    System.err.println("Failed to parse enrollment times: " + e.getMessage());
                    throw new MyException(-1, "报名时间格式错误: " + e.getMessage());
                }
                formEnrollment.setDefinedFormEntries(eventPostDto.getDefinedForm().stream().map(DefinedFormDto::toDefinedFormEntry).toList());
                formEnrollment.setEvent(event);
                event.setAbstractEnrollment(formEnrollment);
                break;
        }
        
        System.out.println("=== Event Creation Start ===");
        System.out.println("releaseEvent - User ID: " + user.getId());
        System.out.println("releaseEvent - User class: " + user.getClass().getSimpleName());
        System.out.println("releaseEvent - Event title: " + event.getTitle());
        System.out.println("releaseEvent - Event name: " + event.getName());
        System.out.println("releaseEvent - Event author (before save): " + (event.getAuthor() != null ? event.getAuthor().getId() : "null"));
        
        boolean saved = eventService.saveEvent(event);
        
        System.out.println("releaseEvent - Event saved: " + saved);
        System.out.println("releaseEvent - Event ID: " + event.getId());
        
        // 重新查询验证
        Event savedEvent = eventService.findEventById(event.getId());
        if (savedEvent != null) {
            System.out.println("releaseEvent - Verified event exists");
            System.out.println("releaseEvent - Saved event author: " + (savedEvent.getAuthor() != null ? savedEvent.getAuthor().getId() : "null"));
            System.out.println("releaseEvent - Saved event author class: " + (savedEvent.getAuthor() != null ? savedEvent.getAuthor().getClass().getSimpleName() : "null"));
            
            // 测试查询
            List<Event> testQuery = eventService.findEventByAuthorId(user.getId());
            System.out.println("releaseEvent - Test query by author ID " + user.getId() + " found " + testQuery.size() + " events");
        } else {
            System.out.println("releaseEvent - ERROR: Event not found after save!");
        }
        System.out.println("=== Event Creation End ===");
        
        return saved;
    }

    /**
     * @return 所有活动ID
     */
    @GetMapping("/all")
    public long[] getAllEvents() {
        return eventService.findAllEvents().stream().mapToLong(Event::getId).toArray();
    }

    /**
     * @param token 用户token
     * @param eventId 活动ID
     * @return 活动信息
     */
    @GetMapping("/detail")
    @org.springframework.transaction.annotation.Transactional
    public EventDto getEvent(@RequestHeader("Authorization") String token, @RequestParam("id") long eventId) {
        // 先验证token，但不直接使用返回的user（可能不在事务内）
        AbstractUser tokenUser = JwtUtil.verifyToken(token);
        if (tokenUser == null) {
            throw new MyException(0, "Invalid token");
        }
        
        // 清除缓存，确保获取最新的报名数据
        Event event = eventService.findEventById(eventId);
        if (event == null) {
            return null;
        }
        
        // 强制初始化所有懒加载的属性，确保在事务内访问
        // 1. 初始化 author
        if (event.getAuthor() != null) {
            event.getAuthor().getId();
            event.getAuthor().getName();
        }
        
        // 2. 初始化 abstractEnrollment 及其相关属性
        AbstractEnrollment enrollment = event.getAbstractEnrollment();
        if (enrollment != null) {
            enrollment.getId();
            enrollment.getStartTime();
            enrollment.getEndTime();
            // 初始化 participants
            if (enrollment.getParticipants() != null) {
                enrollment.getParticipants().size(); // 触发懒加载
                System.out.println("getEvent - Participants count: " + enrollment.getParticipants().size());
            }
            // 如果是 FormEnrollment，初始化 definedFormEntries
            if (enrollment instanceof FormEnrollment formEnrollment) {
                if (formEnrollment.getDefinedFormEntries() != null) {
                    formEnrollment.getDefinedFormEntries().size(); // 触发懒加载
                }
            }
        }
        
        // 3. 初始化 posts
        if (event.getPosts() != null) {
            event.getPosts().size(); // 触发懒加载
        }
        
        // 4. 检查是否已收藏 - 使用JPQL查询来避免懒加载问题
        boolean isLiked = false;
        try {
            AbstractUser currentUser = abstractUserService.findUserById(tokenUser.getId());
            if (currentUser instanceof User user) {
                // 使用JPQL查询检查是否收藏，避免懒加载问题
                Long count = entityManager.createQuery(
                    "SELECT COUNT(ue) FROM User u " +
                    "JOIN u.favouriteEvents ue " +
                    "WHERE u.id = :userId AND ue.id = :eventId", Long.class)
                    .setParameter("userId", user.getId())
                    .setParameter("eventId", eventId)
                    .getSingleResult();
                isLiked = count > 0;
            }
        } catch (Exception e) {
            System.out.println("getEvent - Error checking favorite: " + e.getMessage());
            isLiked = false;
        }
        
        EventDto eventDto = new EventDto(event);
        eventDto.setLiked(isLiked);
        eventDto.setGrade(eventService.getScore(tokenUser.getId(), eventId));
        
        // 检查当前用户是否已报名
        boolean isApplied = eventService.appliedByUser(tokenUser.getId(), eventId);
        eventDto.setApplied(isApplied);
        System.out.println("getEvent - Event ID: " + eventId + ", Current count: " + eventDto.getCurrentCount() + ", Applied: " + isApplied);
        return eventDto;
    }

    /**
     * @param eventId 活动ID
     * @return 活动简要信息
     */
    @GetMapping("/brief")
    public EventBriefDto getEventBrief(@RequestParam("id") long eventId) {
        Event event = eventService.findEventById(eventId);
        if (event == null) {
            return null;
        }
        return new EventBriefDto(event);
    }
    
    /**
     * 获取所有活动的简要信息
     */
    @GetMapping("/brief/all")
    public List<EventBriefDto> getAllEventBriefs() {
        List<Event> events = eventService.findAllEvents();
        // 先按ID排序，确保每次从数据库获取的顺序一致
        events.sort((a, b) -> Long.compare(b.getId(), a.getId())); // 按ID降序排列（最新的在前）
        
        List<EventBriefDto> eventBriefDtos = new ArrayList<>();
        for (Event event : events) {
            eventBriefDtos.add(new EventBriefDto(event));
        }
        // 再次按ID排序，双重保险确保顺序一致
        eventBriefDtos.sort((a, b) -> Long.compare(b.getEventId(), a.getEventId())); // 按ID降序排列（最新的在前）
        return eventBriefDtos;
    }

    /**
     * @param token 用户token
     * @return 用户发布活动的简要信息
     */
    @GetMapping("/hold")
    @org.springframework.transaction.annotation.Transactional
    public List<EventBriefDto> getEventByAuthorId(@RequestHeader("Authorization") String token) {
        System.out.println("=== getEventByAuthorId Start ===");
        AbstractUser user = JwtUtil.verifyToken(token);
        long authorId = user.getId();
        System.out.println("getEventByAuthorId - User ID from token: " + authorId);
        System.out.println("getEventByAuthorId - User class: " + user.getClass().getSimpleName());
        
        List<Event> events = eventService.findEventByAuthorId(authorId);
        System.out.println("getEventByAuthorId - Found " + events.size() + " events");
        
        // 强制初始化懒加载的属性
        events.forEach(event -> {
            if (event.getAuthor() != null) {
                event.getAuthor().getName();
                System.out.println("getEventByAuthorId - Event " + event.getId() + " has author ID: " + event.getAuthor().getId());
            } else {
                System.out.println("getEventByAuthorId - Event " + event.getId() + " has NULL author!");
            }
            if (event.getAbstractEnrollment() != null) {
                event.getAbstractEnrollment().getStartTime();
                event.getAbstractEnrollment().getEndTime();
            }
        });
        
        List<EventBriefDto> dtos = events.stream().map(EventBriefDto::new).toList();
        System.out.println("getEventByAuthorId - Returning " + dtos.size() + " DTOs");
        System.out.println("=== getEventByAuthorId End ===");
        return dtos;
    }

    /**
     * @param token 用户token
     * @param eventId 活动ID
     * @param formValues 报名表单(可选)
     * @return 是否删除成功
     */
    @PostMapping("/apply")
    @org.springframework.transaction.annotation.Transactional
    public boolean applyEvent(@RequestHeader("Authorization") String token, @RequestParam("id") long eventId, @RequestParam(value = "formValues[]", required = false) List<String> formValues) {
        User user = (User) JwtUtil.verifyToken(token);
        if (!user.getPermission().isCanEnroll()) {
            throw new MyException(-1, "Permission denied");
        }
        Event event = eventService.findEventById(eventId);
        if (event == null) {
            throw new MyException(-1, "Event not found");
        }
        
        EnrollForm enrollForm = new EnrollForm();
        AbstractEnrollment abstractEnrollment = event.getAbstractEnrollment();
        if (abstractEnrollment == null) {
            throw new MyException(-1, "Enrollment not found");
        }
        
        System.out.println("applyEvent - Event ID: " + eventId + ", User ID: " + user.getId());
        System.out.println("applyEvent - Enrollment type: " + abstractEnrollment.getClass().getSimpleName());
        
        if (abstractEnrollment instanceof CountEnrollment countEnrollment) {
            System.out.println("applyEvent - Current count: " + countEnrollment.getCount() + ", Capacity: " + countEnrollment.getCapacity());
            if (countEnrollment.getCapacity() > 0 && countEnrollment.getCount() >= countEnrollment.getCapacity()) {
                throw new MyException(-1, "Capacity full");
            } else if (eventService.appliedByUser(user.getId(), eventId)) {
                throw new MyException(-1, "Already applied");
            } else {
                // 确保 participants 列表已初始化
                List<User> participants = countEnrollment.getParticipants();
                if (participants == null) {
                    participants = new ArrayList<>();
                    countEnrollment.setParticipants(participants);
                }
                participants.add(user);
                countEnrollment.setCount(countEnrollment.getCount() + 1);
                System.out.println("applyEvent - After adding, count: " + countEnrollment.getCount());
            }
        }
        if (abstractEnrollment instanceof FormEnrollment formEnrollment) {
            if (formValues == null || formValues.size() != formEnrollment.getDefinedFormEntries().size()) {
                throw new MyException(-1, "Form values not match");
            } else {
                List<User> participants = formEnrollment.getParticipants();
                if (participants == null) {
                    participants = new ArrayList<>();
                    formEnrollment.setParticipants(participants);
                }
                if (!eventService.appliedByUser(user.getId(), eventId)) {
                    participants.add(user);
                }
                enrollForm.setUser(user);
                enrollForm.setFormEnrollment(formEnrollment);
                enrollForm.setFormValues(formValues);
                eventService.saveEnrollForm(enrollForm);
            }
        }
        // 更新 AbstractEnrollment
        abstractEnrollmentService.updateAbstractEnrollment(abstractEnrollment);
        // 同时更新 Event，确保关联关系正确
        eventService.saveEvent(event);
        System.out.println("applyEvent - Enrollment updated successfully");
        return true;
    }

    /**
     * @param id 活动ID
     */
    @GetMapping("/getExcel")
    @ResponseBody
    @org.springframework.transaction.annotation.Transactional
    public void getExcel(@RequestParam("id") long id, HttpServletResponse response) {
        // 清除缓存，重新加载最新数据
        Event event = eventService.findEventById(id);
        if (event == null) {
            throw new MyException(0, "Event not found");
        }
        AbstractEnrollment abstractEnrollment = event.getAbstractEnrollment();
        if (abstractEnrollment == null) {
            throw new MyException(0, "Enrollment not found");
        }
        if (!(abstractEnrollment instanceof CountEnrollment) && !(abstractEnrollment instanceof FormEnrollment)) {
            throw new MyException(0, "Unsupported enrollment type");
        }
        
        // 强制初始化懒加载的 participants 集合
        if (abstractEnrollment.getParticipants() != null) {
            abstractEnrollment.getParticipants().size(); // 触发懒加载
            System.out.println("getExcel - Participants count: " + abstractEnrollment.getParticipants().size());
        }
        
        String fileName = "participants.xlsx";
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            List<String> headers = new ArrayList<>(List.of("Username", "Name"));
            int rowNum = 0;
            XSSFRow row = sheet.createRow(rowNum);
            if (abstractEnrollment instanceof FormEnrollment formEnrollment) {
                for (DefinedFormEntry definedFormEntry : formEnrollment.getDefinedFormEntries()) {
                    headers.add(definedFormEntry.getName());
                }
                for (int i = 0; i < headers.size(); i++) {
                    row.createCell(i).setCellValue(headers.get(i));
                }
                // 使用Repository查询 EnrollForm 列表，确保获取最新数据
                List<EnrollForm> enrollForms = enrollFormRepository.findByFormEnrollment(formEnrollment);
                System.out.println("getExcel - FormEnrollment, EnrollForms count: " + (enrollForms != null ? enrollForms.size() : 0));
                if (enrollForms != null && !enrollForms.isEmpty()) {
                    for (EnrollForm enrollForm : enrollForms) {
                        row = sheet.createRow(++rowNum);
                        User enrollUser = enrollForm.getUser();
                        if (enrollUser != null) {
                            row.createCell(0).setCellValue(enrollUser.getUsername());
                            row.createCell(1).setCellValue(enrollUser.getName());
                        }
                        List<String> formValues = enrollForm.getFormValues();
                        if (formValues != null) {
                            for (int i = 0; i < formValues.size(); i++) {
                                row.createCell(i + 2).setCellValue(formValues.get(i));
                            }
                        }
                    }
                }
            } else if (abstractEnrollment instanceof CountEnrollment) {
                for (int i = 0; i < headers.size(); i++) {
                    row.createCell(i).setCellValue(headers.get(i));
                }
                List<User> participants = abstractEnrollment.getParticipants();
                if (participants != null && !participants.isEmpty()) {
                    System.out.println("getExcel - CountEnrollment, Participants count: " + participants.size());
                    for (User participant : participants) {
                        if (participant != null) {
                            row = sheet.createRow(++rowNum);
                            row.createCell(0).setCellValue(participant.getUsername());
                            row.createCell(1).setCellValue(participant.getName());
                        }
                    }
                } else {
                    System.out.println("getExcel - CountEnrollment, Participants is null or empty");
                }
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param token 用户token
     * @param eventId 活动ID
     * @return 收藏活动是否成功
     */
    @PostMapping("/favor")
    public boolean favorEvent(@RequestHeader("Authorization") String token, @RequestParam("id") long eventId) {
        User user = (User) JwtUtil.verifyToken(token);
        Event event = eventService.findEventById(eventId);
        List<Event> favoriteEvents = user.getFavouriteEvents();
        if (!favoriteEvents.contains(event)) {
            favoriteEvents.add(event);
        } else {
            throw new MyException(-1, "Event already favored");
        }
        user.setFavouriteEvents(favoriteEvents);
        return eventService.updateEvent(event);
    }

    /**
     * @param token 用户token
     * @param eventId 活动ID
     * @return 取消收藏活动是否成功
     */
    @PostMapping("/unfavor")
    public boolean unfavorEvent(@RequestHeader("Authorization") String token, @RequestParam("id") long eventId) {
        User user = (User) JwtUtil.verifyToken(token);
        Event event = eventService.findEventById(eventId);
        List<Event> favoriteEvents = user.getFavouriteEvents();
        if (favoriteEvents.contains(event)) {
            favoriteEvents.remove(event);
        } else {
            throw new MyException(-1, "Event not favored");
        }
        user.setFavouriteEvents(favoriteEvents);
        return eventService.updateEvent(event);
    }

    /**
     * @param token 用户token
     * @param eventId 活动ID
     * @param grade 评分
     * @return 评分是否成功
     */
    @PostMapping("/grade")
    public boolean gradeEvent(@RequestHeader("Authorization") String token, @RequestParam("id") long eventId, @RequestParam("grade") int grade) {
        User user = (User) JwtUtil.verifyToken(token);
        Event event = eventService.findEventById(eventId);
        List<Event> scoredEvents = user.getScoredEvents();
        if (!scoredEvents.contains(event)) {
            event.setScore((event.getScore() * event.getScoreCount() + grade) / (event.getScoreCount() + 1));
            event.setScoreCount(event.getScoreCount() + 1);
        } else {
            long previousScore = eventService.getScore(user.getId(), eventId);
            event.setScore((event.getScore() * event.getScoreCount() - previousScore + grade) / event.getScoreCount());
        }
        eventService.saveScore(user.getId(), eventId, grade);
        eventService.updateEvent(event);
        return true;
    }

    /**
     * @param token 用户token
     * @return 用户已报名的活动ID
     */
    @GetMapping("/applied")
    public long[] getAppliedEvents(@RequestHeader("Authorization") String token) {
        User user = (User) JwtUtil.verifyToken(token);
        return user.getEnrollments().stream().mapToLong(enrollment -> enrollment.getEvent().getId()).toArray();
    }

    /**
     * @param token 用户token
     * @return 用户收藏的活动ID
     */
    @GetMapping("favored")
    public long[] getFavoredEvents(@RequestHeader("Authorization") String token) {
        User user = (User) JwtUtil.verifyToken(token);
        return user.getFavouriteEvents().stream().mapToLong(Event::getId).toArray();
    }
}
