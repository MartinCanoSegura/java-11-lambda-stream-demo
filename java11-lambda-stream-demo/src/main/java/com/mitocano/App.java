package com.mitocano;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mitocano.model.Person;
import com.mitocano.model.Product;

public class App {

	public static void main(String[] args) {
		
		Person p1 = new Person(1, "Mito", LocalDate.of(1991, 1, 21));
        Person p2 = new Person(2, "Cano", LocalDate.of(1990, 2, 21));
        Person p3 = new Person(3, "Jaime", LocalDate.of(1980, 6, 23));
        Person p4 = new Person(4, "Duke", LocalDate.of(2019, 5, 15));
        Person p5 = new Person(5, "James", LocalDate.of(2010, 1, 4));

        Product pr1 = new Product(1, "Ceviche", 15.0);
        Product pr2 = new Product(2, "Chilaquiles", 25.50);
        Product pr3 = new Product(3, "Bandeja Paisa", 35.50);
        Product pr4 = new Product(4, "Ceviche", 15.0);

        List<Person> persons = Arrays.asList(p1, p2, p3, p4, p5);
        List<Product> products = Arrays.asList(pr1, pr2, pr3, pr4);
        
//        Lambda //method reference  -->  list.forEach(System.out::println);
        
/****** CON JDK 1.2 ************************************************************************/
//        for(int i = 0; i<persons.size(); i++){ System.out.println(persons.get(i)); }
        
/****** CON JDK 1.6 ************************************************************************/
//        for(Person p : persons) { System.out.println(p); }
        
/****** CON JDK 1.8 Paradigma Progrmacion Funcional. **************************************************************************************/
        /* primera forma: con expresion lambda. El parametro p de la izq. para iterar, a la der. lo que se va a hacer con el parametro */ 
//        persons.forEach(p -> System.out.println(p)); 
        
        /* segunda forma: con metodos a referencia. si el parametro de la izquierda se envia como parametro la parte derecha  */
//        persons.forEach(System.out::println);		//para el caso de un servicio seria: persons.forEach(service.registrar);
        
        
/******* API Stream SE PUEDE APLICAR A CUALQUIER CONTEXTO, SERVICIOS REST, CAPA DE LOGICA DE NEGOCIO, CAPA DE BASE DE DATOS, ETC..*********/
        /********** 1-Filter (param: Predicate)  Metodo para trabajar de una forma declarativa con las collecciones *********/
        List<Person> filteredList  = persons.stream()
        									 .filter(p -> App.getAge(p.getBirthDate()) >= 18)
        									 .collect(Collectors.toList());
//        App.printList(filteredList);
//        System.out.println(filteredList);
//        filteredList.forEach(System.out::println);
        
        
        /********** 2-Map (param: Function)	map permite tranformar los elementos de la colleccion en el stream *****************/
//      FORMA A:
        //        List<Integer> filteredList2 = persons.stream().map(p -> App.getAge(p.getBirthDate())).collect(Collectors.toList());
        List<Integer> filteredList2A = persons.stream()
        									 .filter(p -> App.getAge(p.getBirthDate()) >= 18)
        									 .map(p -> App.getAge(p.getBirthDate()))
        									 .collect(Collectors.toList());
//        filteredList2A.forEach(System.out::println);
        
//      FORMA B:
        List<String> filteredList2B = persons.stream()
											 .map(p -> "Mexico - " + p.getName())
											 .collect(Collectors.toList());
//        filteredList2B.forEach(System.out::println);
        
//      FORMA C:
        Function<String, String> coderFunction = name -> "MEXICO - " + name;
        List<String> filteredList2C = persons.stream()
							        		 //.map(p -> p.getName()) 	// Verboso
							        		 .map(Person::getName)    	// mejorado
											 .map(coderFunction)
											 .collect(Collectors.toList());
//       filteredList2C.forEach(System.out::println);
        
        
        
        /******** 3-Sorted (param: Comparator)  Para Ordenar a travez de una coleccion de Objetos por lo general, no solo de enteros o strings. Spark muchos datos *********/
       Comparator<Person> byNameAsc = (Person O1, Person O2) -> O1.getName().compareTo(O2.getName());
       Comparator<Person> byNameDesc = (Person O1, Person O2) -> O2.getName().compareTo(O1.getName());
       Comparator<Person> byBirthDate = (Person O1, Person O2) -> O1.getBirthDate().compareTo(O2.getBirthDate());
/*       persons.stream()
       		  //.sorted(byNameAsc)
       		  //.sorted(byNameDesc)
       		  .sorted(byBirthDate)
       		  .collect(Collectors.toList())
       		  .forEach(System.out::println);
*/       
       
       /******** 4-Match (param: Predicate)   *********************************************************/
       		Predicate<Person> startWithPredicate = person -> person.getName().startsWith("J");
//       a) anyMatch : No evalua todo el stream, termina en la primera coincidencia
       		boolean rpta = persons.stream().anyMatch(startWithPredicate);
//       		System.out.println(rpta);
       		
//       b) allMatch : Evalua todo el stream bajo la condicion, si todos los elementos en la estructura coinciden regresa true de otro modo false
       		boolean rpta2 = persons.stream().allMatch(startWithPredicate);
//       		System.out.println(rpta2);
       		
//       c) noneMatch : Evalua todo el stream bajo la condicion
       		boolean rpta3 = persons.stream().noneMatch(startWithPredicate);
//       		System.out.println(rpta3);
       		
       		
       /******* 5-Limit/Skip  Uril para paginacion  **********/
        persons.stream()
        	   .collect(Collectors.toList());
//        	   .forEach(System.out::println); //todos los elementos de la lista
        
//        System.out.println("-------------------------------------------------");
        
        //SKIP:
        persons.stream()
        	   .skip(2) // A PARTIR DEL SEGUNDO ELEMENTO DE LA LISTA
        	   .collect(Collectors.toList());
//        	   .forEach(System.out::println);  
       //LIMIT:		 
		persons.stream()
		 	   .limit(2) // HASTA EL SEGUNDO ELEMENTO DE LA LISTA
		 	   .collect(Collectors.toList());
//		 	   .forEach(System.out::println);
		 
		// SKIP & LIMIT:
		int pageNumber = 2;
		int pageSize = 2;
		persons.stream()
			   .skip(pageNumber * pageSize)
	 		   .limit(pageSize)
	 		   .collect(Collectors.toList()); // para evitar error de compilacion
//	 		   .forEach(System.out::println);
        
		
		/**** 6-Collectors   Analogo al goupBy de SQL, Agrupar por columnas ************************************************************************/
		products.stream()
 	   .collect(Collectors.toList())
 	   .forEach(System.out::println);
		System.out.println("----------------------------------------");
		
        // GroupBy
		Map<Double, List<Product>>  collect1 = products.stream().filter(p -> p.getPrice() > 20).collect(Collectors.groupingBy(Product::getPrice));
		Map<String, List<Product>>  collect2 = products.stream().filter(p -> p.getPrice() > 20).collect(Collectors.groupingBy(Product::getName));
//		System.out.println(collect2);
		
		// Counting: para contar la cantidad de elementos deacuerdo al criterio de agrupamiento
		Map<String, Long> collect3 = products.stream().collect(Collectors.groupingBy(Product::getName, Collectors.counting()));
		System.out.println(collect3);
		
		// Agrupando por nombre del producto y sumando
		Map<String, Double> collect4 = products.stream().collect(Collectors.groupingBy(Product::getName, Collectors.summingDouble(Product::getPrice)));
//		System.out.println(collect4);
		
		// Obteniendo la suma y el resumen
		DoubleSummaryStatistics statistis = products.stream().collect(Collectors.summarizingDouble(Product::getPrice));
//		System.out.println(statistis);
//		System.out.println(statistis.getAverage());
		
		
		/**** 7.reduce: Para agrupara algunos criterios como acumuladores ********************************************************************/
//		Optional<Double> total = products.stream().map(Product::getPrice)
//						 						  .reduce((a,b) -> a+b)
//						 						  .reduce(Double::sum);
//		System.out.println(total.get());
		

	}
	
	
	
	public static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public static void printList(List<?> list){
        list.forEach(System.out::println);
    } 

}
