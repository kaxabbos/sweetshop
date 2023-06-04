package com.mus.controller;

import com.mus.controller.main.Attributes;
import com.mus.model.*;
import com.mus.model.enums.StatusOrdering;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductsCont extends Attributes {

    @GetMapping("/add")
    public String productAdd(Model model) {
        AddAttributesProductAdd(model);
        return "productAdd";
    }

    @GetMapping("/my")
    public String orderingsMy(Model model) {
        AddAttributesProductsMy(model);
        return "productsMy";
    }

    @GetMapping("/notconf/{id}")
    public String orderingNotConf(@PathVariable Long id) {
        Ordering ordering = orderingRepo.getReferenceById(id);
        ordering.setStatusOrdering(StatusOrdering.NOT_CONF);
        orderingRepo.save(ordering);
        return "redirect:/products/my";
    }

    @GetMapping("/conf/{id}")
    public String orderingConf(@PathVariable Long id) {
        Ordering ordering = orderingRepo.getReferenceById(id);
        ordering.setStatusOrdering(StatusOrdering.CONF);
        orderingRepo.save(ordering);
        return "redirect:/products/my";
    }

    @GetMapping("/done/{id}")
    public String orderingDone(@PathVariable Long id) {
        Ordering ordering = orderingRepo.getReferenceById(id);
        ordering.setStatusOrdering(StatusOrdering.DONE);
        ordering.getProduct().setCount(ordering.getProduct().getCount() + 1);
        orderingRepo.save(ordering);
        return "redirect:/products/my";
    }

    @GetMapping("/{id}")
    public String product(Model model, @PathVariable Long id) {
        AddAttributesProduct(model, id);
        return "product";
    }

    @GetMapping("/edit/{id}")
    public String productEdit(Model model, @PathVariable Long id) {
        AddAttributesProductEdit(model, id);
        return "productEdit";
    }

    @GetMapping("/delete/{id}")
    public String productDelete(@PathVariable Long id) {
        List<Ordering> orderings = orderingRepo.findAllByProduct_Id(id);
        for (Ordering i : orderings) {
            orderingRepo.deleteById(i.getId());
        }
        productsRepo.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/ordering/{productId}")
    public String productOrdering(@RequestParam Long placeId, @RequestParam String date, @RequestParam String time, @PathVariable Long productId) {
        Places place = placesRepo.getReferenceById(placeId);
        Users client = getUser();
        Products product = productsRepo.getReferenceById(productId);

        Ordering ordering = new Ordering(place, client, product, date, time);

        orderingRepo.save(ordering);
        return "redirect:/products/{productId}";
    }

    @PostMapping("/add")
    public String productAddNew(Model model, @RequestParam String name, @RequestParam Long categoryId, @RequestParam MultipartFile photo, @RequestParam int price, @RequestParam String description) {
        String res = "";
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "products/" + uuidFile + "_" + photo.getOriginalFilename();
                    photo.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                model.addAttribute("message", "Некорректный данные!");
                AddAttributesProductAdd(model);
                return "productAdd";
            }
        }

        Products product = new Products(name, res, price, description);

        Category category = categoryRepo.getReferenceById(categoryId);

        category.addProduct(product);

        categoryRepo.save(category);

        return "redirect:/products/add";
    }

    @PostMapping("/edit/{id}")
    public String productEditOld(Model model, @RequestParam String name, @RequestParam Long categoryId, @RequestParam MultipartFile photo, @RequestParam int price, @RequestParam String description, @PathVariable Long id) {
        Products product = productsRepo.getReferenceById(id);
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String res = "";
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "products/" + uuidFile + "_" + photo.getOriginalFilename();
                    photo.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (Exception e) {
                model.addAttribute("message", "Некорректный данные!");
                AddAttributesProductEdit(model, id);
                return "productEdit";
            }
            product.setPhoto(res);
        }

        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);

        Category categoryNew = categoryRepo.getReferenceById(categoryId);
        if ((!categoryNew.getId().equals(product.getCategory().getId()))) {
            Category categoryOld = product.getCategory();
            categoryOld.removeProduct(product);
            categoryNew.addProduct(product);
        }
        productsRepo.save(product);

        return "redirect:/";
    }
}
