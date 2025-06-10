package hospital.tourism.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hospital.tourism.Dto.BookingRequest;
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
    
    
    public BookingRequest bookService(
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
        booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
        booking.setDiscountApplied("None");

        Booking savedBooking = bookingRepository.save(booking);

        // Build response DTO directly here
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
        response.setSlotInfo(savedBooking.getSlotInfo());
        response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());

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
    public BookingRequest bookServices(
            Long userId,
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
                Chefs chef = chefRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No chefs available"));
                booking.setChef(chef);
                price = chef.getPrice();
            }
            case "physio" -> {
                Physio physio = physioRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No physios available"));
                booking.setPhysio(physio);
                price = physio.getPrice();
            }
            case "translator" -> {
                Translators translator = translatorRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No translators available"));
                booking.setTranslator(translator);
                 price = translator.getPrice() != null ? translator.getPrice() : 0.0;

            }
            case "spa" -> {
                SpaServicese spa = spaServiceRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No spa services available"));
                booking.setSpa(spa);
                price = spa.getPrice();
            }
            case "doctor" -> {
                Doctors doctor = doctorRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No doctors available"));
                booking.setDoctors(doctor);
                price = doctor.getPrice();
            }
            case "labtests" -> {
                Labtests labtest = labtestsRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No lab tests available"));
                booking.setLabtests(labtest);
                price = labtest.getTestPrice();
            }
            default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
        }

        booking.setBookingAmount(price);
        booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
        booking.setDiscountApplied("None");

        Booking savedBooking = bookingRepository.save(booking);

        // Populate and return DTO
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
        response.setSlotInfo(savedBooking.getSlotInfo());
        response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());

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
    
    public List<BookingRequest> bookMultipleServices(
            Long userId,
            List<String> serviceTypes,
            List<String> slotInfo,
            String paymentMode,
            String bookingType,
            String remarks
    ) {
        users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<BookingRequest> bookingResponses = new ArrayList<>();

        for (String serviceType : serviceTypes) {
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
                    Chefs chef = chefRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("No chefs available"));
                    booking.setChef(chef);
                    price = chef.getPrice();
                }
                case "physio" -> {
                    Physio physio = physioRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("No physios available"));
                    booking.setPhysio(physio);
                    price = physio.getPrice();
                }
                case "translator" -> {
                    Translators translator = translatorRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("No translators available"));
                    booking.setTranslator(translator);
                    price = translator.getPrice() != null ? translator.getPrice() : 0.0;
                }
                case "spa" -> {
                    SpaServicese spa = spaServiceRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("No spa services available"));
                    booking.setSpa(spa);
                    price = spa.getPrice();
                }
                case "doctor" -> {
                    Doctors doctor = doctorRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("No doctors available"));
                    booking.setDoctors(doctor);
                    price = doctor.getPrice();
                }
                case "labtests" -> {
                    Labtests labtest = labtestsRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("No lab tests available"));
                    booking.setLabtests(labtest);
                    price = labtest.getTestPrice();
                }
                default -> throw new IllegalArgumentException("Invalid service type: " + serviceType);
            }

            booking.setBookingAmount(price);
            booking.setPaymentStatus("offline".equalsIgnoreCase(paymentMode) ? "Unpaid" : "Paid");
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
            response.setSlotInfo(savedBooking.getSlotInfo());
            response.setAdditionalRemarks(savedBooking.getAdditionalRemarks());

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
            if (savedBooking.getUser() != null) {
                response.setUserId(savedBooking.getUser().getId());
                response.setUserName(savedBooking.getUser().getName());
            }
            if (savedBooking.getChef() != null) {
                response.setChefId(savedBooking.getChef().getChefID());
                response.setChefName(savedBooking.getChef().getChefName());
            }

            bookingResponses.add(response);
        }

        return bookingResponses;
    }


    public Booking updateBooking(
            Long bookingId,
            Long userId,
            Long serviceId,
            String serviceType,
            List<String> slotInfo,
            String paymentMode,
            String bookingType,
            String remarks
    ) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        booking.setUser(user);

        booking.setSlotInfo(slotInfo);
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

}
