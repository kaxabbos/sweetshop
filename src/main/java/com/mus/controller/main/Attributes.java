package com.mus.controller.main;

import com.mus.model.Category;
import com.mus.model.Places;
import com.mus.model.Products;
import com.mus.model.Users;
import com.mus.model.enums.Role;
import org.springframework.ui.Model;

import java.util.List;

public class Attributes extends Main {

    protected void AddAttributes(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());
    }

    protected void AddAttributesUsers(Model model) {
        AddAttributes(model);
        model.addAttribute("users", usersRepo.findAll());
        model.addAttribute("roles", Role.values());
    }

    protected void AddAttributesProductAdd(Model model) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesProductEdit(Model model, Long id) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("product", productsRepo.getReferenceById(id));
    }

    protected void AddAttributesProductsMy(Model model) {
        AddAttributes(model);
        Users user = getUser();
        if (user.getRole() == Role.MANAGER) {
            model.addAttribute("orderings", user.getPlace().getOrderings());
        } else {
            model.addAttribute("orderings", user.getOrderings());
        }
    }

    protected void AddAttributesProduct(Model model, Long id) {
        AddAttributes(model);
        Products product = productsRepo.getReferenceById(id);
        List<Places> places = placesRepo.findAllByCategory(product.getCategory().getName());
        model.addAttribute("product", product);
        model.addAttribute("places", places);
    }

    protected void AddAttributesIndex(Model model) {
        AddAttributes(model);
        model.addAttribute("products", productsRepo.findAll());
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesCategory(Model model) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesPlace(Model model) {
        AddAttributes(model);
        model.addAttribute("categories", categoryRepo.findAll());
    }

    protected void AddAttributesPlaces(Model model) {
        AddAttributes(model);
        model.addAttribute("places", placesRepo.findAll());
    }

    protected void AddAttributesSearch(Model model, String name, Long categoryId) {
        AddAttributes(model);
        Category category = categoryRepo.getReferenceById(categoryId);
        model.addAttribute("products", productsRepo.findAllByNameContainingAndCategory_Name(name, category.getName()));
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("selectedCId", categoryId);
        model.addAttribute("name", name);
    }

    protected void AddAttributesStats(Model model) {
        AddAttributes(model);
        model.addAttribute("products", productsRepo.findAll());
    }
}
