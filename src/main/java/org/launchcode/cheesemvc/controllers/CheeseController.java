package org.launchcode.cheesemvc.controllers;

import org.launchcode.cheesemvc.models.data.CategoryDao;
import org.launchcode.cheesemvc.models.data.CheeseDao;
import org.launchcode.cheesemvc.models.Category;
import org.launchcode.cheesemvc.models.Cheese;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value="cheese")
public class CheeseController {

    @Autowired
    CheeseDao cheeseDao;

    @Autowired
    CategoryDao categoryDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");
        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute @Valid Cheese newCheese, Errors errors, @RequestParam int categoryId, Model model) {

        if(errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }

        Category cat = categoryDao.findById(categoryId).orElse(null);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }
    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model){

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] ids) {

        for (int id : ids) {
              cheeseDao.deleteById(id);
        }

        return "redirect:";
    }

    @RequestMapping(value = "category", method = RequestMethod.GET)
    public String category(Model model, @RequestParam int id) {

        Category cat = categoryDao.findById(id).orElse(null);
        List<Cheese> cheeses = cat.getCheeses();
        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title", "Cheeses in Category: " + cat.getName());
        return "cheese/index";
    }
}
