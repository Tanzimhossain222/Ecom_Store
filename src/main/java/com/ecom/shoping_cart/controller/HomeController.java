package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.service.CategoryService;
import com.ecom.shoping_cart.service.ProductService;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(Model model, Principal principal){

        List<Category> categories = categoryService.getAllActiveCategory().stream()
                .sorted((c1, c2) -> c2.getId().compareTo(c1.getId()))
                .limit(6).toList();

        List<Product> allActiveProducts = productService.getAllActiveProduct("").stream()
                .sorted((p1, p2) -> p2.getId().compareTo(p1.getId())).limit(4).toList();


        model.addAttribute("categories", categories);
        model.addAttribute("products", allActiveProducts);



        return "index";
    }


    @GetMapping("/products")
    public String product(Model model, @RequestParam(value = "category", required = false, defaultValue = "") String category,
                          @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo, @RequestParam(value = "pageSize", required = false, defaultValue = "6") Integer pageSize){

        List<Category> categories = categoryService.getAllActiveCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("paramValue", category);


        Page<Product> page = productService.getAllActiveProductPaginated(pageNo, pageSize, category);
        List<Product> products = page.getContent();
        model.addAttribute("products", products);
        model.addAttribute(("productsSize"), products.size());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute(("pageNo"), page.getNumber());
        model.addAttribute(("totalElements"), page.getTotalElements());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());


        return "products/index";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable("id") Integer id, Model model){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        return "products/view_product";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("ch") String keyword, Model model){
        List<Product> products = productService.searchProduct(keyword);

        model.addAttribute("products", products);

        return "products/index";
    }



}
