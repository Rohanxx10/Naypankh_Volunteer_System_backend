package com.volunteer.config;

import com.volunteer.entity.Event;
import com.volunteer.entity.User;
import com.volunteer.entity.Volunteer;
import com.volunteer.repository.EventRepository;
import com.volunteer.repository.UserRepository;
import com.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedVolunteers();
        seedEvents();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        userRepository.saveAll(List.of(
                User.builder().email("admin@volunteer.org").password(passwordEncoder.encode("admin123"))
                        .fullName("Priya Sharma").role(User.Role.ADMIN).build(),
                User.builder().email("manager@volunteer.org").password(passwordEncoder.encode("manager123"))
                        .fullName("Rajesh Kumar").role(User.Role.ADMIN).build()
        ));
    }

    private void seedVolunteers() {
        if (volunteerRepository.count() > 0) return;

        int year = LocalDate.now().getYear();

        List<Volunteer> volunteers = Arrays.asList(
                createVolunteer(
                        "Aaradhya", "Singh", "aaradhya.s@email.com", "98765-41001",
                        "Mumbai", "Maharashtra",
                        Volunteer.VolunteerStatus.APPROVED,
                        Arrays.asList("Teaching", "Mentoring"),
                        "WEEKENDS", 8, 45,
                        LocalDate.of(year, 1, 10)
                ),
                createVolunteer(
                        "Arjun", "Patel", "arjun.p@email.com", "98765-41002",
                        "Ahmedabad", "Gujarat",
                        Volunteer.VolunteerStatus.APPROVED,
                        Arrays.asList("IT Support", "Web Design"),
                        "WEEKDAYS", 10, 62,
                        LocalDate.of(year, 2, 14)
                ),
                createVolunteer(
                        "Ananya", "Gupta", "ananya.g@email.com", "98765-41003",
                        "Delhi", "Delhi",
                        Volunteer.VolunteerStatus.APPROVED,
                        Arrays.asList("Cooking", "Event Planning"),
                        "BOTH", 6, 38,
                        LocalDate.of(year, 2, 28)
                ),
                createVolunteer(
                        "Vikram", "Reddy", "vikram.r@email.com", "98765-41004",
                        "Hyderabad", "Telangana",
                        Volunteer.VolunteerStatus.PENDING,
                        Arrays.asList("Medical", "First Aid"),
                        "FLEXIBLE", 12, 0,
                        LocalDate.of(year, 3, 5)
                ),
                createVolunteer(
                        "Meera", "Iyer", "meera.i@email.com", "98765-41005",
                        "Chennai", "Tamil Nadu",
                        Volunteer.VolunteerStatus.PENDING,
                        Arrays.asList("Administration", "Communication"),
                        "WEEKENDS", 5, 0,
                        LocalDate.of(year, 4, 18)
                ),
                createVolunteer(
                        "Rohan", "Deshmukh", "rohan.d@email.com", "98765-41006",
                        "Pune", "Maharashtra",
                        Volunteer.VolunteerStatus.APPROVED,
                        Arrays.asList("Construction", "Maintenance"),
                        "WEEKDAYS", 15, 91,
                        LocalDate.of(year, 4, 22)
                ),
                createVolunteer(
                        "Kavita", "Nair", "kavita.n@email.com", "98765-41007",
                        "Bengaluru", "Karnataka",
                        Volunteer.VolunteerStatus.INACTIVE,
                        Arrays.asList("Counseling", "Social Work"),
                        "BOTH", 8, 24,
                        LocalDate.of(year, 5, 3)
                ),
                createVolunteer(
                        "Suresh", "Choudhury", "suresh.c@email.com", "98765-41008",
                        "Kolkata", "West Bengal",
                        Volunteer.VolunteerStatus.APPROVED,
                        Arrays.asList("Driving", "Logistics"),
                        "WEEKDAYS", 20, 110,
                        LocalDate.of(year, 5, 30)
                ),
                createVolunteer(
                        "Divya", "Joshi", "divya.j@email.com", "98765-41009",
                        "Jaipur", "Rajasthan",
                        Volunteer.VolunteerStatus.REJECTED,
                        Arrays.asList("Photography", "Media"),
                        "WEEKENDS", 4, 0,
                        LocalDate.of(year, 6, 7)
                ),
                createVolunteer(
                        "Amit", "Thakur", "amit.t@email.com", "98765-41010",
                        "Lucknow", "Uttar Pradesh",
                        Volunteer.VolunteerStatus.APPROVED,
                        Arrays.asList("Music", "Arts"),
                        "FLEXIBLE", 7, 55,
                        LocalDate.of(year, 6, 15)
                )
        );

        volunteerRepository.saveAll(volunteers);
    }
    private Volunteer createVolunteer(String first, String last, String email, String phone,
                                      String city, String state, Volunteer.VolunteerStatus status,
                                      List<String> skills, String availability, int hours, int totalHours) {
        return createVolunteer(first, last, email, phone, city, state, status, skills, availability, hours, totalHours, null);
    }

    private Volunteer createVolunteer(String first, String last, String email, String phone,
                                      String city, String state, Volunteer.VolunteerStatus status,
                                      List<String> skills, String availability, int hours, int totalHours,
                                      LocalDate registrationDate) {
        Volunteer v = new Volunteer();
        v.setFirstName(first);
        v.setLastName(last);
        v.setEmail(email);
        v.setPhone(phone);
        v.setCity(city);
        v.setState(state);
        v.setStatus(status);
        v.setSkills(skills);
        v.setAvailability(availability);
        v.setHoursPerWeek(hours);
        v.setTotalHoursLogged(totalHours);
        v.setMotivation("I want to give back to the community and make a positive difference.");
        v.setEmergencyContactName("Emergency Contact");
        v.setEmergencyContactPhone("98765-99999");
        v.setDateOfBirth(LocalDate.of(1990, 1, 15));
        if (registrationDate != null) v.setRegistrationDate(registrationDate);
        return v;
    }

    private void seedEvents() {
        if (eventRepository.count() > 0) return;

        List<Event> events = Arrays.asList(
                createEvent("Food Donation Drive - Annadaan Abhiyaan",
                        "Collecting and distributing food to families in need. Aao milkar karein seva!",
                        "Shivaji Park, Mumbai",
                        LocalDateTime.now().plusDays(7),
                        LocalDateTime.now().plusDays(7).plusHours(6),
                        50, Event.EventStatus.UPCOMING, "Community Service"),

                createEvent("Youth Mentorship - Yuva Margdarshan",
                        "Mentoring underprivileged youth in academics and life skills. Unka future bright banayein!",
                        "Connaught Place, Delhi",
                        LocalDateTime.now().plusDays(14),
                        LocalDateTime.now().plusDays(14).plusHours(4),
                        20, Event.EventStatus.UPCOMING, "Education"),

                createEvent("Clean City Drive - Swachh Bharat",
                        "Cleaning and beautifying local parks. Let's make India clean and green!",
                        "Cubbon Park, Bengaluru",
                        LocalDateTime.now().plusDays(3),
                        LocalDateTime.now().plusDays(3).plusHours(4),
                        30, Event.EventStatus.UPCOMING, "Environment"),

                createEvent("Elder Care Program - Vruddha Seva",
                        "Visiting and supporting elderly residents at local care homes. Unki muskurahat hai humara reward!",
                        "Anand Care Home, Chennai",
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now().minusDays(5).plusHours(3),
                        15, Event.EventStatus.COMPLETED, "Healthcare"),

                createEvent("Housing for All - Ghar Sabko",
                        "Building affordable housing for low-income families. Chalo unka sapna poora karein!",
                        "Salt Lake, Kolkata",
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().plusDays(5),
                        40, Event.EventStatus.ONGOING, "Construction"),

                createEvent("Kids Science Workshop - Vigyan Mela",
                        "Teaching basic programming to children aged 10-16. Science ko banayein interesting!",
                        "IIT Delhi Campus",
                        LocalDateTime.now().plusDays(21),
                        LocalDateTime.now().plusDays(21).plusHours(5),
                        12, Event.EventStatus.UPCOMING, "Education"),

                createEvent("Blood Donation Camp - Raktdaan Shivir",
                        "Donate blood, save lives! Aao khoon daan karein aur kisi ki jaan bachayein!",
                        "Apollo Hospital, Mumbai",
                        LocalDateTime.now().plusDays(10),
                        LocalDateTime.now().plusDays(10).plusHours(4),
                        40, Event.EventStatus.UPCOMING, "Healthcare"),

                createEvent("Tree Plantation - Pedh Lagao",
                        "Planting trees for a greener future. Hariyali laao, environment bachao!",
                        "Sunder Nursery, Delhi",
                        LocalDateTime.now().plusDays(25),
                        LocalDateTime.now().plusDays(25).plusHours(3),
                        60, Event.EventStatus.UPCOMING, "Environment"),

                createEvent("Digital Literacy - Digital Saksharta",
                        "Teaching basic computer skills to senior citizens. Technology ko banayein unka dost!",
                        "Community Center, Pune",
                        LocalDateTime.now().plusDays(30),
                        LocalDateTime.now().plusDays(30).plusHours(4),
                        25, Event.EventStatus.UPCOMING, "Education"),

                createEvent("Daily Food Distribution - Rozana Annadaan",
                        "Daily food distribution for street children and needy families. Kisi ko bhukha na rahne dein!",
                        "Railway Station Area, Mumbai",
                        LocalDateTime.now().plusDays(12),
                        LocalDateTime.now().plusDays(12).plusHours(5),
                        35, Event.EventStatus.UPCOMING, "Community Service")
        );

        eventRepository.saveAll(events);
    }

    private Event createEvent(String title, String desc, String location,
                              LocalDateTime start, LocalDateTime end,
                              int maxVols, Event.EventStatus status, String category) {
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(desc);
        e.setLocation(location);
        e.setStartDateTime(start);
        e.setEndDateTime(end);
        e.setMaxVolunteers(maxVols);
        e.setRegisteredVolunteers((int)(maxVols * 0.6));
        e.setStatus(status);
        e.setCategory(category);
        return e;
    }
}