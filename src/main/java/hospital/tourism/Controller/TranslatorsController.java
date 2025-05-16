// package: hospital.tourism.Controller

package hospital.tourism.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import hospital.tourism.Entity.Translators;
import hospital.tourism.Service.TranslatorsService;

@RestController
@RequestMapping("/api/translators")
public class TranslatorsController {

    @Autowired
    private TranslatorsService translatorsService;

    // ✅ Insert Translator
    @PostMapping("/add")
    public Translators addTranslator(@RequestBody Map<String, Object> translatorMap) {
        Translators translator = new Translators();
        translator.setTranslatorName((String) translatorMap.get("translatorName"));
        translator.setTranslatorDescription((String) translatorMap.get("translatorDescription"));
        translator.setTranslatorImage((String) translatorMap.get("translatorImage"));
        translator.setTranslatorRating((String) translatorMap.get("translatorRating"));
        translator.setTranslatorLanguages((String) translatorMap.get("translatorLanguages"));
        Integer locationId = Integer.parseInt(translatorMap.get("locationId").toString());
        return translatorsService.saveTranslator(translator, locationId);
    }

    // ✅ Get Translators by Location ID
    @GetMapping("/by-location/{locationId}")
    public List<Translators> getTranslatorsByLocation(@PathVariable Integer locationId) {
        return translatorsService.getTranslatorsByLocationId(locationId);
    }

    // ✅ Get All Translators
    @GetMapping
    public List<Translators> getAllTranslators() {
        return translatorsService.getAllTranslators();
    }
}
