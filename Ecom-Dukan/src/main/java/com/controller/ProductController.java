package com.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model.Product;
import com.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	public static String uploadDir= System.getProperty("user.dir")+"/src/main/resources/static/images";
    
    @GetMapping
    public String listProducts(Model model) {
    	model.addAttribute("products", productService.getAllProducts());
    	return "products";
    }
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
    	model.addAttribute("product", new Product());
    	return "add_product";
    }
    
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,@RequestParam("image") MultipartFile file) throws IOException{
    	
    	String imageUUID;
    	if(!file.isEmpty()) {
    		imageUUID= file.getOriginalFilename();
    		
    		Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
    		Files.write(fileNameAndPath, file.getBytes());
    	}
    	else {
    		imageUUID = "abc";
    	}
    	product.setImagePath(imageUUID);
    	productService.saveProduct(product);
    	
    	return "redirect:/products";
    	
    }

}
