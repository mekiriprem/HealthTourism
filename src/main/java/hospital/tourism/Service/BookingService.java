package hospital.tourism.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import hospital.tourism.Dto.BookingRequest;
import hospital.tourism.Dto.CartCheckoutRequest;
import hospital.tourism.Dto.ChefDTO;
import hospital.tourism.Entity.Booking;
import hospital.tourism.Entity.Chefs;
import hospital.tourism.Entity.CouponEntity;
import hospital.tourism.Entity.Doctors;
import hospital.tourism.Entity.Labtests;
import hospital.tourism.Entity.Physio;
import hospital.tourism.Entity.SpaServicese;
import hospital.tourism.Entity.Translators;
import hospital.tourism.Entity.users;
import hospital.tourism.repo.BookingRepo;
import hospital.tourism.repo.CouponRepository;
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

    @Autowired
	private CouponRepository couponRepository;

    
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

        // Validate payment mode
        if (paymentMode == null || (!paymentMode.equalsIgnoreCase("online") && !paymentMode.equalsIgnoreCase("offline"))) {
            throw new IllegalArgumentException("Payment mode is required and must be either 'online' or 'offline'.");
        }

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


    public List<BookingRequest> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream()
            .filter(booking -> !"Paid".equalsIgnoreCase(booking.getPaymentStatus())) // ✅ Filter based on paymentStatus
            .map(booking -> {
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
                continue; // Skip if booking not found
            }

            Booking booking = optionalBooking.get();

            if (!"Pending".equalsIgnoreCase(booking.getBookingStatus())) {
                continue; // Skip if status is not pending
            }

            String paymentMode = booking.getPaymentMode();

            if ("offline".equalsIgnoreCase(paymentMode)) {
                booking.setBookingStatus("Booked");
                booking.setPaymentStatus("Unpaid");
            } else if ("online".equalsIgnoreCase(paymentMode)) {
                booking.setBookingStatus("Success");
                booking.setPaymentStatus("Paid");
            } else {
                // Skip if payment mode is invalid or missing
                continue;
            }

            bookingRepository.save(booking);

            // Send email only if payment was online (i.e., success)
            if ("online".equalsIgnoreCase(paymentMode)) {
                users user = booking.getUser();
                if (user != null && user.getEmail() != null) {
                    try {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(user.getEmail());
                        message.setSubject("Booking Confirmation - #" + booking.getBookingId());
                        message.setText("Dear " + user.getName() + ",\n\nYour " + booking.getBookingType() +
                                " booking (ID: " + booking.getBookingId() + ") has been confirmed.\nThank you for choosing us!\n\nRegards,\nHospital Tourism Team");
                        message.setFrom("anil.n@zynlogic.com");

                        javaMailSender.send(message);
                    } catch (Exception e) {
                        System.err.println("Failed to send email for bookingId: " + bookingId + " due to: " + e.getMessage());
                    }
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

    @Transactional
    public List<BookingRequest> updatePaymentStatusToPaid(List<Long> bookingIds) {
        List<BookingRequest> updatedBookings = new ArrayList<>();

        for (Long bookingId : bookingIds) {
            Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

            if (optionalBooking.isEmpty()) {
                continue; // Skip if booking not found
            }

            Booking booking = optionalBooking.get();

            // Handle different scenarios:
            // 1. Pending + Offline -> Booked + Unpaid (first time confirmation)
            // 2. Booked + Unpaid + Offline -> Success + Paid (payment confirmation)
            // 3. Pending + Online -> Success + Paid (direct online payment)

            String currentStatus = booking.getBookingStatus();
            String paymentMode = booking.getPaymentMode();
            String paymentStatus = booking.getPaymentStatus();

            if ("Pending".equalsIgnoreCase(currentStatus)) {
                // First time confirmation
                if ("offline".equalsIgnoreCase(paymentMode)) {
                    booking.setBookingStatus("Booked");
                    booking.setPaymentStatus("Unpaid");
                } else if ("online".equalsIgnoreCase(paymentMode)) {
                    booking.setBookingStatus("Success");
                    booking.setPaymentStatus("Paid");
                }
            } else if ("Booked".equalsIgnoreCase(currentStatus) && "Unpaid".equalsIgnoreCase(paymentStatus)) {
                // Payment confirmation for offline bookings
                booking.setBookingStatus("Success");
                booking.setPaymentStatus("Paid");
            } else {
                // Skip if booking is already processed or in invalid state
                continue;
            }

            bookingRepository.save(booking);

            // Send email for successful payments
            if ("Paid".equalsIgnoreCase(booking.getPaymentStatus())) {
                users user = booking.getUser();
                if (user != null && user.getEmail() != null) {
                    try {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(user.getEmail());
                        message.setSubject("Booking Confirmation - #" + booking.getBookingId());
                        message.setText("Dear " + user.getName() + ",\n\nYour " + booking.getBookingType() +
                                " booking (ID: " + booking.getBookingId() + ") has been confirmed and payment has been processed.\nThank you for choosing us!\n\nRegards,\nHospital Tourism Team");
                        message.setFrom("anil.n@zynlogic.com");

                        javaMailSender.send(message);
                    } catch (Exception e) {
                        System.err.println("Failed to send email for bookingId: " + bookingId + " due to: " + e.getMessage());
                    }
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
            response.setBookingStartTime(booking.getBookingStartTime());
            response.setBookingEndTime(booking.getBookingEndTime());

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


    public Booking checkoutBooking(CartCheckoutRequest req) {
        Booking booking = new Booking();

        users user = userRepository.findById(req.getUserId()).orElseThrow();
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingStartTime(req.getStartTime());
        booking.setBookingEndTime(req.getEndTime());
        booking.setPaymentMode(req.getPaymentMode());
        booking.setBookingType(req.getBookingType());
        booking.setAdditionalRemarks(req.getAdditionalRemarks());
        booking.setBookingStatus("CONFIRMED");
        booking.setPaymentStatus("PENDING");

        double total = 0.0;

        for (int i = 0; i < req.getServiceIds().size(); i++) {
            Long id = req.getServiceIds().get(i);
            String type = req.getServiceTypes().get(i).toLowerCase();

            switch (type) {
                case "spa" -> {
                    SpaServicese spa = spaServiceRepository.findById(id).orElseThrow();
                    booking.setSpa(spa);
                    total += spa.getPrice();
                }
                case "doctor" -> {
                    Doctors doc = doctorRepository.findById(id).orElseThrow();
                    booking.setDoctors(doc);
                    total += doc.getPrice();
                }
                case "chef" -> {
                    Chefs chef = chefRepository.findById(id).orElseThrow();
                    booking.setChef(chef);
                    total += chef.getPrice();
                }
                case "translator" -> {
                    Translators t = translatorRepository.findById(id).orElseThrow();
                    booking.setTranslator(t);
                    total += t.getPrice();
                }
                case "labtests" -> {
                    Labtests l = labtestsRepository.findById(id).orElseThrow();
                    booking.setLabtests(l);
                    total += l.getTestPrice();
                }
                case "physio" -> {
                    Physio p = physioRepository.findById(id).orElseThrow();
                    booking.setPhysio(p);
                    total += p.getPrice();
                }
            }
        }

        // Store original amount (optional)
        booking.setCtotalAmount(total); // You had this field

        // Apply coupon if valid
        if (req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
            CouponEntity coupon = couponRepository.findByCode(req.getCouponCode());
            if (coupon != null &&
                coupon.isActive() &&
                (coupon.getValidFrom() == null || !coupon.getValidFrom().isAfter(LocalDateTime.now())) &&
                (coupon.getValidTill() == null || !coupon.getValidTill().isBefore(LocalDateTime.now()))
            ) {
                double discount = 0.0;
                if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                    discount = total * coupon.getDiscountAmount() / 100.0;
                } else if ("FIXED".equalsIgnoreCase(coupon.getDiscountType())) {
                    discount = coupon.getDiscountAmount();
                }

                total -= discount;

                booking.setCoupon(coupon);
                booking.setDiscountApplied(coupon.getCode());
            }
        }

        booking.setBookingAmount(total);
        return bookingRepository.save(booking);
    }
}
