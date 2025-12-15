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
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

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
     * 创建活动
     * @param token 用户token
     * @param eventPostDto 活动信息
     * @return 是否创建成功
     */
    @PostMapping("/create")
    public boolean releaseEvent(@RequestHeader("Authorization") String token, @RequestBody EventPostDto eventPostDto) {
        System.out.println("=== releaseEvent Start ===");
        AbstractUser abstractUser = JwtUtil.verifyToken(token);
        if (!(abstractUser instanceof User)) {
            throw new MyException(-1, "Only regular users can create events. Please login with a user account.");
        }
        User user = (User) abstractUser;
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

                // 防止 definedForm 为空导致 NPE
                List<DefinedFormDto> definedForms = eventPostDto.getDefinedForm();
                if (definedForms == null || definedForms.isEmpty()) {
                    throw new MyException(-1, "表单报名类型必须提供至少一个表单字段");
                }

                formEnrollment.setDefinedFormEntries(
                        definedForms.stream()
                                .map(DefinedFormDto::toDefinedFormEntry)
                                .toList()
                );
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
     * 更新活动（仅作者可改，且不能修改报名方式、容量或表单结构，也不能修改ID）
     */
    @PostMapping("/update")
    public boolean updateEvent(@RequestHeader("Authorization") String token,
                               @RequestBody EventPostDto dto) {
        AbstractUser au = JwtUtil.verifyToken(token);
        if (!(au instanceof User)) {
            throw new MyException(-1, "Only regular users can update events");
        }
        if (dto.getId() == null || dto.getId() <= 0) {
            throw new MyException(-1, "Event id is required");
        }
        Event event = eventService.findEventById(dto.getId());
        if (event == null) {
            throw new MyException(-1, "Event not found");
        }
        // 只能作者本人修改
        if (event.getAuthor() == null || event.getAuthor().getId() != au.getId()) {
            throw new MyException(-1, "Permission denied: only author can edit this event");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 不允许修改 enrollmentType、capacity、definedForm 等字段
        event.setTitle(dto.getTitle());
        event.setName(dto.getName());
        event.setIntroduction(dto.getIntroduction());
        event.setText(dto.getMdText());
        event.setPosterUrl(dto.getImageUrl());

        try {
            String startStr = dto.getStartTime();
            String endStr = dto.getEndTime();
            if (startStr != null && !startStr.isBlank()) {
                event.setStartTime(LocalDateTime.parse(startStr, formatter));
            }
            if (endStr != null && !endStr.isBlank()) {
                event.setEndTime(LocalDateTime.parse(endStr, formatter));
            }
        } catch (Exception e) {
            throw new MyException(-1, "Invalid event time format, expected yyyy-MM-dd HH:mm:ss");
        }

        try {
            AbstractEnrollment enrollment = event.getAbstractEnrollment();
            if (enrollment != null) {
                String applyStartStr = dto.getApplyStartTime();
                String applyEndStr = dto.getApplyEndTime();
                if (applyStartStr != null && !applyStartStr.isBlank()) {
                    enrollment.setStartTime(LocalDateTime.parse(applyStartStr, formatter));
                }
                if (applyEndStr != null && !applyEndStr.isBlank()) {
                    enrollment.setEndTime(LocalDateTime.parse(applyEndStr, formatter));
                }
            }
        } catch (Exception e) {
            throw new MyException(-1, "Invalid apply time format, expected yyyy-MM-dd HH:mm:ss");
        }

        return eventService.updateEvent(event);
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
                // enrollment is full, fail with clear English message
                throw new MyException(-1, "Enrollment is full. You cannot apply for this event anymore.");
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
                // 如果用户已经报名过该表单活动，则直接拒绝，避免重复记录
                if (eventService.appliedByUser(user.getId(), eventId)) {
                    throw new MyException(-1, "Already applied");
                }

                List<User> participants = formEnrollment.getParticipants();
                if (participants == null) {
                    participants = new ArrayList<>();
                    formEnrollment.setParticipants(participants);
                }
                participants.add(user);

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
                    // 去重：按 userId 只保留一条记录，避免同一用户重复出现在导出中
                    Map<Long, EnrollForm> uniqueByUser = new LinkedHashMap<>();
                    for (EnrollForm enrollForm : enrollForms) {
                        User u = enrollForm.getUser();
                        if (u != null) {
                            uniqueByUser.putIfAbsent(u.getId(), enrollForm);
                        }
                    }

                    for (EnrollForm enrollForm : uniqueByUser.values()) {
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
                    System.out.println("getExcel - CountEnrollment, Participants count (raw): " + participants.size());
                    // 去重：按 userId 只导出一次，防止同一用户在 participants 列表中出现多次
                    Set<Long> seenUserIds = new HashSet<>();
                    for (User participant : participants) {
                        if (participant != null && seenUserIds.add(participant.getId())) {
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
    @org.springframework.transaction.annotation.Transactional
    public boolean favorEvent(@RequestHeader("Authorization") String token, @RequestParam("id") long eventId) {
        AbstractUser tokenUser = JwtUtil.verifyToken(token);
        if (!(tokenUser instanceof User)) {
            throw new MyException(-1, "Only regular users can favorite events");
        }
        // 使用托管实体，避免懒加载问题
        User user = (User) abstractUserService.findUserById(tokenUser.getId());
        if (user == null) {
            throw new MyException(-1, "User not found");
        }

        Event event = eventService.findEventById(eventId);
        if (event == null) {
            throw new MyException(-1, "Event not found");
        }

        List<Event> favoriteEvents = user.getFavouriteEvents();
        if (favoriteEvents == null) {
            favoriteEvents = new java.util.ArrayList<>();
            user.setFavouriteEvents(favoriteEvents);
        } else {
            favoriteEvents.size(); // 触发懒加载，在事务内不会抛错
        }

        boolean alreadyFavored = favoriteEvents.stream()
                .anyMatch(e -> e != null && e.getId() == event.getId());
        if (alreadyFavored) {
            throw new MyException(-1, "Event already favored");
        }
        favoriteEvents.add(event);
        abstractUserService.saveUser(user);
        return true;
    }

    /**
     * @param token 用户token
     * @param eventId 活动ID
     * @return 取消收藏活动是否成功
     */
    @PostMapping("/unfavor")
    @org.springframework.transaction.annotation.Transactional
    public boolean unfavorEvent(@RequestHeader("Authorization") String token, @RequestParam("id") long eventId) {
        AbstractUser tokenUser = JwtUtil.verifyToken(token);
        if (!(tokenUser instanceof User)) {
            throw new MyException(-1, "Only regular users can unfavorite events");
        }
        // 使用托管实体，避免懒加载问题
        User user = (User) abstractUserService.findUserById(tokenUser.getId());
        if (user == null) {
            throw new MyException(-1, "User not found");
        }

        Event event = eventService.findEventById(eventId);
        if (event == null) {
            throw new MyException(-1, "Event not found");
        }

        List<Event> favoriteEvents = user.getFavouriteEvents();
        if (favoriteEvents == null) {
            throw new MyException(-1, "Event not favored");
        } else {
            favoriteEvents.size(); // 触发懒加载
        }

        Event toRemove = favoriteEvents.stream()
                .filter(e -> e != null && e.getId() == event.getId())
                .findFirst()
                .orElse(null);
        if (toRemove == null) {
            throw new MyException(-1, "Event not favored");
        }
        favoriteEvents.remove(toRemove);
        abstractUserService.saveUser(user);
        return true;
    }

    /**
     * @param token 用户token
     * @param eventId 活动ID
     * @param grade 评分
     * @return 评分是否成功
     */
    @PostMapping("/grade")
    @Transactional
    public boolean gradeEvent(@RequestHeader("Authorization") String token,
                              @RequestParam("id") long eventId,
                              @RequestParam("grade") int grade) {
        // 只用 userId + eventId 查询 / 更新评分记录，不再访问 user.scoredEvents 懒加载集合
        AbstractUser tokenUser = JwtUtil.verifyToken(token);
        if (!(tokenUser instanceof User)) {
            throw new MyException(-1, "Only regular users can grade events");
        }
        long userId = tokenUser.getId();

        Event event = eventService.findEventById(eventId);
        if (event == null) {
            throw new MyException(-1, "Event not found");
        }

        // 通过 Score 表判断是否第一次评分，避免访问懒加载集合
        long previousScore = eventService.getScore(userId, eventId);
        boolean firstTime = (previousScore == 0); // 约定未评分返回 0，评分区间为 1-5

        if (firstTime) {
            event.setScore((event.getScore() * event.getScoreCount() + grade) / (event.getScoreCount() + 1));
            event.setScoreCount(event.getScoreCount() + 1);
        } else {
            event.setScore((event.getScore() * event.getScoreCount() - previousScore + grade) / event.getScoreCount());
        }

        eventService.saveScore(userId, eventId, grade);
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
        AbstractUser tokenUser = JwtUtil.verifyToken(token);
        if (!(tokenUser instanceof User)) {
            throw new MyException(-1, "Only regular users can have favorite events");
        }
        // 在一个短事务中加载用户的收藏列表并返回 ID，避免懒加载异常
        User user = (User) abstractUserService.findUserById(tokenUser.getId());
        if (user == null) {
            throw new MyException(-1, "User not found");
        }
        List<Event> favorites = user.getFavouriteEvents();
        if (favorites == null) {
            return new long[0];
        }
        favorites.size(); // 触发懒加载
        return favorites.stream()
                .filter(java.util.Objects::nonNull)
                .mapToLong(Event::getId)
                .toArray();
    }
}
