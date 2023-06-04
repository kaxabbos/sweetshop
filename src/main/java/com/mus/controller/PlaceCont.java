package com.mus.controller;

import com.mus.controller.main.Attributes;
import com.mus.model.Places;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/place")
public class PlaceCont extends Attributes {
    @GetMapping
    public String Place(Model model) {
        AddAttributesPlace(model);
        return "place";
    }

    @PostMapping("/edit/photo")
    public String PlaceEditPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String res = "";
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "places/" + uuidFile + "_" + photo.getOriginalFilename();
                    photo.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                model.addAttribute("message", "Некорректный данные!");
                AddAttributesPlace(model);
                return "place";
            }
            Places place = getUser().getPlace();
            place.setPhoto(res);
            placesRepo.save(place);
        }
        return "redirect:/place";
    }

    @PostMapping("/edit")
    public String PlaceEdit(@RequestParam String fio, @RequestParam Long category, @RequestParam String tel, @RequestParam String address) {
        Places place = getUser().getPlace();
        place.setFio(fio);
        place.setCategory(categoryRepo.getReferenceById(category).getName());
        place.setTel(tel);
        place.setAddress(address);
        placesRepo.save(place);
        return "redirect:/place";
    }
}
