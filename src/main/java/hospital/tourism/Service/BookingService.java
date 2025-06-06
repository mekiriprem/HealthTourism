package hospital.tourism.Service;

import hospital.tourism.Entity.*;

import hospital.tourism.repo.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
