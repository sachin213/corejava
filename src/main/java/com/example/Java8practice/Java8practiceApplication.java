package com.example.Java8practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@SpringBootApplication
public class Java8practiceApplication {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private OrderRepo orderRepo;

	public static void main(String[] args) {

		SpringApplication.run(Java8practiceApplication.class, args);

	}

	//1.Exercise 1 — Obtain a list of products belongs to category “Books” with price > 100
	public void ex1()
	{
		List<Product> productList =  productRepo.findAll().stream()
				.filter(p -> "Books".equalsIgnoreCase(p.getCategory()) && p.getPrice() > 100)
				.collect(Collectors.toList());
	}

	//Exercise 2 — Obtain a list of order with products belong to category “Baby”
	public void ex2()
	{
		List<Order> orderList = orderRepo.findAll().stream()
						.filter(order -> order.getProducts().stream()
										.anyMatch(product -> product.getCategory().equalsIgnoreCase("BABY"))
								)
				.collect(Collectors.toList());
	}

	//Exercise 3 — Obtain a list of product with category = “Toys” and then apply 10% discount
	public void ex3() {
		List<Product> productList = productRepo.findAll()
				.stream()
				.filter(p -> "Toys".equalsIgnoreCase(p.getCategory()))
				.map(p -> p.withPrice(p.getPrice() * 0.9))
				.collect(Collectors.toList());
	}

	//Exercise 4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021
	public void ex4() {
		List<Product> productList = orderRepo.findAll().stream()
				.filter(order -> order.getCustomer().getTier() == 2)
				.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1)))
				.filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 2, 1)))
				.flatMap(order -> order.getProducts().stream())
				.distinct()
				.collect(Collectors.toList());
	}

	//Exercise 5 — Get the cheapest products of “Books” category
	public void ex5()
	{
		Optional<Product> productWithCheapestPrice = productRepo.findAll().stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
				.sorted(Comparator.comparing(Product::getPrice))
				.findFirst();

		Optional<Product> productWithCheapestPrice1 = productRepo.findAll().stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
				.min(Comparator.comparing(product -> product.getPrice()));

		Optional<Product> productWithCheapestPrice2 = productRepo.findAll().stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
				.min((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
	}

	//Exercise 6 — Get the 3 most recent placed order
	public void ex6()
	{
		List<Order> orderList = orderRepo.findAll().stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.limit(3)
				.collect(Collectors.toList());
	}

	//Exercise 7 — Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list
	public void ex7() {
		List<Product> productList =  orderRepo.findAll()
				.stream()
				.filter(order -> order.getOrderDate().equals(LocalDate.of(2021, 3, 15)))
				.peek(System.out::println)
				.flatMap(order -> order.getProducts().stream())
				.collect(Collectors.toList());
	}

	//Exercise 8 — Calculate total lump sum of all orders placed in Feb 2021
	public void ex8() {
		orderRepo.findAll()
				.stream()
				.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1)))
				.filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 3, 1)))
				.flatMap(order -> order.getProducts().stream())
				.mapToDouble(product -> product.getPrice())
				.sum();

		orderRepo.findAll()
				.stream()
				.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1)))
				.filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 3, 1)))
				.flatMapToDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice))
				.sum();
	}

	//Exercise 9 — Calculate order average payment placed on 14-Mar-2021
	public void ex9()
	{
		orderRepo.findAll().stream()
				.filter(order -> order.getOrderDate().equals(LocalDate.of(2021, 4, 14)))
				.flatMap(order -> order.getProducts().stream())
				.mapToDouble(Product::getPrice)
				.average().getAsDouble();
	}

	//Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Books”
	public void ex10()
	{
		DoubleSummaryStatistics statistics = productRepo.findAll()
				.stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
				.mapToDouble(Product::getPrice)
				.summaryStatistics();

		System.out.println(String.format("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f",
				statistics.getCount(), statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getSum()));
	}

	//Exercise 11 — Obtain a data map with order id and order’s product count
	public void ex11()
	{
	Map<Long,Integer> mapEx11 = 	orderRepo.findAll()
				.stream()
				.collect(
						Collectors.toMap(
						order -> order.getId(),
						order -> order.getProducts().size()
						)
				);
	}

	//Exercise 12 — Produce a data map with order records grouped by customer
	public void ex12()
	{
		Map<Customer,List<Order>> customerListMap = orderRepo.findAll()
				.stream()
				.collect(Collectors.groupingBy(order -> order.getCustomer()));
	}

	//Exercise 13 — Produce a data map with order record and product total sum
	public void ex13()
	{
		Map<Order,Double> priceMap = orderRepo.findAll()
				.stream()
				.collect(Collectors.toMap(
						//order -> order.getId(),
						Function.identity(),
						order -> order.getProducts().stream()
								.mapToDouble(product -> product.getPrice()).sum()
				));
	}

	//Exercise 14 — Obtain a data map with list of product by category
	public void ex14()
	{
		Map<String,List<Product>> listOfProductWithCategory = productRepo.findAll()
				.stream()
				.collect(Collectors.groupingBy(product -> product.getCategory()));

		//Exercise 14.1 — Obtain a data map with list of product name by category
		Map<String,List<String>> listOfProducNametWithCategory = productRepo.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(
						product -> product.getCategory(),
						Collectors.mapping(product -> product.getName(),Collectors.toList()))
				);

	}

	//Exercise 15 — Get the most expensive product by category
	//
	public void ex15()
	{
		//Map<Category(String),price(Double)>
		//p1 Books, 102
		//p2 Books , 105
		//p3 Toy, 107
		//P4 Toy , 108
		//output
		//1.Books,105 2. Toy, 108

	Map<String,Optional<Product>> mapOfMaxPriceByProductCategory =
		 productRepo.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						Product::getCategory,
						Collectors.maxBy(Comparator.comparing(Product::getPrice))
						));
	}
}


