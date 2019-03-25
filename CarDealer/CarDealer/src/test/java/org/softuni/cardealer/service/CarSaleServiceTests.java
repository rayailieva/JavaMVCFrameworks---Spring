package org.softuni.cardealer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.models.service.CarSaleServiceModel;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.domain.models.service.PartSaleServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.CarSaleRepository;
import org.softuni.cardealer.repository.PartSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarSaleServiceTests {

    @Autowired
    private CarSaleRepository carSaleRepository;
    @Autowired
    private PartSaleRepository partSaleRepository;
    @Autowired
    private CarRepository carRepository;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
    }

    @Test
    public void carSaleService_saleCarSaleWithCorrectValues_ReturnsCorrect(){
        SaleService saleService =
                new SaleServiceImpl(this.carSaleRepository, this.partSaleRepository, this.modelMapper);

        CarSaleServiceModel carSaleServiceModel = new CarSaleServiceModel();

        Car car = new Car();
        car.setMake("bmv");
        car.setModel("3");
        car.setTravelledDistance(10000L);

        this.carRepository.saveAndFlush(car);

        CarServiceModel carServiceModel =
                this.modelMapper.map(this.carRepository.findAll().get(0), CarServiceModel.class);

        carSaleServiceModel.setCar(carServiceModel);

    }

    @Test(expected = Exception.class)
    public void carSaleService_saleCarSaleWithNullValues_ThrowsException(){

        Car car = new Car();
        car.setMake(null);
        car.setModel("3");
        car.setTravelledDistance(10000L);

        this.carRepository.saveAndFlush(car);


    }
}
