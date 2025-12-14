package org.example.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.app.AbstractUserApp;
import org.example.backend.domain.*;
import org.example.backend.dto.EventPostDto;
import org.example.backend.dto.GlobalResponse;
import org.example.backend.dto.PostDto;
import org.example.backend.service.AbstractEnrollmentService;
import org.example.backend.service.AbstractUserService;
import org.example.backend.service.EventService;
import org.example.backend.service.PostService;
import org.example.backend.util.JwtUtil;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// This will not start the server, but will start the application context
//@SpringBootTest()
// This will start the server
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BackendApplicationTests {
    // This will inject the port that the server is running on
    @LocalServerPort
    private Integer port;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AbstractUserService abstractUserService;
    @Autowired
    private AbstractEnrollmentService abstractEnrollmentService;
    @Autowired
    private EventService eventService;
    @Autowired
    private PostService postService;
    @Autowired
    private ProjectInfoAutoConfiguration projectInfoAutoConfiguration;


//    @Test
//    void generateData() {
//        // generate users
//        Admin admin = new Admin();
//        admin.setUsername("admin");
//        admin.setPassword("admin");
//        admin.setName("admin");
//        admin.setBio("admin");
//        abstractUserService.saveUser(admin);
//        User user = new User();
//        for (int i = 0; i < 8; i++) {
//            user = new User();
//            user.setUsername(String.valueOf(i));
//            user.setPassword(String.valueOf(i));
//            user.setName("user" + i);
//            user.setBio("user" + i);
//            user.setAvatar("http://10.16.88.247:8082/image/default_avatar.jpg");
//            Permission permission = new Permission();
//            permission.setUser(user);
//            permission.setCanCreate((i & 1) == 1);
//            permission.setCanEnroll((i & 2) == 2);
//            permission.setCanComment((i & 4) == 4);
//            user.setPermission(permission);
//            abstractUserService.saveUser(user);
//        }
//
//        // generate events
//        // preform login user 7
//        String token = (String) restTemplate.postForObject("http://localhost:" + port + "/login?username=7&password=7", null, GlobalResponse.class).getData();
//        // add lecture
//        Event lecture = new Event();
//        CountEnrollment countEnrollment = new CountEnrollment();
//        lecture.setTitle("环境保护人人有责");
//        lecture.setName("环境保护主题讲座");
//        lecture.setIntroduction("请来参与讲座吧");
//        lecture.setPosterUrl("http://10.16.88.247:8082/image/env_poster.png");
//        lecture.setText("\n" + "------\n" + "\n" + "# \uD83C\uDF3F 大学环境保护讲座 - “绿色行动，共创未来” \uD83C\uDF3F\n" + "\n" + "## 加入我们，一起探索可持续发展的秘密！\n" + "\n" + "<img src='http://10.16.88.247:8082/image/env.png' width=300>\n" + "\n" + "### \uD83D\uDDD3\uFE0F 讲座日期\n" + "\n" + "- **2024年8月5日 星期一**\n" + "\n" + "### \uD83D\uDD52 讲座时间\n" + "\n" + "- **下午3:00 - 5:00**\n" + "\n" + "### \uD83D\uDCCD 讲座地点\n" + "\n" + "- **大学图书馆报告厅**\n" + "- 南山区, 广东省\n" + "\n" + "### \uD83C\uDF99\uFE0F 特邀嘉宾介绍\n" + "\n" + "#### 李教授\n" + "\n" + "- **职位**：环境科学系主任\n" + "- **专长**：生态系统管理与可持续发展\n" + "- **成就**：发表多篇关于生态保护的论文，主持多个国家级环保项目\n" + "\n" + "#### 张博士\n" + "\n" + "- **职位**：知名环保活动家\n" + "- **专长**：城市可持续规划\n" + "- **成就**：在国际环保会议上多次发言，推动多项环保法案的制定\n" + "\n" + "### \uD83C\uDF31 讲座内容\n" + "\n" + "- 在这次讲座中，我们将深入探讨以下几个关键主题：\n" + "\n" + "  1. **可持续生活方式的实践**\n" + "     - 探索日常生活中的绿色选择\n" + "     - 分享节能减排的小技巧\n" + "     - 讨论如何减少塑料和一次性产品的使用\n" + "  2. **绿色能源的未来**\n" + "     - 了解太阳能、风能等可再生能源的最新进展\n" + "     - 讨论绿色能源对经济和社会的影响\n" + "     - 分析传统能源转型的挑战与机遇\n" + "  3. **生物多样性的重要性**\n" + "     - 讲述生物多样性对地球生态系统的意义\n" + "     - 分享成功的野生动植物保护案例\n" + "     - 探讨城市化进程中的生态平衡问题\n" + "  4. **个人与社区如何减少碳足迹**\n" + "     - 分享社区绿化和城市农业的案例\n" + "     - 讨论公共交通和绿色出行的优势\n" + "     - 提供家庭能效提升的建议\n" + "  5. **环保法律与政策**\n" + "     - 解读当前环保法律框架\n" + "     - 分析政策如何推动环保行动\n" + "     - 探讨公民如何参与环保政策的制定\n" + "  6. **绿色科技的创新**\n" + "     - 展示环保科技的最新发明\n" + "     - 讨论科技如何帮助我们更好地保护环境\n" + "     - 分析绿色科技的发展趋势\n" + "\n" + "  ------\n" + "\n" + "  这些内容将由我们的嘉宾通过精彩的演讲和互动环节呈现给大家。我们期待您的参与，一起为保护我们的地球贡献力量！\n" + "\n" + "### \uD83D\uDCDA 参与者将获得\n" + "\n" + "- **免费环保资料包**\n" + "- **参与证书**\n" + "- **绿色植物一盆**\n" + "\n" + "### \uD83D\uDCDD 报名方式\n" + "\n" + "请在这里进行在线报名。\n" + "\n" + "\n" + "### ? 报名截止\n" + "\n" + "**2024年7月30日**\n" + "\n" + "------\n" + "\n" + "\uD83C\uDF1F **让我们携手为地球的未来贡献力量！立即报名参加！** \uD83C\uDF1F\n" + "\n" + "\n" + "\n" + "\u200B              ");
//        lecture.setAuthor(user);
//        lecture.setStartTime(LocalDateTime.of(2021, 6, 15, 0, 0, 0));
//        lecture.setEndTime(LocalDateTime.of(2021, 6, 16, 0, 0, 0));
//        countEnrollment.setEvent(lecture);
//        countEnrollment.setStartTime(LocalDateTime.of(2021, 6, 1, 0, 0, 0));
//        countEnrollment.setEndTime(LocalDateTime.of(2024, 6, 1, 0, 0, 0));
//        countEnrollment.setCapacity(100);
//        lecture.setAbstractEnrollment(countEnrollment);
//        // save lecture
//        eventService.saveEvent(lecture);
//
//        // add soccer
//        Event soccer = new Event();
//        FormEnrollment formEnrollment = new FormEnrollment();
//        soccer.setTitle("一起来踢球吧！");
//        soccer.setName("校园足球赛");
//        soccer.setIntroduction("校园足球赛即将开赛，参与比赛展风采！");
//        soccer.setPosterUrl("http://10.16.88.247:8082/image/soccer_poster.png");
//        soccer.setText("------\n" + "\n" + "# \uD83C\uDFC6 大学足球联赛报名现已开启！ \uD83C\uDFC6\n" + "\n" + "## 一场展现技巧，团队精神和激情的比赛！\n" + "\n" + "### \uD83D\uDCC5 比赛日期\n" + "\n" + "- **预选赛**：2024年7月12日\n" + "- **半决赛**：2024年7月19日\n" + "- **决赛**：2024年7月26日\n" + "\n" + "### \uD83D\uDCCD 比赛地点\n" + "\n" + "- **体育中心足球场**\n" + "- 南山区, 广东省\n" + "\n" + "### \uD83D\uDCDD 报名要求\n" + "\n" + "- 必须是在校大学生\n" + "- 每队人数不超过 **11人**\n" + "- 提供完整的团队信息和联系方式\n" + "\n" + "### \uD83C\uDF96\uFE0F 奖项设置\n" + "\n" + "- **冠军**：奖金 **￥5000** + 奖杯\n" + "- **亚军**：奖金 **￥3000**\n" + "- **季军**：奖金 **￥1000**\n" + "\n" + "<img src=\"http://10.16.88.247:8082/image/soccer.png\" width=\"500\">\n" + "\n" + "### \uD83D\uDCE2 如何报名\n" + "\n" + "请填写报名表格。\n" + "\n" + "```markdown\n" + "- 团队名称：\n" + "- 队长姓名：\n" + "- 联系电话：\n" + "- 队员名单：\n" + "  - 球员1\n" + "  - 球员2\n" + "  - ...\n" + "```\n" + "\n" + "### ? 报名截止日期\n" + "\n" + "**2024年6月30日**\n" + "\n" + "更多相关信息请查看以下链接：\n" + "\n" + "[足球](https://soccer.hupu.com/)\n" + "\n" + "------\n" + "\n" + "\uD83D\uDD25 **不要错过这个展现你们足球才华的机会！快来报名参加吧！** \uD83D\uDD25\n" + "\n" + "------\n" + "\n");
//        soccer.setAuthor(user);
//        soccer.setStartTime(LocalDateTime.of(2021, 6, 15, 0, 0, 0));
//        soccer.setEndTime(LocalDateTime.of(2021, 6, 16, 0, 0, 0));
//        soccer.setAbstractEnrollment(formEnrollment);
//        formEnrollment.setEvent(soccer);
//        formEnrollment.setStartTime(LocalDateTime.of(2021, 6, 1, 0, 0, 0));
//        formEnrollment.setEndTime(LocalDateTime.of(2024, 6, 1, 0, 0, 0));
//        formEnrollment.setDefinedFormEntries(new ArrayList<>());
//        soccer.setAbstractEnrollment(formEnrollment);
//        // add FormEntry
//        List<DefinedFormEntry> definedFormEntries = formEnrollment.getDefinedFormEntries();
//        //name
//        DefinedFormEntry definedFormEntry = new DefinedFormEntry();
//        definedFormEntry.setEntryId(0);
//        definedFormEntry.setName("name");
//        definedFormEntry.setType("input");
//        definedFormEntry.setRequired(true);
//        definedFormEntries.add(definedFormEntry);
//        //sid
//        definedFormEntry = new DefinedFormEntry();
//        definedFormEntry.setEntryId(1);
//        definedFormEntry.setName("sid");
//        definedFormEntry.setType("input");
//        definedFormEntry.setRequired(true);
//        definedFormEntries.add(definedFormEntry);
//        //gender
//        definedFormEntry = new DefinedFormEntry();
//        definedFormEntry.setOptions(new ArrayList<>());
//        List<String> options = definedFormEntry.getOptions();
//        options.add("male");
//        options.add("female");
//        definedFormEntry.setEntryId(2);
//        definedFormEntry.setName("gender");
//        definedFormEntry.setType("select");
//        definedFormEntry.setRequired(true);
//        definedFormEntries.add(definedFormEntry);
//        //gpa
//        definedFormEntry = new DefinedFormEntry();
//        definedFormEntry.setEntryId(3);
//        definedFormEntry.setName("gpa");
//        definedFormEntry.setType("input");
//        definedFormEntry.setRequired(true);
//        definedFormEntries.add(definedFormEntry);
//        //save soccer
//        eventService.saveEvent(soccer);
//
//        // generate posts
//        // add lecture post1
//        Post post = new Post();
//        post.setUser(user);
//        ArrayList<AbstractUser> likeUsers = new ArrayList<>();
//        likeUsers.add(user);
//        post.setLikeUsers(likeUsers);
//        post.setEvent(lecture);
//        post.setPostTitle("大学环境保护讲座 - “绿色行动，共创未来”");
//        post.setPostContent("# \uD83C\uDF3F 大学环境保护讲座 - “绿色行动，共创未来” \uD83C\uDF3F\n" + "\n" + "8月5日下午，我们在大学图书馆报告厅参加了一场别开生面的环境保护讲座。两位特邀嘉宾李教授和张博士带领我们深入了解了可持续发展的重要性，并为我们带来了很多有益的启示和实践建议。\n" + "\n" + "### 讲座亮点回顾\n" + "\n" + "#### 1. **可持续生活方式的实践**\n" + "\n" + "李教授详细介绍了如何在日常生活中做出绿色选择。他分享了一些简单易行的节能减排小技巧，比如减少塑料使用和推广可重复利用的产品。这些建议不仅实用，还让我们意识到每一个小改变都可以带来大不同。\n" + "\n" + "#### 2. **绿色能源的未来**\n" + "\n" + "张博士为我们展示了太阳能、风能等可再生能源的最新进展，并分析了这些绿色能源对经济和社会的积极影响。特别是关于传统能源转型的讨论，让我们看到在环保事业中科技创新的重要性。\n" + "\n" + "#### 3. **生物多样性的重要性**\n" + "\n" + "李教授讲述了生物多样性对地球生态系统的意义，并分享了一些成功的野生动植物保护案例。这部分内容让我们深刻认识到保护生物多样性不仅是为了环境，更是为了我们的未来。\n" + "\n" + "#### 4. **个人与社区如何减少碳足迹**\n" + "\n" + "张博士分享了许多关于社区绿化和城市农业的案例，并讨论了公共交通和绿色出行的优势。他还提供了许多家庭能效提升的实用建议，让我们知道每个人都可以为环保做出贡献。\n" + "\n" + "### 感谢与收获\n" + "\n" + "这次讲座不仅丰富了我们的知识，还激发了我们对环保的热情。每一位参与者都获得了免费环保资料包、参与证书和一盆绿色植物。这些小礼物不仅是纪念品，更是我们行动的开始。\n" + "\n" + "### 未来的行动\n" + "\n" + "通过这次讲座，我们深刻认识到环境保护的紧迫性和重要性。未来，我们将更加注重绿色生活方式，积极参与环保活动，共同为地球的未来贡献力量。\n" + "\n" + "感谢所有参与者和讲座组织者，让我们携手为地球的未来努力！\n" + "\n" + "\uD83C\uDF1F **让我们共同为环境保护献出一份力量！** \uD83C\uDF1F\n" + "\n" + "![环保讲座海报](http://10.16.88.247:8082/image/env.png)\n" + "\n" + "更多环保信息\n" + "\n" + "让我们期待下一次的环保讲座，共同学习，共同进步！");
//        // save lecture post1
//        postService.savePost(post);
//
//        // add lecture post2
//        post = new Post();
//        post.setUser(user);
//        likeUsers = new ArrayList<>();
//        likeUsers.add(user);
//        post.setLikeUsers(likeUsers);
//        post.setEvent(lecture);
//        post.setPostTitle("环保讲座感想");
//        post.setPostContent("8月5日下午，大学图书馆报告厅迎来了一场意义非凡的讲座——“绿色行动，共创未来”。两位重量级嘉宾李教授和张博士的精彩演讲，带领我们深入了解了可持续发展的方方面面。\n" + "\n" + "### 精彩瞬间回顾\n" + "\n" + "#### **李教授的环保智慧**\n" + "\n" + "李教授作为环境科学系主任，他的演讲生动有趣。他从生态系统管理和可持续发展的角度，讲解了许多实际可行的环保方法。他告诉我们，从减少一次性产品使用到推广绿色出行，每一个小小的改变都能带来巨大的环境效益。\n" + "\n" + "#### **张博士的前瞻视野**\n" + "\n" + "张博士的讲座则更侧重于城市可持续规划和绿色能源的未来。他介绍了最新的太阳能和风能技术，并分享了这些绿色能源在经济和社会中的应用实例。张博士还提到了环保法律和政策的制定过程，鼓励我们积极参与到环保政策的推动中。\n" + "\n" + "### 讲座的核心内容\n" + "\n" + "- 可持续生活方式的实践\n" + "  - 了解如何在日常生活中做出绿色选择，减少塑料和一次性产品的使用。\n" + "- 绿色能源的未来\n" + "  - 探讨太阳能、风能等可再生能源的最新进展及其对社会经济的影响。\n" + "- 生物多样性的重要性\n" + "  - 讲述生物多样性对生态系统的意义及成功保护案例。\n" + "- 个人与社区如何减少碳足迹\n" + "  - 分享社区绿化、城市农业和绿色出行的案例和建议。\n" + "- 环保法律与政策\n" + "  - 解读当前环保法律框架，探讨政策如何推动环保行动。\n" + "- 绿色科技的创新\n" + "  - 展示最新的环保科技发明，分析其发展趋势。\n" + "\n" + "### 感悟与收获\n" + "\n" + "这次讲座不仅让我们学到了丰富的环保知识，还让我们更深刻地理解了环境保护的重要性。讲座结束后，每位参与者都收到了环保资料包、参与证书和一盆绿色植物，这些小礼物不仅是纪念，更是我们行动的动力。\n" + "\n" + "### 行动从现在开始\n" + "\n" + "通过这次讲座，我们更加坚定了践行绿色生活的决心。未来，我们会积极参与环保活动，从小事做起，减少碳足迹，为地球的未来贡献我们的力量。\n" + "\n" + "感谢所有参与者和讲座的组织者，让我们携手为保护环境努力！\n" + "\n" + "\uD83C\uDF1F **一起行动起来，为地球的未来尽一份力！** \uD83C\uDF1F");
//        // save lecture post2
//        postService.savePost(post);
//
//        // add soccer post1
//        post = new Post();
//        post.setUser(user);
//        likeUsers = new ArrayList<>();
//        likeUsers.add(user);
//        post.setLikeUsers(likeUsers);
//        post.setEvent(soccer);
//        post.setPostTitle("大学足球联赛，我们的赛场回忆");
//        post.setPostContent("### 大学足球联赛，我们的赛场回忆\n" + "\n" + "大学足球联赛刚刚落下帷幕，每一个参与者都在赛场上留下了不可磨灭的回忆。作为一名参赛队员，我想分享一些心路历程和难忘瞬间。\n" + "\n" + "从预选赛到决赛，每一场比赛都紧张激烈。**7月12日**的预选赛，我们全队团结一致，克服了重重困难，终于挺进半决赛。那天的天气虽然炎热，但我们的激情更加炽热。\n" + "\n" + "**7月19日**的半决赛是我们面临的最大挑战，对手实力强劲，比赛异常艰苦。尽管我们在最后时刻惜败，但我们没有遗憾，因为我们拼尽全力，展现了最佳状态。\n" + "\n" + "**7月26日**的决赛虽然没有我们队的身影，但我们在场边为其他队伍加油呐喊，共同见证了冠军的诞生。看到「热血战队」捧起奖杯的那一刻，我们所有人都感受到了那份荣誉的分量。\n" + "\n" + "这次比赛不仅是对我们足球技术的考验，更是对团队协作和意志力的磨砺。我们收获了友谊，增强了团队精神，最重要的是，我们更加热爱这项运动。\n" + "\n" + "感谢大学足球联赛给我们提供了这样一个展示自我的平台。期待明年的比赛，期待我们能再次并肩作战，争取更好的成绩！\n" + "\n" + "\uD83C\uDFC6 **大学足球联赛，我们明年再见！** \uD83C\uDFC6");
//        // save soccer post1
//        postService.savePost(post);
//
//        // add soccer post2
//        post = new Post();
//        post.setUser(user);
//        likeUsers = new ArrayList<>();
//        likeUsers.add(user);
//        post.setLikeUsers(likeUsers);
//        post.setEvent(soccer);
//        post.setPostTitle("燃情回顾，大学足球联赛的感动瞬间！");
//        post.setPostContent("### 燃情回顾，大学足球联赛的感动瞬间！\n" + "\n" + "大学足球联赛圆满结束，让我们一起回顾这段充满激情与感动的足球之旅吧！\n" + "\n" + "\uD83C\uDFC6 **冠军**：恭喜「热血战队」荣获冠军，并斩获丰厚奖金￥5000和闪亮奖杯！他们的团队合作和顽强拼搏精神令人敬佩！\n" + "\n" + "\uD83E\uDD48 **亚军**：「风暴队」表现出色，力拼到最后一刻，最终获得亚军，并收获￥3000的奖金。他们的奋斗精神令人动容！\n" + "\n" + "\uD83E\uDD49 **季军**：「豪杰队」在比赛中展现出良好的技术和团队默契，获得了季军的殊荣，同时也获得了￥1000的奖金。\n" + "\n" + "每支参赛队伍都用汗水和激情书写了自己的足球梦想，在比赛中收获了友谊、团队精神和成长。正是这些感动瞬间，让大学足球联赛充满了意义与价值！\n" + "\n" + "我们期待着下一届的比赛，更多的激情，更多的荣耀！感谢每一位参与者的付出和支持，让我们一起见证足球梦想的绽放！\n" + "\n" + "[想了解更多关于足球赛事的信息，请点击这里！](https://soccer.hupu.com/)\n" + "\n" + "共同创造更多足球的辉煌瞬间！\uD83C\uDF1F");
//        // save soccer post2
//        postService.savePost(post);
//
//        System.out.println(restTemplate.postForObject("http://localhost:" + port + "/event/apply?id=1", null, GlobalResponse.class).getData());
//    }


    @Test
    @Order(1)
    void saveUser() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setName("admin");
        admin.setBio("admin");
        assert (abstractUserService.saveUser(admin));
        for (int i = 0; i < 8; i++) {
            User user = new User();
            user.setUsername(String.valueOf(i));
            user.setPassword(String.valueOf(i));
            user.setName("user" + i);
            user.setBio("user" + i);
            Permission permission = new Permission();
            permission.setUser(user);
            permission.setCanCreate((i & 1) == 1);
            permission.setCanEnroll((i & 2) == 2);
            permission.setCanComment((i & 4) == 4);
            user.setPermission(permission);
            assert (abstractUserService.saveUser(user));
        }
    }

    @Test
    @Order(2)
    void LoginTest() {
        String token = (String) restTemplate.postForObject("http://localhost:" + port + "/login?username=1&password=1", null, GlobalResponse.class).getData();
        assert (token != null);
        User user = (User) JwtUtil.verifyToken(token);
        assert (user.getUsername().equals("1"));
    }


    @Test
    @Order(3)
    void MessageTest() {
        // preform login
        String token1 = (String) restTemplate.postForObject("http://localhost:" + port + "/login?username=1&password=1", null, GlobalResponse.class).getData();
        String token2 = (String) restTemplate.postForObject("http://localhost:" + port + "/login?username=2&password=2", null, GlobalResponse.class).getData();
        // test the sendChat api
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/message/sendChat").header("Authorization", token1).param("userId", "2").param("content", "hello2").param("time", "2021-05-01 00:00:00")).andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/message/sendChat").header("Authorization", token2).param("userId", "1").param("content", "hello1").param("time", "2021-05-02 00:00:00")).andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void EventTest() throws JSONException, JsonProcessingException {
        // preform login
        String token = (String) restTemplate.postForObject("http://localhost:" + port + "/login?username=7&password=7", null, GlobalResponse.class).getData();
        User user = (User) JwtUtil.verifyToken(token);
        // test the create api
        // create two events
        Event event = generateSoccer(user, "event1");
        ObjectMapper mapper = new ObjectMapper();
        EventPostDto eventPostDto = new EventPostDto(event);
        eventPostDto.setApplyEndTime("2024-06-05 00:00:00");
        eventPostDto.setApplyStartTime("2021-06-01 00:00:00");
        eventPostDto.setEndTime("2021-06-16 00:00:00");
        eventPostDto.setStartTime("2021-06-15 00:00:00");
        String jsonStr = mapper.writeValueAsString(eventPostDto);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/create").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(jsonStr)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        event = generateSoccer(user, "event2");
        eventPostDto = new EventPostDto(event);
        eventPostDto.setApplyEndTime("2024-06-05 00:00:00");
        eventPostDto.setApplyStartTime("2021-06-01 00:00:00");
        eventPostDto.setEndTime("2021-06-16 00:00:00");
        eventPostDto.setStartTime("2021-06-15 00:00:00");
        jsonStr = mapper.writeValueAsString(eventPostDto);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/create").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(jsonStr)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the event list
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/event/hold").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the event detail
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/event/detail").header("Authorization", token).param("id", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.title").value("event1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the event brief
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/event/brief").header("Authorization", token).param("id", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.title").value("event1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // test the apply api
        // apply for event1
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/apply").header("Authorization", token).param("id", "1").param("formValues[]", "wyr").param("formValues[]", "12111111").param("formValues[]", "male")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // apply for event1
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/apply").header("Authorization", token).param("id", "1").param("formValues[]", "wyr").param("formValues[]", "12111111").param("formValues[]", "male")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // apply for event2
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/apply").header("Authorization", token).param("id", "2").param("formValues[]", "wyr").param("formValues[]", "12111111").param("formValues[]", "male")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the apply list
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/event/applied").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // favor event1
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/favor").header("Authorization", token).param("id", "1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the favor list
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/event/favored").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // unfavor event1
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/unfavor").header("Authorization", token).param("id", "1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the favor list
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/event/favored").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // score event1
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/event/grade").header("Authorization", token).param("id", "1").param("grade", "5")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the excelFile
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/event/getExcel").header("Authorization", token).param("id", "1")).andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    void PostTest() {
        // preform login
        String token = (String) restTemplate.postForObject("http://localhost:" + port + "/login?username=1&password=1", null, GlobalResponse.class).getData();
        // test the create api
        // create a post
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/post/releasePost").header("Authorization", token).param("postTitle", "post1").param("postContent", "content1").param("postRelevantEvent", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("code").value("0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // like and collect
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/post/likeThePost").header("Authorization", token).param("postID", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data").value("true"));
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/post/collectThePost").header("Authorization", token).param("postID", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the post list
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/post/getPostSquare").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get the post detail
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/post/getFullPost").header("Authorization", token).param("postID", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.postTitle").value("post1"))
                    .andExpect(MockMvcResultMatchers.jsonPath("data.postContent").value("content1"))
                    .andExpect(MockMvcResultMatchers.jsonPath("data.likeOrNot").value("true"))
                    .andExpect(MockMvcResultMatchers.jsonPath("data.collectOrNot").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // dislike and discollect
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/post/dislikeThePost").header("Authorization", token).param("postID", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data").value("true"));
            mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:" + port + "/post/discollectThePost").header("Authorization", token).param("postID", "1")).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data").value("true"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // get collection
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port + "/post/getPostSquare/collect").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    Event generateSoccer(User author, String title) {
        // generate soccer
        Event soccer = new Event();
        FormEnrollment formEnrollment = new FormEnrollment();
        soccer.setTitle(title);
        soccer.setName("校园足球赛");
        soccer.setIntroduction("校园足球赛即将开赛，参与比赛展风采！");
        soccer.setPosterUrl("http://10.16.88.247:8082/image/soccer_poster.png");
        soccer.setText("------\n" + "\n" + "# \uD83C\uDFC6 大学足球联赛报名现已开启！ \uD83C\uDFC6\n" + "\n" + "## 一场展现技巧，团队精神和激情的比赛！\n" + "\n" + "### \uD83D\uDCC5 比赛日期\n" + "\n" + "- **预选赛**：2024年7月12日\n" + "- **半决赛**：2024年7月19日\n" + "- **决赛**：2024年7月26日\n" + "\n" + "### \uD83D\uDCCD 比赛地点\n" + "\n" + "- **体育中心足球场**\n" + "- 南山区, 广东省\n" + "\n" + "### \uD83D\uDCDD 报名要求\n" + "\n" + "- 必须是在校大学生\n" + "- 每队人数不超过 **11人**\n" + "- 提供完整的团队信息和联系方式\n" + "\n" + "### \uD83C\uDF96\uFE0F 奖项设置\n" + "\n" + "- **冠军**：奖金 **￥5000** + 奖杯\n" + "- **亚军**：奖金 **￥3000**\n" + "- **季军**：奖金 **￥1000**\n" + "\n" + "<img src=\"http://10.16.88.247:8082/image/soccer.png\" width=\"500\">\n" + "\n" + "### \uD83D\uDCE2 如何报名\n" + "\n" + "请填写报名表格。\n" + "\n" + "```markdown\n" + "- 团队名称：\n" + "- 队长姓名：\n" + "- 联系电话：\n" + "- 队员名单：\n" + "  - 球员1\n" + "  - 球员2\n" + "  - ...\n" + "```\n" + "\n" + "### ? 报名截止日期\n" + "\n" + "**2024年6月30日**\n" + "\n" + "更多相关信息请查看以下链接：\n" + "\n" + "[足球](https://soccer.hupu.com/)\n" + "\n" + "------\n" + "\n" + "\uD83D\uDD25 **不要错过这个展现你们足球才华的机会！快来报名参加吧！** \uD83D\uDD25\n" + "\n" + "------\n" + "\n");
        soccer.setAuthor(author);
        soccer.setStartTime(LocalDateTime.of(2021, 6, 15, 0, 0, 0));
        soccer.setEndTime(LocalDateTime.of(2021, 6, 16, 0, 0, 0));
        soccer.setAbstractEnrollment(formEnrollment);
        formEnrollment.setEvent(soccer);
        formEnrollment.setStartTime(LocalDateTime.of(2021, 6, 1, 0, 0, 0));
        formEnrollment.setEndTime(LocalDateTime.of(2024, 6, 1, 0, 0, 0));
        formEnrollment.setDefinedFormEntries(new ArrayList<>());
        soccer.setAbstractEnrollment(formEnrollment);
        // add FormEntry
        List<DefinedFormEntry> definedFormEntries = formEnrollment.getDefinedFormEntries();
        //name
        DefinedFormEntry definedFormEntry = new DefinedFormEntry();
        definedFormEntry.setEntryId(0);
        definedFormEntry.setName("name");
        definedFormEntry.setType("input");
        definedFormEntry.setRequired(true);
        definedFormEntries.add(definedFormEntry);
        //sid
        definedFormEntry = new DefinedFormEntry();
        definedFormEntry.setEntryId(1);
        definedFormEntry.setName("sid");
        definedFormEntry.setType("input");
        definedFormEntry.setRequired(true);
        definedFormEntries.add(definedFormEntry);
        //gender
        definedFormEntry = new DefinedFormEntry();
        definedFormEntry.setOptions(new ArrayList<>());
        List<String> options = definedFormEntry.getOptions();
        options.add("male");
        options.add("female");
        definedFormEntry.setEntryId(2);
        definedFormEntry.setName("gender");
        definedFormEntry.setType("select");
        definedFormEntry.setRequired(true);
        definedFormEntries.add(definedFormEntry);
        return soccer;
    }


}
