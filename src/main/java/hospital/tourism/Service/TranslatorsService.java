// package: hospital.tourism.Service

package hospital.tourism.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.tourism.Entity.LocationEntity;
import hospital.tourism.Entity.Translators;
import hospital.tourism.repo.LocationRepo;
import hospital.tourism.repo.TranslatorsRepo;

@Service
public class TranslatorsService {

    @Autowired
    private TranslatorsRepo translatorsRepo;

    @Autowired
    private LocationRepo locationRepo;

    // Save translator
    public Translators saveTranslator(Translators translator, Integer locationId) {
        LocationEntity location = locationRepo.findById(locationId)
            .orElseThrow(() -> new RuntimeException("❌ Location not found with id: " + locationId));

        translator.setLocation(location);
        return translatorsRepo.save(translator);
    }


    // Get all translators by locationId
    public List<Translators> getTranslatorsByLocationId(Integer locationId) {
        return translatorsRepo.findByLocation_LocationId(locationId);
    }

    // (Optional) Get all translators
    public List<Translators> getAllTranslators() {
        return translatorsRepo.findAll();
    }
}

