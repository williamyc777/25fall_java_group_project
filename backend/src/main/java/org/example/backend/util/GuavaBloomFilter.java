package org.example.backend.util;

//@Configuration
public class GuavaBloomFilter {
//    private final Logger logger = Logger.getLogger(GuavaBloomFilter.class.getName());
//    private BloomFilter<String> bloomFilter;
//
//    @Value("${spring.bloom-filter.capacity}")
//    private long capacity;
//
//    @Value("${spring.bloom-filter.error-rate}")
//    private double errorRate;
//
//    @Autowired
//    private com.example.dormitoryselection.api.BuildingRepository BuildingRepository;
//    @Autowired
//    private StudentRepository studentRepository;
//    @Autowired
//    private RoomRepository roomRepository;
//    @Autowired
//    private StudentProfileRepository studentProfileRepository;
//    @Autowired
//    private StudentTeamRepository studentTeamRepository;
//    @Autowired
//    private ZoningRepository zoningRepository;

//    @Bean
//    public BloomFilter<String> bloomFilter(){
//        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), capacity, errorRate);
//        this.bloomFilter = bloomFilter;
//        return bloomFilter;
//    }

//    @PostConstruct
//    private void constructBloomFilter() {
//        logger.info("开始构建布隆过滤器");
//        List<Building> buildings = BuildingRepository.findAll();
//        List<Student> students = studentRepository.findAll();
//        List<Room> rooms = roomRepository.findAll();
//        List<StudentProfile> studentProfiles = studentProfileRepository.findAll();
//        List<StudentTeam> studentTeams = studentTeamRepository.findAll();
//        List<Zoning> zonings = zoningRepository.findAll();
//        for (Building building : buildings) {
//            bloomFilter.put("building" + building.getId());
//        }
//        for (Student student : students) {
//            bloomFilter.put("student" + student.getStudentId());
//        }
//        for (Room room : rooms) {
//            bloomFilter.put("room" + room.getId());
//        }
//        for (StudentProfile studentProfile : studentProfiles) {
//            bloomFilter.put("studentProfile" + studentProfile.getId());
//        }
//        for (StudentTeam studentTeam : studentTeams) {
//            bloomFilter.put("studentTeam" + studentTeam.getId());
//        }
//        for (Zoning zoning : zonings) {
//            bloomFilter.put("zoning" + zoning.getId());
//        }
//        logger.info("布隆过滤器构建完成");
//    }

}

