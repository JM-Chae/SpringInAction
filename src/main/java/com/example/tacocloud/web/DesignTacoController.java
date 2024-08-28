package com.example.tacocloud.web;

import com.example.tacocloud.Ingredient;
import com.example.tacocloud.Ingredient.Type;
import com.example.tacocloud.Order;
import com.example.tacocloud.Taco;
import com.example.tacocloud.data.IngredientRepository;
import com.example.tacocloud.data.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAttributeEditor;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController
  {
    private final IngredientRepository ingredientRepo;
    private final TacoRepository tacoRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo)
      {
        this.ingredientRepo = ingredientRepo;
        this.tacoRepo = tacoRepo;
      }

    @GetMapping
    public String ShowDesignForm(Model model)
      {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types)
          {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
          }

        model.addAttribute("taco", new Taco());

        return "design";
      }

    private List<Ingredient> filterByType
        (List<Ingredient> ingredients, Type type)
      {
        return ingredients
            .stream()
            .filter(x -> x.getType().equals(type))
            .collect(Collectors.toList());
      }

    @ModelAttribute(name="order")
    public Order order()
      {
        return new Order();
      }

    @ModelAttribute(name="taco")
    public Taco taco()
      {
        return new Taco();
      }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order)
      {
        if (errors.hasErrors())
          {
            return "design";
          }

        Taco saved = tacoRepo.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
      }
  }

