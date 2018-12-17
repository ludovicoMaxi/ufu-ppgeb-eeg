package br.com.ufu.ppgeb.eeg;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UfuPpgebEegApplicationTests {

//    List<Car> initialCars;
//
//    @Autowired
//    CarService carService;
//
//    @Before
//    public void setUp() {
//        initialCars = new ArrayList<>();
//
//        Car corsa = new Car();
//        corsa.setId(2L);
//        corsa.setBrand("CHEVROLET");
//        corsa.setModel("CORSA");
//        corsa.setColor("BRANCO");
//        corsa.setYear(2011);
//        corsa.setPrice(22000.10F);
//        corsa.setDescription("1.0");
//        corsa.setIsNew(false);
//        initialCars.add(corsa);
//
//        Car civic = new Car();
//        civic.setId(3L);
//        civic.setBrand("HONDA");
//        civic.setModel("CIVIC");
//        civic.setColor("AZUL");
//        civic.setYear(2017);
//        civic.setPrice(90000.10F);
//        civic.setDescription("2.0");
//        civic.setIsNew(true);
//        initialCars.add(civic);
//
//
//        Car gol = new Car();
//        gol.setId(4L);
//        gol.setBrand("VOLKSWAGEM");
//        gol.setModel("GOL");
//        gol.setColor("AMARELO");
//        gol.setYear(2018);
//        gol.setPrice(45000.10F);
//        gol.setDescription("1.6");
//        gol.setIsNew(true);
//        initialCars.add(gol);
//    }
//
//    @Test
//    public void contextLoads() {
//
//    }
//
//    @Test
//    public void findAll() {
//
//        List<Car> allCars = carService.findAll();
//
//        Assert.assertNotNull("allCars cannot be null", allCars);
//        Assert.assertEquals("allcars and initialCars must be equals", allCars, initialCars);
//
//    }
//
//    @Test
//    public void findById() {
//
//        Car corsa = carService.findById(2L);
//        Assert.assertNotNull("corsa cannot be null", corsa);
//        Assert.assertEquals("corsa and initialCars(0) must be equals", corsa, initialCars.get(0));
//
//        Car civic = carService.findById(3L);
//        Assert.assertNotNull("civic cannot be null", civic);
//        Assert.assertEquals("civic and initialCars(1) must be equals", civic, initialCars.get(1));
//
//        Car gol = carService.findById(4L);
//        Assert.assertNotNull("gol cannot be null", gol);
//        Assert.assertEquals("gol and initialCars(2) must be equals", gol, initialCars.get(2));
//
//    }
//
//    @Test
//    public void findByFilter() {
//
//        List<Car> allCars = carService.findByFilter(null, null, null, null, null,null, null, null);
//
//        Assert.assertNotNull("allCars cannot be null", allCars);
//        Assert.assertEquals("allcars and initialCars must be equals", allCars, initialCars);
//
//        List<Car> filterIsNewFalse = carService.findByFilter(null, null, null, null, null,null, null, Boolean.FALSE);
//        Assert.assertEquals("filterIsNewFalse must be size = 1",filterIsNewFalse.size(), 1);
//        Assert.assertEquals("filterIsNewFalse and initialCars(0) must be equals", filterIsNewFalse.get(0), initialCars.get(0));
//
//        List<Car> filterIsHonda = carService.findByFilter("HONDA", null, null, null, null,null, null, null);
//        Assert.assertEquals("filterIsHonda must be size = 1",filterIsHonda.size(), 1);
//        Assert.assertEquals("filterIsHonda and initialCars(1) must be equals", filterIsHonda.get(0), initialCars.get(1));
//
//        List<Car> filterIsGol = carService.findByFilter(null, "GOL", null, null, null,null, null, null);
//        Assert.assertEquals("filterIsGol must be size = 1",filterIsGol.size(), 1);
//        Assert.assertEquals("filterIsGol and initialCars(2) must be equals", filterIsGol.get(0), initialCars.get(2));
//
//        List<Car> filterIsNewAndYearGrater2015 = carService.findByFilter(null, null, null, null, null,null, null, Boolean.TRUE);
//        List<Car> compare = new ArrayList<>();
//        compare.add(initialCars.get(1));
//        compare.add(initialCars.get(2));
//        Assert.assertEquals("filterIsNewAndYearGrater2015 must be size = 2",filterIsNewAndYearGrater2015.size(), 2);
//        Assert.assertEquals("filterIsNewAndYearGrater2015 and initialCars(2) must be equals", filterIsNewAndYearGrater2015, compare);
//
//        List<Car> filterPriceGreater50k = carService.findByFilter(null, null, null, null, null,null, 50000F, null);
//        Assert.assertEquals("filterPriceGreater50k must be size = 1",filterPriceGreater50k.size(), 1);
//        Assert.assertEquals("filterPriceGreater50k and initialCars(1) must be equals", filterPriceGreater50k.get(0), initialCars.get(1));
//
//        List<Car> filterPriceLess10k = carService.findByFilter(null, null, null, null, null,10000F, null, null);
//        Assert.assertEquals("filterPriceLess10k must be size = 0",filterPriceLess10k.size(), 0);
//
//    }
//
//    @Test
//    public void insert() {
//
//        Car car = new Car();
//        car.setBrand("FORD");
//        car.setModel("FOCUS");
//        car.setColor("BRANCO");
//        car.setYear(2016);
//        car.setPrice(47000.50F);
//        car.setIsNew(false);
//        car.setDescription("Rodas liga leve");
//
//        carService.save(car);
//
//        Car foundCar = carService.findById(car.getId());
//
//        Assert.assertNotNull("foundCar cannot be null", foundCar);
//        Assert.assertEquals("Object save and found must be equals", car, foundCar);
//
//    }
//
//    @Test
//    public void update() throws Exception {
//
//        Car corsa = new Car();
//        corsa.setId(2L);
//        corsa.setBrand("CHEVROLET");
//        corsa.setModel("CORSA");
//        corsa.setColor("BRANCO");
//        corsa.setYear(2010);
//        corsa.setPrice(22000.10F);
//        corsa.setDescription("Rodas liga leve");
//        corsa.setIsNew(false);
//
//        corsa = carService.update(corsa);
//
//        Car foundCar = carService.findById(2l);
//
//        Assert.assertNotNull("foundCar cannot be null", foundCar);
//        Assert.assertEquals("Object save and found must be equals", corsa, foundCar);
//
//    }

}
