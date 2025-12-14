package org.example.backend.app;

import org.example.backend.domain.Event;
import org.example.backend.domain.Post;
import org.example.backend.service.EventService;
import org.example.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 该类用于处理搜索相关的请求
 * @author Shinomiya
 * @version 1.0
 */
@RestController
@RequestMapping("/search")
public class SearchApp {
    @Autowired
    private EventService eventService;
    @Autowired
    private PostService postService;

    /**
     * @param content 搜索内容
     * @return 模糊搜索到的相关活动
     */
    @GetMapping("/event")
    public List<Long> searchEvent(@RequestParam String content) {
        return eventService.searchEvent(content).stream().map(Event::getId).toList();
    }

    /**
     * @param content 搜索内容
     * @return 模糊搜索到的相关帖子
     */
    @GetMapping("/post")
    public List<Long> searchPost(@RequestParam String content) {
        return postService.searchPost(content).stream().map(Post::getId).toList();
    }
}
