package com.booleanuk.api.bagels.view;

import com.booleanuk.api.bagels.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


public class ProductsRepository {
	private ArrayList<Product> products;

	public ProductsRepository() {
		products = new ArrayList<Product>();
		products.add(new Product(
				"Prod1",
				"mat",
				12
		));
		products.add(new Product(
				"Prod2",
				"renhold",
				30
		));
	}

	public Product getById(int id) {
		return products.stream()
				.filter(product -> product.getId() == id)
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public ArrayList<Product> getAll(){
		return products;
	}

	public ArrayList<Product> getAllFromCategory(String category){
		ArrayList<Product> prods = products.stream()
				.filter(product -> product.getCategory().equals(category))
				.collect(Collectors.toCollection(ArrayList::new));

		if (prods.isEmpty()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
		}
		return prods;
	}

	public Product createNew(Product product){
		boolean isInList = products.stream()
				.anyMatch(product1 -> product1.getName().equals(product.getName()));

		if (isInList){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "already exists");
		}
		products.add(product);
		return product;
	}

	public Product putById(int id, Product product){
		Optional<Product> OP= products.stream()
				.filter(product1 -> product1.getId() == id)
				.findFirst();

		if (OP.isEmpty()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		Product prodToChange = OP.get();
		// check if another exists with same name
		boolean anotherExists = products.stream()
				.anyMatch(product1 -> product1.getId() != id && prodToChange.getName().equals(product1.getName()));

		if(anotherExists){
			throw new ResponseStatusException(HttpStatus.CONFLICT, "another item has same name");
		}

		prodToChange.setName(product.getName());
		prodToChange.setCategory(product.getCategory());
		prodToChange.setPrice(product.getPrice());
		return prodToChange;
	}

	public Product deleteById(int id){
		Optional<Product> isInList = products.stream()
				.filter(author -> author.getId() == id)
				.findFirst();

		if (isInList.isEmpty()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");

		}
		isInList.ifPresent(products::remove);
		return isInList.get();
	}

}
