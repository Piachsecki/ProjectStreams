import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    static final Integer CURRENT_YEAR = 2020;

    public static void main(String[] args) {
        List<Purchase> produce = DataFactory.produce();


        //level 1.1
        long numberOfClients = produce.stream()
                .map(Purchase::getBuyer)
                .distinct()
                .count();

//        System.out.println(numberOfClients);

        //level 1.2 - jezeli uznajemy ze 1 client moze max i operacje tym blikiem wykonac

        long count = produce.stream()
                .filter(purchase -> Purchase.Payment.BLIK.equals(purchase.getPayment()))
                .map(Purchase::getBuyer)
                .distinct()
                .count();
//        System.out.println(count);

        //level 1.3

        long numbersOfCreditCardPayments = produce.stream()
                .map(Purchase::getPayment)
                .filter(payment -> {
                    int result = payment.compareTo(Purchase.Payment.CREDIT_CARD);
                    return result == 0;
                })
                .count();
//        System.out.println(numbersOfCreditCardPayments);

        //level 1.4

        long numberOfPaymentsInEuros = produce.stream()
                .map(Purchase::getProduct)
                .map(Product::getPrice)
                .map(Money::getCurrency)
                .filter(currency -> currency.equals(Money.Currency.EUR))
                .count();
//        System.out.println(numberOfPaymentsInEuros);

        // level 1.5

        long numberOfDistinctProductsPaidInEuros = produce.stream()
                .map(Purchase::getProduct)
                .distinct()
                .map(Product::getPrice)
                .map(Money::getCurrency)
                .filter(currency -> currency.equals(Money.Currency.EUR))
                .count();
//        System.out.println(numberOfDistinctProductsPaidInEuros);


        //level 2.1
        Map<String, BigDecimal> peopleExpenses = produce.stream()
                .filter(purchase -> Money.Currency.PLN.equals(purchase.getProduct().getPrice().getCurrency()))
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getBuyer().getId(),
                        TreeMap::new,
                        Collectors.mapping(purchase -> purchase.getProduct().getPrice().getValue(),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // level 2.2

        Map<String, ? extends Number> stringMap = myMethod(produce);
//        System.out.println(stringMap);


        //level 2.3
        Map<Purchase.Status, List<Purchase>> collect = produce.stream()
                .map(purchase -> new Purchase(purchase, OrderService.checkOrderStatus(purchase)))
                .collect(
                        Collectors.toMap(
                                Purchase::getStatus,
                                List::of,
                                (List<Purchase> cL, List<Purchase> nL) -> Stream.concat(cL.stream(), nL.stream())
                                        .collect(Collectors.toList())
                        )
                );

//        System.out.println(collect);


        //level 2.4

        Map<String, List<Purchase>> map = produce.stream()
                .filter(purchase -> Money.Currency.EUR.equals(purchase.getProduct().getPrice().getCurrency()))
                .collect(
                        Collectors.groupingBy(
                                purchase -> purchase.getBuyer().getId()));

//        System.out.println(map);


        // level 2.5

        TreeMap<String, List<Product>> collect1 = produce.stream()
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getBuyer().getYear(),
                        TreeMap::new,
                        Collectors.mapping(Purchase::getProduct, Collectors.toList())

                ));

//        System.out.println(collect1);


        // level 2.6
        TreeMap<String, Set<Product.Category>> collect2 = produce.stream()
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getBuyer().getYear(),
                        TreeMap::new,
                        Collectors.mapping(purchase -> purchase.getProduct().getCategory(), Collectors.toSet())

                ));
//        System.out.println(collect2);


        // level 2.7

        long count1 = produce.stream()
                .map(purchase -> purchase.getProduct())
                .distinct()
                .count();
//        System.out.println(count1);

        Map<String, Long> collect3 = produce.stream()
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getProduct().getId(),
                        Collectors.mapping(
                                Purchase::getQuantity,
                                Collectors.reducing(0L, Long::sum))
                ));

//        System.out.println(collect3);

        Comparator<? super Pair<String, Long>> pairComparator=
                Comparator.comparing((Pair<String, Long>p) -> p.getV())
                                .thenComparing(p -> p.getU())
                                        .reversed();


        Pair<String, Long> sorted = collect3.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .sorted(pairComparator)
                .skip(1)
                .findFirst()
                .orElse(new Pair<>("IDK", 123L));
//        System.out.println(sorted);




        // level 3.1

        Map<String, Map<Product.Category, Long>> collect4 = produce.stream()
                .filter(p -> CURRENT_YEAR - (1900 + Integer.parseInt(p.getBuyer().getYear())) > 50)
                .collect(
                        Collectors.groupingBy(
                                p -> p.getBuyer().getYear(),
                                Collectors.groupingBy(
                                        p -> p.getProduct().getCategory(),
                                        Collectors.counting()
                                )
                        )
                );

//        System.out.println(collect4);


        //level 3.2


    }

    private static Map<String, ? extends Number> myMethod(List<Purchase> purchases) {


        return purchases.stream()
                .filter(purchase -> purchase.getQuantity() > 1)
                .filter(purchase -> purchase.getProduct().getCategory().equals(Product.Category.HOBBY))
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getBuyer().getId(),
                        Collectors.mapping(
                                Purchase::getQuantity,
                                Collectors.reducing(0L, Long::sum)))
                );





    }
}