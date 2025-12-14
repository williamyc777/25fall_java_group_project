package org.example.backend.app;

import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.Event;
import org.example.backend.service.AbstractUserService;
import org.example.backend.service.EventService;
import org.example.backend.util.JwtUtil;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 视图控制器 - 处理页面渲染请求
 * 根据教授反馈，前端应该使用Java框架（Thymeleaf）而不是JavaScript框架
 * @author Generated
 * @version 1.0
 */
@Controller
public class ViewController {

    @Autowired
    private AbstractUserService abstractUserService;
    
    @Autowired
    private EventService eventService;

    /**
     * 登录页面
     */
    @GetMapping("/")
    public String login() {
        return "login";
    }

    /**
     * 注册页面
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    /**
     * 主页
     */
    @GetMapping("/main")
    public String mainPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 禁用页面缓存，确保每次访问都获取最新数据
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                AbstractUser user = JwtUtil.verifyToken(token);
                if (user != null) {
                    model.addAttribute("user", user);
                    model.addAttribute("isAdmin", user instanceof org.example.backend.domain.Admin);
                }
            } catch (Exception e) {
                // Token无效，忽略
            }
        }
        // 加载活动列表
        List<Event> events = eventService.findAllEvents();
        System.out.println("mainPage - Found " + events.size() + " events");
        List<Long> eventIds = events.stream()
                .map(e -> e.getId())
                .limit(10)
                .toList();
        System.out.println("mainPage - Event IDs: " + eventIds);
        model.addAttribute("eventIds", eventIds);
        return "main";
    }

    /**
     * 活动页面
     */
    @GetMapping("/event")
    public String eventPage(@RequestParam(required = false) Long id, Model model, HttpServletRequest request, HttpServletResponse response) {
        // 禁用页面缓存，确保每次访问都获取最新数据
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        String token = getTokenFromRequest(request);
        // 即使没有id也要检查token，以便在导航栏显示正确的用户信息
        if (token != null) {
            try {
                AbstractUser user = JwtUtil.verifyToken(token);
                if (user != null) {
                    model.addAttribute("user", user);
                    model.addAttribute("isAdmin", user instanceof org.example.backend.domain.Admin);
                    // 如果有活动ID，检查是否是活动创建者
                    if (id != null) {
                        Event event = eventService.findEventById(id);
                        if (event != null && event.getAuthor() != null && event.getAuthor().getId() == user.getId()) {
                            model.addAttribute("isEventAuthor", true);
                        } else {
                            model.addAttribute("isEventAuthor", false);
                        }
                    }
                }
            } catch (Exception e) {
                // Token无效
            }
        }
        if (id != null) {
            model.addAttribute("eventId", id);
        }
        return "event";
    }

    /**
     * 创建活动页面
     */
    @GetMapping("/event/create")
    public String createEvent(Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return "redirect:/";
        }
        try {
            AbstractUser user = JwtUtil.verifyToken(token);
            if (user == null) {
                return "redirect:/";
            }
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/";
        }
        return "createEvent";
    }

    /**
     * 管理活动页面
     */
    @GetMapping("/event/manage")
    public String manageEvent(Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return "redirect:/";
        }
        try {
            AbstractUser user = JwtUtil.verifyToken(token);
            if (user == null) {
                return "redirect:/";
            }
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/";
        }
        return "manageEvent";
    }

    /**
     * 帖子广场
     */
    @GetMapping("/square")
    public String square(@RequestParam(required = false) Long eventID, Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                AbstractUser user = JwtUtil.verifyToken(token);
                if (user != null) {
                    model.addAttribute("user", user);
                    model.addAttribute("isAdmin", user instanceof org.example.backend.domain.Admin);
                }
            } catch (Exception e) {
                // Token无效
            }
        }
        if (eventID != null) {
            model.addAttribute("eventId", eventID);
        }
        return "square";
    }

    /**
     * 帖子详情
     */
    @GetMapping("/square/post")
    public String postDetail(@RequestParam Long id, Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                AbstractUser user = JwtUtil.verifyToken(token);
                if (user != null) {
                    model.addAttribute("user", user);
                }
            } catch (Exception e) {
                // Token无效
            }
        }
        model.addAttribute("postId", id);
        return "postDetail";
    }

    /**
     * 发帖页面
     */
    @GetMapping("/square/post/write")
    public String writePost(@RequestParam(required = false) Long eventID, Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return "redirect:/";
        }
        try {
            AbstractUser user = JwtUtil.verifyToken(token);
            if (user == null) {
                return "redirect:/";
            }
            model.addAttribute("user", user);
            if (eventID != null) {
                model.addAttribute("eventId", eventID);
            }
        } catch (Exception e) {
            return "redirect:/";
        }
        return "writePost";
    }

    /**
     * 个人资料页面
     */
    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) Long userID, Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                AbstractUser user = JwtUtil.verifyToken(token);
                if (user != null) {
                    System.out.println("Profile page - Loading user ID: " + user.getId());
                    // 强制从数据库重新加载用户信息，确保获取最新数据（包括头像）
                    AbstractUser freshUser = abstractUserService.findUserById(user.getId());
                    if (freshUser != null) {
                        System.out.println("Profile page - Fresh user avatar: " + freshUser.getAvatar());
                        model.addAttribute("currentUser", freshUser);
                        model.addAttribute("isAdmin", freshUser instanceof org.example.backend.domain.Admin);
                        if (userID != null && !userID.equals(freshUser.getId())) {
                            AbstractUser profileUser = abstractUserService.findUserById(userID);
                            if (profileUser != null) {
                                model.addAttribute("user", profileUser);
                            }
                        } else {
                            model.addAttribute("user", freshUser);
                        }
                    } else {
                        // 如果重新加载失败，使用原来的user
                        System.out.println("Profile page - Failed to reload, using original user");
                        model.addAttribute("currentUser", user);
                        model.addAttribute("user", user);
                        model.addAttribute("isAdmin", user instanceof org.example.backend.domain.Admin);
                    }
                }
            } catch (Exception e) {
                // Token无效
                System.out.println("Profile page error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return "profile";
    }

    /**
     * 消息页面
     */
    @GetMapping("/message")
    public String message(Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            System.out.println("Message page: No token found in request");
            return "redirect:/";
        }
        try {
            AbstractUser user = JwtUtil.verifyToken(token);
            if (user == null) {
                System.out.println("Message page: Token verification failed");
                return "redirect:/";
            }
            System.out.println("Message page: User authenticated: " + user.getUsername());
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", user instanceof org.example.backend.domain.Admin);
        } catch (Exception e) {
            System.out.println("Message page: Exception during token verification: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/";
        }
        return "message";
    }

    /**
     * 管理员页面
     */
    @GetMapping("/admin")
    public String admin(Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return "redirect:/";
        }
        try {
            AbstractUser user = JwtUtil.verifyToken(token);
            if (user == null || !(user instanceof org.example.backend.domain.Admin)) {
                return "redirect:/";
            }
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/";
        }
        return "admin";
    }

    /**
     * 搜索帖子页面
     */
    @GetMapping("/post/search")
    public String searchPost(@RequestParam(required = false) String content, Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                AbstractUser user = JwtUtil.verifyToken(token);
                if (user != null) {
                    model.addAttribute("user", user);
                }
            } catch (Exception e) {
                // Token无效
            }
        }
        if (content != null) {
            model.addAttribute("searchContent", content);
        }
        return "searchPost";
    }

    /**
     * 搜索活动页面
     */
    @GetMapping("/event/search")
    public String searchEvent(@RequestParam(required = false) String content, Model model, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                AbstractUser user = JwtUtil.verifyToken(token);
                if (user != null) {
                    model.addAttribute("user", user);
                }
            } catch (Exception e) {
                // Token无效
            }
        }
        if (content != null) {
            model.addAttribute("searchContent", content);
        }
        return "searchEvent";
    }

    /**
     * 从请求中获取token（支持Header和Cookie）
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 首先尝试从Header获取
        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) {
            System.out.println("Token found in Header");
            return token;
        }
        
        // 然后尝试从Cookie获取
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("Found " + cookies.length + " cookies");
            for (Cookie cookie : cookies) {
                System.out.println("Cookie: " + cookie.getName() + " = " + 
                    (cookie.getValue() != null ? cookie.getValue().substring(0, Math.min(20, cookie.getValue().length())) + "..." : "null"));
                if ("token".equals(cookie.getName())) {
                    System.out.println("Token found in Cookie");
                    return cookie.getValue();
                }
            }
        } else {
            System.out.println("No cookies found in request");
        }
        
        System.out.println("No token found");
        return null;
    }
}

