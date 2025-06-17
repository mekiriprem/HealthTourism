package hospital.tourism.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import hospital.tourism.Dto.BookingRequest;
import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Entity.Booking;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.Entity.Physio;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.Entity.Translators;
import hospital.tourism.Entity.users;
import hospital.tourism.repo.BookingRepo;
import hospital.tourism.repo.DoctorsRepo;
import hospital.tourism.repo.PhysioRepo;
import hospital.tourism.repo.SpaservicesRepo;
import hospital.tourism.repo.TranslatorsRepo;
import hospital.tourism.repo.chefsRepo;
import hospital.tourism.repo.labtestsRepo;
import hospital.tourism.repo.usersrepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepository;
    private final usersrepo userRepository;
    private final chefsRepo chefRepository;
    private final PhysioRepo physioRepository;
    private final TranslatorsRepo translatorRepository;
    private final SpaservicesRepo spaServiceRepository;
    private final DoctorsRepo doctorRepository;
    private final labtestsRepo labtestsRepository;
    @Autowired
    private JavaMailSender javaMailSender;

	/*
	public Booking bookService(
	        Long userId,
	        Long serviceId,
	        String serviceType,
	        List<String> slotInfo,
	        String paymentMode,
	        String bookingType,
	        String remarks
	       
	) {
	    users user = userRepository.findById(userId)
	            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
	
	    Booking booking = new Booking();
	    booking.setUser(user);
	    booking.setSlotInfo(slotInfo);
	    booking.setBookingDate(LocalDateTime.now());
	    booking.setBookingType(bookingType);
	   
	    booking.setBookingStatus("Pending");
	    booking.setAdditionalRemarks(remarks);
	    booking.setPaymentMode(paymentMode);
	
	    double price;
	
	    switch (serviceType.toLowerCase()) {
	        case "chef" -> {
	            Chefs chef = chefRepository.findById(serviceId)
	                    .orElseThrow(() -> new EntityNotFoundException("Chef not found"));
	            booking.setChef(chef);
	            price = chef.getPrice();
	        }
	        case "physio" -> {
	            Physio physio = physioRepository.findById(serviceId)
	                    .orElseThrow(() -> new EntityNotFoundException("Physio not found"));
	            booking.setPhysio(physio);
	            price = physio.getPrice();
	        }
	        case "translator" -> {
	            Translators translator = translatorRepository.findById(serviceId)
	                    .orElseThrow(() -> new EntityNotFoundException("Translator not found"));
	            booking.setTranslator(translator);
	            price = translator.getPrice();
	        }
	        case "spa" -> {
	            SpaServicese spa = spaServiceRepository.findById(serviceId)
	                    .orElseThrow(() -> new EntityNotFoundException("Spa service not found"));
	            booking.setSpa(spa);
	            price = spa.getPrice();
	        }
	        case "doctor" -> {
	            Doctors doctor = doctorRepository.findById(serviceId)
	                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
	            booking.setDoctors(doctor);
	            price = doctor.getPrice();
	        }
	        case "labtests" -> {
	            Labtests labtest = labtestsRepository.findById(serviceId)
	                    .orElseThrow(() -> new EntityNotFoundException("Lab test not found"));
	            booking.setLabtests(labtest);
	            price = labtest.getTestPrice();
	        }
	        default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
	    }
	
	    booking.setBookingAmount(price);
	
	    if ("offline".equalsIgnoreCase(paymentMode)) {
	        booking.setPaymentStatus("Unpaid");
	    } else {
	        booking.setPaymentStatus("Paid");
	    }
	
	    booking.setDiscountApplied("None");
	
	    return bookingRepository.save(booking);
	}
	*/
    
//    
//    public BookingRequest bookService(
//            Long userId,
//            Long serviceId,
//            String serviceType,
//            LocalDateTime bookingStartDate,
//            LocalDateTime bookingEndDate,
//            String paymentMode,
//            String bookingType,
//            String remarks
//    ) {
//        users user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//
//        Booking booking = new Booking();
//        booking.setUser(user);
//        booking.setBookingStartTime(bookingStartDate);
//        booking.setBookingEndTime(bookingEndDate);
//        booking.setBookingDate(LocalDateTime.now());
//        booking.setBookingType(bookingType);
//        booking.setBookingStatus("Pending");
//        booking.setAdditionalRemarks(remarks);
//        booking.setPaymentMode(paymentMode);
//
//        double price;
//
//        switch (serviceType.toLowerCase()) {
//            case "chef" -> {
//                Chefs chef = chefRepository.findById(serviceId)
//                        .orElseThrow(() -> new EntityNotFoundException("Chef not found"));
//                booking.setChef(chef);
//                price = chef.getPrice();
//            }
//            case "physio" -> {
//                Physio physio = physioRepository.findById(serviceId)
//                        .orElseThrow(() -> new EntityNotFoundException("Physio not found"));
//                booking.setPhysio(physio);
//                price = physio.getPrice();
//            }
//            case "translator" -> {
//                Translators translator = translatorRepository.findById(serviceId)
//                        .orElseThrow(() -> new EntityNotFoundException("Translator not found"));
//                booking.setTranslator(translator);
//                price = translator.getPrice();
//            }
//            case "spa" -> {
//                SpaServicese spa = spaServiceRepository.findById(serviceId)
//                        .orElseThrow(() -> new EntityNotFoundException("Spa service not found"));
//                booking.setSpa(spa);
//                price = spa.getPrice();
//            }
//            case "doctor" -> {
//                Doctors doctor = doctorRepository.findById(serviceId)
//                        .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
//                booking.setDoctors(doctor);
//                price = doctor.getPrice();
//            }
//            case "labtests" -> {
//                Labtests labtest = labtestsRepository.findById(serviceId)
//                        .orElseThrow(() -> new EntityNotFoundException("Lab test not found"));
//                booking.setLabtests(labtest);
//                price = labtest.getTestPrice();
//            }
//            default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
//        }
//
//        booking.setBookingAmount(price);
//        booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
//        booking.setDiscountApplied("None");
//
//        Booking savedBooking = bookingRepository.save(booking);
//
//        // Build response DTO directly here
//        BookingRequest response = new BookingRequest();
//        response.setBookingId(savedBooking.getBookingId());
//        response.setBookingDate(savedBooking.getBookingDate());
//        response.setSlotIdLong(savedBooking.getSlotIdLong());
//        response.setBookingStatus(savedBooking.getBookingStatus());
//        response.setBookingType(savedBooking.getBookingType());
//        response.setBookingAmount(savedBooking.getBookingAmount());
//        response.setPaymentMode(savedBooking.getPaymentMode());
//        response.setPaymentStatus(savedBooking.getPaymentStatus());
//        response.setDiscountApplied(savedBooking.getDiscountApplied());
//        response.setBookingStartTime(savedBooking.getBookingStartTime());
//        response.setBookingEndTime(savedBooking.getBookingEndTime());
//        response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());
//
//        if (savedBooking.getPhysio() != null) {
//            response.setPhysioId(savedBooking.getPhysio().getPhysioId());
//            response.setPhysioName(savedBooking.getPhysio().getPhysioName());
//        }
//        if (savedBooking.getTranslator() != null) {
//            response.setTranslatorId(savedBooking.getTranslator().getTranslatorID());
//            response.setTranslatorName(savedBooking.getTranslator().getTranslatorName());
//        }
//        if (savedBooking.getSpa() != null) {
//            response.setSpaId(savedBooking.getSpa().getServiceId());
//            response.setSpaName(savedBooking.getSpa().getServiceName());
//        }
//        if (savedBooking.getDoctors() != null) {
//            response.setDoctorId(savedBooking.getDoctors().getId());
//            response.setDoctorName(savedBooking.getDoctors().getName());
//        }
//        if (savedBooking.getLabtests() != null) {
//            response.setLabtestId(savedBooking.getLabtests().getId());
//            response.setLabtestName(savedBooking.getLabtests().getTestTitle());
//        }
//        if (savedBooking.getUser() != null) {
//            response.setUserId(savedBooking.getUser().getId());
//            response.setUserName(savedBooking.getUser().getName());
//        }
//        if (savedBooking.getChef() != null) {
//            response.setChefId(savedBooking.getChef().getChefID());
//            response.setChefName(savedBooking.getChef().getChefName());
//        }
//
//        return response;
//    }
    
    
    
    public BookingRequest bookService(
            Long userId,
            Long serviceId,
            String serviceType,
            LocalDateTime bookingStartDate,
            LocalDateTime bookingEndDate,
            String paymentMode,
            String bookingType,
            String remarks,
            double bookingAmount 
            
    ) {
        users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookingStartTime(bookingStartDate);
        booking.setBookingEndTime(bookingEndDate);
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingType(bookingType);
        booking.setBookingStatus("Pending");
        booking.setAdditionalRemarks(remarks);
        booking.setPaymentMode(paymentMode);

        switch (serviceType.toLowerCase()) {
            case "chef" -> {
                Chefs chef = chefRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Chef not found"));
                booking.setChef(chef);
            }
            case "physio" -> {
                Physio physio = physioRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Physio not found"));
                booking.setPhysio(physio);
            }
            case "translator" -> {
                Translators translator = translatorRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Translator not found"));
                booking.setTranslator(translator);
            }
            case "spa" -> {
                SpaServicese spa = spaServiceRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Spa service not found"));
                booking.setSpa(spa);
            }
            case "doctor" -> {
                Doctors doctor = doctorRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
                booking.setDoctors(doctor);
            }
            case "labtests" -> {
                Labtests labtest = labtestsRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Lab test not found"));
                booking.setLabtests(labtest);
            }
            default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
        }

        booking.setBookingAmount(bookingAmount); // ✅ Use frontend-calculated amount
        booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
        booking.setDiscountApplied("Applied from frontend based on multi-day booking");

        Booking savedBooking = bookingRepository.save(booking);

        BookingRequest response = new BookingRequest();
        response.setBookingId(savedBooking.getBookingId());
        response.setBookingDate(savedBooking.getBookingDate());
        response.setSlotIdLong(savedBooking.getSlotIdLong());
        response.setBookingStatus(savedBooking.getBookingStatus());
        response.setBookingType(savedBooking.getBookingType());
        response.setBookingAmount(savedBooking.getBookingAmount());
        response.setPaymentMode(savedBooking.getPaymentMode());
        response.setPaymentStatus(savedBooking.getPaymentStatus());
        response.setDiscountApplied(savedBooking.getDiscountApplied());
        response.setBookingStartTime(savedBooking.getBookingStartTime());
        response.setBookingEndTime(savedBooking.getBookingEndTime());
        response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());

        if (savedBooking.getPhysio() != null) {
            response.setPhysioId(savedBooking.getPhysio().getPhysioId());
            response.setPhysioName(savedBooking.getPhysio().getPhysioName());
        }
        if (savedBooking.getTranslator()!= null) {
            response.setTranslatorId(savedBooking.getTranslator().getTranslatorID());
            response.setTranslatorName(savedBooking.getTranslator().getTranslatorName());
        }
        if (savedBooking.getSpa() != null) {
            response.setSpaId(savedBooking.getSpa().getServiceId());
            response.setSpaName(savedBooking.getSpa().getServiceName());
        }
        if (savedBooking.getDoctors() != null) {
            response.setDoctorId(savedBooking.getDoctors().getId());
            response.setDoctorName(savedBooking.getDoctors().getName());
        }
        if (savedBooking.getLabtests() != null) {
            response.setLabtestId(savedBooking.getLabtests().getId());
            response.setLabtestName(savedBooking.getLabtests().getTestTitle());
        }
        if (savedBooking.getUser() != null) {
            response.setUserId(savedBooking.getUser().getId());
            response.setUserName(savedBooking.getUser().getName());
        }
        if (savedBooking.getChef() != null) {
            response.setChefId(savedBooking.getChef().getChefID());
            response.setChefName(savedBooking.getChef().getChefName());
        }

        return response;
    }

    
    
//    public BookingRequest bookServices(
//            Long userId,
//            String serviceType,
//            LocalDateTime bookingStartDate,
//            LocalDateTime bookingEndDate,
//            String paymentMode,
//            String bookingType,
//            String remarks
//    ) {
//        users user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//
//        Booking booking = new Booking();
//        booking.setUser(user);
//        booking.setBookingStartTime(bookingStartDate);
//        booking.setBookingEndTime(bookingEndDate);
//        booking.setBookingDate(LocalDateTime.now());
//        booking.setBookingType(bookingType);
//        booking.setBookingStatus("Pending");
//        booking.setAdditionalRemarks(remarks);
//        booking.setPaymentMode(paymentMode);
//
//        double price;
//
//        switch (serviceType.toLowerCase()) {
//            case "chef" -> {
//                Chefs chef = chefRepository.findAll().stream().findFirst()
//                        .orElseThrow(() -> new EntityNotFoundException("No chefs available"));
//                booking.setChef(chef);
//                price = chef.getPrice();
//            }
//            case "physio" -> {
//                Physio physio = physioRepository.findAll().stream().findFirst()
//                        .orElseThrow(() -> new EntityNotFoundException("No physios available"));
//                booking.setPhysio(physio);
//                price = physio.getPrice();
//            }
//            case "translator" -> {
//                Translators translator = translatorRepository.findAll().stream().findFirst()
//                        .orElseThrow(() -> new EntityNotFoundException("No translators available"));
//                booking.setTranslator(translator);
//                 price = translator.getPrice() != null ? translator.getPrice() : 0.0;
//
//            }
//            case "spa" -> {
//                SpaServicese spa = spaServiceRepository.findAll().stream().findFirst()
//                        .orElseThrow(() -> new EntityNotFoundException("No spa services available"));
//                booking.setSpa(spa);
//                price = spa.getPrice();
//            }
//            case "doctor" -> {
//                Doctors doctor = doctorRepository.findAll().stream().findFirst()
//                        .orElseThrow(() -> new EntityNotFoundException("No doctors available"));
//                booking.setDoctors(doctor);
//                price = doctor.getPrice();
//            }
//            case "labtests" -> {
//                Labtests labtest = labtestsRepository.findAll().stream().findFirst()
//                        .orElseThrow(() -> new EntityNotFoundException("No lab tests available"));
//                booking.setLabtests(labtest);
//                price = labtest.getTestPrice();
//            }
//            default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
//        }
//
//        booking.setBookingAmount(price);
//        booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
//        booking.setDiscountApplied("None");
//
//        Booking savedBooking = bookingRepository.save(booking);
//
//        // Populate and return DTO
//        BookingRequest response = new BookingRequest();
//        response.setBookingId(savedBooking.getBookingId());
//        response.setBookingDate(savedBooking.getBookingDate());
//        response.setSlotIdLong(savedBooking.getSlotIdLong());
//        response.setBookingStatus(savedBooking.getBookingStatus());
//        response.setBookingType(savedBooking.getBookingType());
//        response.setBookingAmount(savedBooking.getBookingAmount());
//        response.setPaymentMode(savedBooking.getPaymentMode());
//        response.setPaymentStatus(savedBooking.getPaymentStatus());
//        response.setDiscountApplied(savedBooking.getDiscountApplied());
//        response.setBookingStartTime(savedBooking.getBookingStartTime());
//        response.setBookingEndTime(savedBooking.getBookingEndTime());
//        response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());
//
//        if (savedBooking.getPhysio() != null) {
//            response.setPhysioId(savedBooking.getPhysio().getPhysioId());
//            response.setPhysioName(savedBooking.getPhysio().getPhysioName());
//        }
//        if (savedBooking.getTranslator() != null) {
//            response.setTranslatorId(savedBooking.getTranslator().getTranslatorID());
//            response.setTranslatorName(savedBooking.getTranslator().getTranslatorName());
//        }
//        if (savedBooking.getSpa() != null) {
//            response.setSpaId(savedBooking.getSpa().getServiceId());
//            response.setSpaName(savedBooking.getSpa().getServiceName());
//        }
//        if (savedBooking.getDoctors() != null) {
//            response.setDoctorId(savedBooking.getDoctors().getId());
//            response.setDoctorName(savedBooking.getDoctors().getName());
//        }
//        if (savedBooking.getLabtests() != null) {
//            response.setLabtestId(savedBooking.getLabtests().getId());
//            response.setLabtestName(savedBooking.getLabtests().getTestTitle());
//        }
//        if (savedBooking.getUser() != null) {
//            response.setUserId(savedBooking.getUser().getId());
//            response.setUserName(savedBooking.getUser().getName());
//        }
//        if (savedBooking.getChef() != null) {
//            response.setChefId(savedBooking.getChef().getChefID());
//            response.setChefName(savedBooking.getChef().getChefName());
//        }
//
//        return response;
//    }
//    
//    public List<BookingRequest> bookMultipleServices(
//            Long userId,
//            List<String> serviceTypes,
//            LocalDateTime bookingStartDate,
//            LocalDateTime bookingEndDate,
//            String paymentMode,
//            String bookingType,
//            String remarks
//    ) {
//        users user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//
//        List<BookingRequest> bookingResponses = new ArrayList<>();
//
//        for (String serviceType : serviceTypes) {
//            Booking booking = new Booking();
//            booking.setUser(user);
//            booking.setBookingStartTime(bookingStartDate);
//            booking.setBookingEndTime(bookingEndDate);
//            booking.setBookingDate(LocalDateTime.now());
//            booking.setBookingType(bookingType);
//            booking.setBookingStatus("Pending");
//            booking.setAdditionalRemarks(remarks);
//            booking.setPaymentMode(paymentMode);
//
//            double price;
//
//            switch (serviceType.toLowerCase()) {
//                case "chef" -> {
//                    Chefs chef = chefRepository.findAll().stream().findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No chefs available"));
//                    booking.setChef(chef);
//                    price = chef.getPrice();
//                }
//                case "physio" -> {
//                    Physio physio = physioRepository.findAll().stream().findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No physios available"));
//                    booking.setPhysio(physio);
//                    price = physio.getPrice();
//                }
//                case "translator" -> {
//                    Translators translator = translatorRepository.findAll().stream().findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No translators available"));
//                    booking.setTranslator(translator);
//                    price = translator.getPrice() != null ? translator.getPrice() : 0.0;
//                }
//                case "spa" -> {
//                    SpaServicese spa = spaServiceRepository.findAll().stream().findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No spa services available"));
//                    booking.setSpa(spa);
//                    price = spa.getPrice();
//                }
//                case "doctor" -> {
//                    Doctors doctor = doctorRepository.findAll().stream().findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No doctors available"));
//                    booking.setDoctors(doctor);
//                    price = doctor.getPrice();
//                }
//                case "labtests" -> {
//                    Labtests labtest = labtestsRepository.findAll().stream().findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No lab tests available"));
//                    booking.setLabtests(labtest);
//                    price = labtest.getTestPrice();
//                }
//                default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
//            }
//
//            booking.setBookingAmount(price);
//            booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
//            booking.setDiscountApplied("None");
//
//            Booking savedBooking = bookingRepository.save(booking);
//
//            BookingRequest response = new BookingRequest();
//            response.setBookingId(savedBooking.getBookingId());
//            response.setBookingDate(savedBooking.getBookingDate());
//            response.setSlotIdLong(savedBooking.getSlotIdLong());
//            response.setBookingStatus(savedBooking.getBookingStatus());
//            response.setBookingType(savedBooking.getBookingType());
//            response.setBookingAmount(savedBooking.getBookingAmount());
//            response.setPaymentMode(savedBooking.getPaymentMode());
//            response.setPaymentStatus(savedBooking.getPaymentStatus());
//            response.setDiscountApplied(savedBooking.getDiscountApplied());
//            response.setBookingStartTime(savedBooking.getBookingStartTime());
//            response.setBookingEndTime(savedBooking.getBookingEndTime());
//            response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());
//
//            if (savedBooking.getPhysio() != null) {
//                response.setPhysioId(savedBooking.getPhysio().getPhysioId());
//                response.setPhysioName(savedBooking.getPhysio().getPhysioName());
//            }
//            if (savedBooking.getTranslator() != null) {
//                response.setTranslatorId(savedBooking.getTranslator().getTranslatorID());
//                response.setTranslatorName(savedBooking.getTranslator().getTranslatorName());
//            }
//            if (savedBooking.getSpa() != null) {
//                response.setSpaId(savedBooking.getSpa().getServiceId());
//                response.setSpaName(savedBooking.getSpa().getServiceName());
//            }
//            if (savedBooking.getDoctors() != null) {
//                response.setDoctorId(savedBooking.getDoctors().getId());
//                response.setDoctorName(savedBooking.getDoctors().getName());
//            }
//            if (savedBooking.getLabtests() != null) {
//                response.setLabtestId(savedBooking.getLabtests().getId());
//                response.setLabtestName(savedBooking.getLabtests().getTestTitle());
//            }
//            if (savedBooking.getUser() != null) {
//                response.setUserId(savedBooking.getUser().getId());
//                response.setUserName(savedBooking.getUser().getName());
//            }
//            if (savedBooking.getChef() != null) {
//                response.setChefId(savedBooking.getChef().getChefID());
//                response.setChefName(savedBooking.getChef().getChefName());
//            }
//
//            bookingResponses.add(response);
//        }
//
//        return bookingResponses;
//    }
//
//    
//    public List<BookingRequest> bookMultipleServices(
//            Long userId,
//            List<String> serviceTypes,
//            LocalDateTime bookingStartDate,
//            LocalDateTime bookingEndDate,
//            String paymentMode,
//            String bookingType,
//            String remarks
//    ) {
//        users user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//
//        List<BookingRequest> bookingResponses = new ArrayList<>();
//
//        for (String serviceType : serviceTypes) {
//            Booking booking = new Booking();
//            booking.setUser(user);
//            booking.setBookingStartTime(bookingStartDate);
//            booking.setBookingEndTime(bookingEndDate);
//            booking.setBookingDate(LocalDateTime.now());
//            booking.setBookingType(bookingType);
//            booking.setBookingStatus("Pending");
//            booking.setAdditionalRemarks(remarks);
//            booking.setPaymentMode(paymentMode);
//
//            double price;
//
//            switch (serviceType.toLowerCase()) {
//                case "chef" -> {
//                    Chefs availableChef = chefRepository.findAll().stream()
//                            .filter(chef -> bookingRepository
//                                    .findChefBookingsInTimeRange(chef.getChefID(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available chefs in the selected time slot"));
//                    booking.setChef(availableChef);
//                    price = availableChef.getPrice();
//                }
//                case "physio" -> {
//                    Physio availablePhysio = physioRepository.findAll().stream()
//                            .filter(p -> bookingRepository
//                                    .findPhysioBookingsInTimeRange(p.getPhysioId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available physios"));
//                    booking.setPhysio(availablePhysio);
//                    price = availablePhysio.getPrice();
//                }
//                case "translator" -> {
//                    Translators availableTranslator = translatorRepository.findAll().stream()
//                            .filter(t -> bookingRepository
//                                    .findTranslatorBookingsInTimeRange(t.getTranslatorID(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available translators"));
//                    booking.setTranslator(availableTranslator);
//                    price = availableTranslator.getPrice() != null ? availableTranslator.getPrice() : 0.0;
//                }
//                case "spa" -> {
//                    SpaServicese availableSpa = spaServiceRepository.findAll().stream()
//                            .filter(s -> bookingRepository
//                                    .findSpaBookingsInTimeRange(s.getServiceId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available spa services"));
//                    booking.setSpa(availableSpa);
//                    price = availableSpa.getPrice();
//                }
//                case "doctor" -> {
//                    Doctors availableDoctor = doctorRepository.findAll().stream()
//                            .filter(d -> bookingRepository
//                                    .findDoctorBookingsInTimeRange(d.getId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available doctors"));
//                    booking.setDoctors(availableDoctor);
//                    price = availableDoctor.getPrice();
//                }
//                case "labtests" -> {
//                    Labtests availableLab = labtestsRepository.findAll().stream()
//                            .filter(l -> bookingRepository
//                                    .findLabTestBookingsInTimeRange(l.getId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available lab tests"));
//                    booking.setLabtests(availableLab);
//                    price = availableLab.getTestPrice();
//                }
//                default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
//            }
//
//            booking.setBookingAmount(price);
//            booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
//            booking.setDiscountApplied("None");
//
//            Booking savedBooking = bookingRepository.save(booking);
//
//            // Prepare response
//            BookingRequest response = new BookingRequest();
//            response.setBookingId(savedBooking.getBookingId());
//            response.setBookingDate(savedBooking.getBookingDate());
//            response.setSlotIdLong(savedBooking.getSlotIdLong());
//            response.setBookingStatus(savedBooking.getBookingStatus());
//            response.setBookingType(savedBooking.getBookingType());
//            response.setBookingAmount(savedBooking.getBookingAmount());
//            response.setPaymentMode(savedBooking.getPaymentMode());
//            response.setPaymentStatus(savedBooking.getPaymentStatus());
//            response.setDiscountApplied(savedBooking.getDiscountApplied());
//            response.setBookingStartTime(savedBooking.getBookingStartTime());
//            response.setBookingEndTime(savedBooking.getBookingEndTime());
//            response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());
//
//            if (savedBooking.getPhysio() != null) {
//                response.setPhysioId(savedBooking.getPhysio().getPhysioId());
//                response.setPhysioName(savedBooking.getPhysio().getPhysioName());
//            }
//            if (savedBooking.getTranslator() != null) {
//                response.setTranslatorId(savedBooking.getTranslator().getTranslatorID());
//                response.setTranslatorName(savedBooking.getTranslator().getTranslatorName());
//            }
//            if (savedBooking.getSpa() != null) {
//                response.setSpaId(savedBooking.getSpa().getServiceId());
//                response.setSpaName(savedBooking.getSpa().getServiceName());
//            }
//            if (savedBooking.getDoctors() != null) {
//                response.setDoctorId(savedBooking.getDoctors().getId());
//                response.setDoctorName(savedBooking.getDoctors().getName());
//            }
//            if (savedBooking.getLabtests() != null) {
//                response.setLabtestId(savedBooking.getLabtests().getId());
//                response.setLabtestName(savedBooking.getLabtests().getTestTitle());
//            }
//            if (savedBooking.getUser() != null) {
//                response.setUserId(savedBooking.getUser().getId());
//                response.setUserName(savedBooking.getUser().getName());
//            }
//            if (savedBooking.getChef() != null) {
//                response.setChefId(savedBooking.getChef().getChefID());
//                response.setChefName(savedBooking.getChef().getChefName());
//            }
//
//            bookingResponses.add(response);
//        }
//
//        return bookingResponses;
//    }

//    public List<BookingRequest> bookServicePackage(
//    		Long userId,
//            List<String> serviceTypes,
//            List<Double> bookingAmounts,
//            LocalDateTime bookingStartDate,
//            LocalDateTime bookingEndDate,
//            String paymentMode,
//            String bookingType,
//            String remarks
//    ) {
//        users user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//
//        String packageBookingId = UUID.randomUUID().toString(); // Unique ID for the whole package
//        List<BookingRequest> responses = new ArrayList<>();
//
//        for (int i = 0; i < serviceTypes.size(); i++) {
//            String serviceType = serviceTypes.get(i).toLowerCase();
//            double finalPrice = bookingAmounts.get(i);
//
//            Booking booking = new Booking();
//            booking.setUser(user);
//            booking.setBookingStartTime(bookingStartDate);
//            booking.setBookingEndTime(bookingEndDate);
//            booking.setBookingDate(LocalDateTime.now());
//            booking.setBookingType(bookingType);
//            booking.setBookingStatus("Pending");
//            booking.setAdditionalRemarks(remarks);
//            booking.setPaymentMode(paymentMode);
//            booking.setPackageBookingId(packageBookingId); // 🔗 Group under same package
//            booking.setBookingAmount(finalPrice);
//            booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
//            booking.setDiscountApplied("Calculated in frontend");
//
//            switch (serviceType) {
//                case "chef" -> {
//                    Chefs chef = chefRepository.findAll().stream()
//                            .filter(c -> bookingRepository
//                                    .findChefBookingsInTimeRange(c.getChefID(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available chef"));
//                    booking.setChef(chef);
//                }
//                case "physio" -> {
//                    Physio physio = physioRepository.findAll().stream()
//                            .filter(p -> bookingRepository
//                                    .findPhysioBookingsInTimeRange(p.getPhysioId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available physio"));
//                    booking.setPhysio(physio);
//                }
//                case "spa" -> {
//                    SpaServicese spa = spaServiceRepository.findAll().stream()
//                            .filter(s -> bookingRepository
//                                    .findSpaBookingsInTimeRange(s.getServiceId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available spa"));
//                    booking.setSpa(spa);
//                }
//                case "doctor" -> {
//                    Doctors doctor = doctorRepository.findAll().stream()
//                            .filter(d -> bookingRepository
//                                    .findDoctorBookingsInTimeRange(d.getId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available doctor"));
//                    booking.setDoctors(doctor);
//                }
//                case "translator" -> {
//                    Translators translator = translatorRepository.findAll().stream()
//                            .filter(t -> bookingRepository
//                                    .findTranslatorBookingsInTimeRange(t.getTranslatorID(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available translator"));
//                    booking.setTranslator(translator);
//                }
//                case "labtests" -> {
//                    Labtests lab = labtestsRepository.findAll().stream()
//                            .filter(l -> bookingRepository
//                                    .findLabTestBookingsInTimeRange(l.getId(), bookingStartDate, bookingEndDate)
//                                    .isEmpty())
//                            .findFirst()
//                            .orElseThrow(() -> new EntityNotFoundException("No available lab test"));
//                    booking.setLabtests(lab);
//                }
//                default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
//            }
//
//            Booking saved = bookingRepository.save(booking);
//
//            // Prepare response
//            BookingRequest resp = new BookingRequest();
//            resp.setBookingId(saved.getBookingId());
//            resp.setBookingDate(saved.getBookingDate());
//            resp.setSlotIdLong(saved.getSlotIdLong());
//            resp.setBookingStatus(saved.getBookingStatus());
//            resp.setBookingType(saved.getBookingType());
//            resp.setBookingAmount(saved.getBookingAmount());
//            resp.setPaymentMode(saved.getPaymentMode());
//            resp.setPaymentStatus(saved.getPaymentStatus());
//            resp.setDiscountApplied(saved.getDiscountApplied());
//            resp.setBookingStartTime(saved.getBookingStartTime());
//            resp.setBookingEndTime(saved.getBookingEndTime());
//            resp.setAdditionalRemarks(saved.getAdditionalRemarks());
//            resp.setUserId(user.getId());
//            resp.setUserName(user.getName());
//            resp.setPackageBookingId(saved.getPackageBookingId());
//
//            if (saved.getChef() != null) {
//                resp.setChefId(saved.getChef().getChefID());
//                resp.setChefName(saved.getChef().getChefName());
//            }
//            if (saved.getPhysio() != null) {
//                resp.setPhysioId(saved.getPhysio().getPhysioId());
//                resp.setPhysioName(saved.getPhysio().getPhysioName());
//            }
//            if (saved.getSpa() != null) {
//                resp.setSpaId(saved.getSpa().getServiceId());
//                resp.setSpaName(saved.getSpa().getServiceName());
//            }
//            if (saved.getDoctors() != null) {
//                resp.setDoctorId(saved.getDoctors().getId());
//                resp.setDoctorName(saved.getDoctors().getName());
//            }
//            if (saved.getTranslator() != null) {
//                resp.setTranslatorId(saved.getTranslator().getTranslatorID());
//                resp.setTranslatorName(saved.getTranslator().getTranslatorName());
//            }
//            if (saved.getLabtests() != null) {
//                resp.setLabtestId(saved.getLabtests().getId());
//                resp.setLabtestName(saved.getLabtests().getTestTitle());
//            }
//
//            responses.add(resp);
//        }
//
//        return responses;
//    }

    public Booking updateBooking(
            Long bookingId,
            Long userId,
            Long serviceId,
            String serviceType,
            LocalDateTime bookingStartDate,
            LocalDateTime bookingEndDate,
            String paymentMode,
            String bookingType,
            String remarks
    ) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        booking.setUser(user);

        booking.setBookingStartTime(bookingStartDate);
        booking.setBookingEndTime(bookingEndDate);
        booking.setBookingType(bookingType);
        booking.setAdditionalRemarks(remarks);
        booking.setBookingDate(LocalDateTime.now()); // or keep the original if not rebooking

        double price;

        switch (serviceType.toLowerCase()) {
            case "chef" -> {
                Chefs chef = chefRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Chef not found"));
                booking.setChef(chef);
                price = chef.getPrice();
            }
            case "physio" -> {
                Physio physio = physioRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Physio not found"));
                booking.setPhysio(physio);
                price = physio.getPrice();
            }
            case "translator" -> {
                Translators translator = translatorRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Translator not found"));
                booking.setTranslator(translator);
                price = translator.getPrice();
            }
            case "spa" -> {
                SpaServicese spa = spaServiceRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Spa service not found"));
                booking.setSpa(spa);
                price = spa.getPrice();
            }
            case "doctor" -> {
                Doctors doctor = doctorRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
                booking.setDoctors(doctor);
                price = doctor.getPrice();
            }
            case "labtests" -> {
                Labtests labtest = labtestsRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Lab test not found"));
                booking.setLabtests(labtest);
                price = labtest.getTestPrice();
            }
            default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
        }

        booking.setBookingAmount(price);

        if ("offline".equalsIgnoreCase(paymentMode)) {
            booking.setPaymentStatus("Unpaid");
        } else {
            booking.setPaymentStatus("Paid");
        }

        // Optionally reset status or apply logic
        booking.setBookingStatus("Updated");
        booking.setDiscountApplied("None");

        return bookingRepository.save(booking);
    }
    
     
    public BookingRequest addToCart(
            Long userId,
            String serviceType,
            LocalDateTime bookingStartDate,
            LocalDateTime bookingEndDate,
            String bookingType,
            String paymentMode,
            Double bookingAmount,
            String remarks,
            Long chefId,
            Long physioId,
            Long translatorId,
            Long spaId,
            Long doctorId,
            Long labtestId,
            String bookingStatus
    ) {
        users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookingStartTime(bookingStartDate);
        booking.setBookingEndTime(bookingEndDate);
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingStatus("Pending");
        booking.setAdditionalRemarks(remarks);
        booking.setPaymentMode(paymentMode);
        booking.setBookingType(serviceType);
        

        switch (serviceType.toLowerCase()) {
            case "chef" -> {
                if (chefId == null) throw new IllegalArgumentException("Chef ID is required");

                Chefs chef = chefRepository.findById(chefId)
                        .orElseThrow(() -> new EntityNotFoundException("Chef not found with ID: " + chefId));

                boolean alreadyBooked = !bookingRepository.findChefBookingsInTimeRange(chefId, bookingStartDate, bookingEndDate).isEmpty();
                if (alreadyBooked) throw new IllegalStateException("Chef is already booked for this time slot.");

                if (bookingAmount < chef.getPrice()) {
                    throw new IllegalArgumentException("Booking amount is less than base price for chef");
                }

                booking.setChef(chef);
            }

            case "physio" -> {
                if (physioId == null) throw new IllegalArgumentException("Physio ID is required");

                Physio physio = physioRepository.findById(physioId)
                        .orElseThrow(() -> new EntityNotFoundException("Physio not found with ID: " + physioId));

                boolean alreadyBooked = !bookingRepository.findPhysioBookingsInTimeRange(physioId, bookingStartDate, bookingEndDate).isEmpty();
                if (alreadyBooked) throw new IllegalStateException("Physio is already booked for this time slot.");

                if (bookingAmount < physio.getPrice()) {
                    throw new IllegalArgumentException("Booking amount is less than base price for physio");
                }

                booking.setPhysio(physio);
            }

            case "translator" -> {
                if (translatorId == null) throw new IllegalArgumentException("Translator ID is required");

                Translators translator = translatorRepository.findById(translatorId)
                        .orElseThrow(() -> new EntityNotFoundException("Translator not found with ID: " + translatorId));

                boolean alreadyBooked = !bookingRepository.findTranslatorBookingsInTimeRange(translatorId, bookingStartDate, bookingEndDate).isEmpty();
                if (alreadyBooked) throw new IllegalStateException("Translator is already booked for this time slot.");

                if (translator.getPrice() != null && bookingAmount < translator.getPrice()) {
                    throw new IllegalArgumentException("Booking amount is less than base price for translator");
                }

                booking.setTranslator(translator);
            }

            case "spa" -> {
                if (spaId == null) throw new IllegalArgumentException("Spa ID is required");

                SpaServicese spa = spaServiceRepository.findById(spaId)
                        .orElseThrow(() -> new EntityNotFoundException("Spa not found with ID: " + spaId));

                boolean alreadyBooked = !bookingRepository.findSpaBookingsInTimeRange(spaId, bookingStartDate, bookingEndDate).isEmpty();
                if (alreadyBooked) throw new IllegalStateException("Spa is already booked for this time slot.");

                if (bookingAmount < spa.getPrice()) {
                    throw new IllegalArgumentException("Booking amount is less than base price for spa");
                }

                booking.setSpa(spa);
            }

            case "doctor" -> {
                if (doctorId == null) throw new IllegalArgumentException("Doctor ID is required");

                Doctors doctor = doctorRepository.findById(doctorId)
                        .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

                boolean alreadyBooked = !bookingRepository.findDoctorBookingsInTimeRange(doctorId, bookingStartDate, bookingEndDate).isEmpty();
                if (alreadyBooked) throw new IllegalStateException("Doctor is already booked for this time slot.");

                if (bookingAmount < doctor.getPrice()) {
                    throw new IllegalArgumentException("Booking amount is less than base price for doctor");
                }

                booking.setDoctors(doctor);
            }

            case "labtests" -> {
                if (labtestId == null) throw new IllegalArgumentException("Lab Test ID is required");

                Labtests labtest = labtestsRepository.findById(labtestId)
                        .orElseThrow(() -> new EntityNotFoundException("Lab test not found with ID: " + labtestId));

                boolean alreadyBooked = !bookingRepository.findLabTestBookingsInTimeRange(labtestId, bookingStartDate, bookingEndDate).isEmpty();
                if (alreadyBooked) throw new IllegalStateException("Lab test is already booked for this time slot.");

                if (bookingAmount < labtest.getTestPrice()) {
                    throw new IllegalArgumentException("Booking amount is less than base price for lab test");
                }

                booking.setLabtests(labtest);
            }

            default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
        }

        booking.setBookingAmount(bookingAmount);
        booking.setPaymentStatus("Unpaid");
        booking.setDiscountApplied("None");

        Booking savedBooking = bookingRepository.save(booking);

        BookingRequest response = new BookingRequest();
        response.setBookingId(savedBooking.getBookingId());
        response.setBookingDate(savedBooking.getBookingDate());
        response.setSlotIdLong(savedBooking.getSlotIdLong());
        response.setBookingStatus(savedBooking.getBookingStatus());
        response.setBookingType(savedBooking.getBookingType());
        response.setBookingAmount(savedBooking.getBookingAmount());
        response.setPaymentMode(savedBooking.getPaymentMode());
        response.setPaymentStatus(savedBooking.getPaymentStatus());
        response.setDiscountApplied(savedBooking.getDiscountApplied());
        response.setBookingStartTime(savedBooking.getBookingStartTime());
        response.setBookingEndTime(savedBooking.getBookingEndTime());
        response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());

        if (savedBooking.getUser() != null) {
            response.setUserId(savedBooking.getUser().getId());
            response.setUserName(savedBooking.getUser().getName());
        }
        if (savedBooking.getChef() != null) {
            response.setChefId(savedBooking.getChef().getChefID());
            response.setChefName(savedBooking.getChef().getChefName());
        }
        if (savedBooking.getPhysio() != null) {
            response.setPhysioId(savedBooking.getPhysio().getPhysioId());
            response.setPhysioName(savedBooking.getPhysio().getPhysioName());
        }
        if (savedBooking.getTranslator() != null) {
            response.setTranslatorId(savedBooking.getTranslator().getTranslatorID());
            response.setTranslatorName(savedBooking.getTranslator().getTranslatorName());
        }
        if (savedBooking.getSpa() != null) {
            response.setSpaId(savedBooking.getSpa().getServiceId());
            response.setSpaName(savedBooking.getSpa().getServiceName());
        }
        if (savedBooking.getDoctors() != null) {
            response.setDoctorId(savedBooking.getDoctors().getId());
            response.setDoctorName(savedBooking.getDoctors().getName());
        }
        if (savedBooking.getLabtests() != null) {
            response.setLabtestId(savedBooking.getLabtests().getId());
            response.setLabtestName(savedBooking.getLabtests().getTestTitle());
        }

        return response;
    }


    public List<ChefDTO> getAvailableChefs(LocalDateTime start, LocalDateTime end) {
        return chefRepository.findAll().stream()
                .filter(chef -> bookingRepository
                    .findChefBookingsInTimeRange(chef.getChefID(), start, end)
                    .isEmpty()) // ✅ Available
                .map(chef -> {
                    ChefDTO dto = new ChefDTO();
                    dto.setChefID(chef.getChefID());
                    dto.setChefName(chef.getChefName());
                    dto.setPrice(chef.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());
    }

 
//    public List<BookingRequest> getBookingsByUserId(Long userId) {
//        List<Booking> bookings = bookingRepository.findByUserId(userId);
//
//        return bookings.stream().map(booking -> {
//            BookingRequest dto = new BookingRequest();
//
//            // Booking details
//            dto.setBookingId(booking.getBookingId());
//            dto.setBookingStatus(booking.getBookingStatus());
//            dto.setBookingType(booking.getBookingType());
//            dto.setBookingAmount(booking.getBookingAmount());
//            dto.setPaymentMode(booking.getPaymentMode());
//            dto.setPaymentStatus(booking.getPaymentStatus());
//            dto.setDiscountApplied(booking.getDiscountApplied());
//            dto.setBookingStartTime(booking.getBookingStartTime());
//            dto.setBookingEndTime(booking.getBookingEndTime());
//            dto.setBookingDate(booking.getBookingDate());
//            dto.setRemarks(booking.getAdditionalRemarks());
//            dto.setServiceType(booking.getServiceType());
//
//            // User info
//            if (booking.getUser() != null) {
//                dto.setUserId(booking.getUser().getId());
//                dto.setUserName(booking.getUser().getName());
//                dto.setUserEmail(booking.getUser().getEmail());
//            }
//
//            // Dynamically map booked service ID & name only
//            switch (booking.getServiceType().toLowerCase()) {
//                case "chef" -> {
//                    if (booking.getChef() != null) {
//                        dto.setServiceId(booking.getChef().getChefID());
//                        dto.setServiceName(booking.getChef().getChefName());
//                    }
//                }
//                case "physio" -> {
//                    if (booking.getPhysio() != null) {
//                        dto.setServiceId(booking.getPhysio().getPhysioId());
//                        dto.setServiceName(booking.getPhysio().getPhysioName());
//                    }
//                }
//                case "doctor" -> {
//                    if (booking.getDoctors() != null) {
//                        dto.setServiceId(booking.getDoctors().getId());
//                        dto.setServiceName(booking.getDoctors().getName());
//                    }
//                }
//                case "labtests" -> {
//                    if (booking.getLabtests() != null) {
//                        dto.setServiceId(booking.getLabtests().getId());
//                        dto.setServiceName(booking.getLabtests().getTestTitle());
//                    }
//                }
//                case "spa" -> {
//                    if (booking.getSpa() != null) {
//                        dto.setServiceId(booking.getSpa().getServiceId());
//                        dto.setServiceName(booking.getSpa().getServiceName());
//                    }
//                }
//                case "translator" -> {
//                    if (booking.getTranslator() != null) {
//                        dto.setServiceId(booking.getTranslator().getTranslatorID());
//                        dto.setServiceName(booking.getTranslator().getTranslatorName());
//                    }
//                }
//            }
//
//            return dto;
//        }).collect(Collectors.toList());
//    }

    public List<BookingRequest> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream().map(booking -> {
        	BookingRequest dto = new BookingRequest();
            dto.setBookingId(booking.getBookingId());
            dto.setBookingStatus(booking.getBookingStatus());
            dto.setBookingType(booking.getBookingType());
            dto.setBookingAmount(booking.getBookingAmount());
            dto.setBookingStartTime(booking.getBookingStartTime());
            dto.setBookingEndTime(booking.getBookingEndTime());
            dto.setBookingStatus(booking.getBookingStatus());

            String serviceType = booking.getServiceType();
            dto.setServiceTypes(serviceType);

            // Set the name based on the service
            switch (serviceType) {
                case "physio" -> dto.setServiceName(booking.getPhysio().getPhysioName());
                case "translator" -> dto.setServiceName(booking.getTranslator().getTranslatorName());
                case "spa" -> dto.setServiceName(booking.getSpa().getServiceName());
                case "doctor" -> dto.setServiceName(booking.getDoctors().getName());
                case "labtests" -> dto.setServiceName(booking.getLabtests().getTestTitle());
                case "chef" -> dto.setServiceName(booking.getChef().getChefName());
                default -> dto.setServiceName("Unknown");
            }

            return dto;
        }).toList();
    }

    
    
    
    //update the booking Status
  

    @Transactional
    public List<BookingRequest> updateBookingStatusesToSuccess(List<Long> bookingIds) {

        List<BookingRequest> updatedBookings = new ArrayList<>();

        for (Long bookingId : bookingIds) {
            Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

            if (optionalBooking.isEmpty()) {
                continue; // skip if booking not found
            }

            Booking booking = optionalBooking.get();

            if (!"Pending".equalsIgnoreCase(booking.getBookingStatus())) {
                continue; // skip if status is not pending
            }

            booking.setBookingStatus("Success");
            booking.setPaymentStatus("Paid");
            bookingRepository.save(booking);

            // ✉️ Send email directly from here
            users user = booking.getUser();
            if (user != null && user.getEmail() != null) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(user.getEmail());
                    message.setSubject("Booking Confirmation - #" + booking.getBookingId());
                    message.setText("Dear " + user.getName() + ",\n\nYour " + booking.getBookingType() +
                            " booking (ID: " + booking.getBookingId() + ") has been confirmed.\nThank you for choosing us!\n\nRegards,\nHospital Tourism Team");
                    message.setFrom("anil.n@zynlogic.com"); // match with spring.mail.username

                    javaMailSender.send(message);
                } catch (Exception e) {
                    System.err.println("Failed to send email for bookingId: " + bookingId + " due to: " + e.getMessage());
                }
            }

            // Add to response list
            BookingRequest response = new BookingRequest();
            response.setBookingId(booking.getBookingId());
            response.setBookingStatus(booking.getBookingStatus());
            response.setBookingType(booking.getBookingType());
            response.setBookingAmount(booking.getBookingAmount());
            response.setPaymentMode(booking.getPaymentMode());
            response.setPaymentStatus(booking.getPaymentStatus());
            response.setBookingDate(booking.getBookingDate());

            updatedBookings.add(response);
        }

        return updatedBookings;
    }

    
    
    public List<BookingRequest> getAllSuccessfulBookings() {
        List<Booking> successfulBookings = bookingRepository.findByPaymentStatus("Paid");

        return successfulBookings.stream().map(booking -> {
            BookingRequest dto = new BookingRequest();
            
            dto.setBookingId(booking.getBookingId());
            dto.setBookingDate(booking.getBookingDate());
            dto.setBookingStartTime(booking.getBookingStartTime());
            dto.setBookingEndTime(booking.getBookingEndTime());
            dto.setBookingType(booking.getBookingType());
            dto.setBookingStatus(booking.getBookingStatus());
            dto.setBookingAmount(booking.getBookingAmount());
            dto.setPaymentStatus(booking.getPaymentStatus());  // This will be "Success"
            dto.setPaymentMode(booking.getPaymentMode());
            dto.setAdditionalRemarks(booking.getAdditionalRemarks());

            if (booking.getUser() != null) {
                dto.setUserId(booking.getUser().getId());
                dto.setUserName(booking.getUser().getName());
            }

            if (booking.getChef() != null) {
                dto.setChefId(booking.getChef().getChefID());
                dto.setChefName(booking.getChef().getChefName());
            }

            if (booking.getPhysio() != null) {
                dto.setPhysioId(booking.getPhysio().getPhysioId());
                dto.setPhysioName(booking.getPhysio().getPhysioName());
            }

            if (booking.getTranslator() != null) {
                dto.setTranslatorId(booking.getTranslator().getTranslatorID());
                dto.setTranslatorName(booking.getTranslator().getTranslatorName());
            }

            if (booking.getSpa() != null) {
                dto.setSpaId(booking.getSpa().getServiceId());
                dto.setSpaName(booking.getSpa().getServiceName());
            }

            if (booking.getDoctors() != null) {
                dto.setDoctorId(booking.getDoctors().getId());
                dto.setDoctorName(booking.getDoctors().getName());
            }

            if (booking.getLabtests() != null) {
                dto.setLabtestId(booking.getLabtests().getId());
                dto.setLabtestName(booking.getLabtests().getTestTitle());
            }

            return dto;
        }).toList();
    }
    
    public List<BookingRequest> getSuccessfulBookingsByUserId(Long userId) {
        List<Booking> successfulBookings = bookingRepository.findByUserIdAndPaymentStatus(userId, "Paid");

        return successfulBookings.stream().map(booking -> {
            BookingRequest dto = new BookingRequest();

            dto.setBookingId(booking.getBookingId());
            dto.setBookingDate(booking.getBookingDate());
            dto.setBookingStartTime(booking.getBookingStartTime());
            dto.setBookingEndTime(booking.getBookingEndTime());
            dto.setBookingType(booking.getBookingType());
            dto.setBookingStatus(booking.getBookingStatus());
            dto.setBookingAmount(booking.getBookingAmount());
            dto.setPaymentStatus(booking.getPaymentStatus());
            dto.setPaymentMode(booking.getPaymentMode());
            dto.setAdditionalRemarks(booking.getAdditionalRemarks());

            if (booking.getUser() != null) {
                dto.setUserId(booking.getUser().getId());
                dto.setUserName(booking.getUser().getName());
            }

            if (booking.getChef() != null) {
                dto.setChefId(booking.getChef().getChefID());
                dto.setChefName(booking.getChef().getChefName());
            }

            if (booking.getPhysio() != null) {
                dto.setPhysioId(booking.getPhysio().getPhysioId());
                dto.setPhysioName(booking.getPhysio().getPhysioName());
            }

            if (booking.getTranslator() != null) {
                dto.setTranslatorId(booking.getTranslator().getTranslatorID());
                dto.setTranslatorName(booking.getTranslator().getTranslatorName());
            }

            if (booking.getSpa() != null) {
                dto.setSpaId(booking.getSpa().getServiceId());
                dto.setSpaName(booking.getSpa().getServiceName());
            }

            if (booking.getDoctors() != null) {
                dto.setDoctorId(booking.getDoctors().getId());
                dto.setDoctorName(booking.getDoctors().getName());
            }

            if (booking.getLabtests() != null) {
                dto.setLabtestId(booking.getLabtests().getId());
                dto.setLabtestName(booking.getLabtests().getTestTitle());
            }

            return dto;
        }).toList();
    }
    
    public void deleteByBookingId(Long bookingId) {
    	  bookingRepository.deleteById(bookingId);
		
    }
    
    public void deleteByMultipleBookingIds(List<Long> bookingIds) {
        for (Long bookingId : bookingIds) {
            bookingRepository.deleteById(bookingId);
        }
    }


}
