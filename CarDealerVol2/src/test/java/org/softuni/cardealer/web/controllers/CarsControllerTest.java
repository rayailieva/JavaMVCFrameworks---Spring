package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PartRepository partRepository;

    @Test
    @WithMockUser("spring")
    public void cars_addCarCorrectly() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);

        this.supplierRepository.saveAndFlush(supplier);

        Part part = new Part();
        part.setName("neshto");
        part.setPrice(BigDecimal.ONE);
        part.setSupplier(supplier);

        this.partRepository.saveAndFlush(part);

        this.mvc
                .perform(post("/cars/add")
                        .param("make", "opel")
                        .param("model", "astra")
                        .param("travelledDistance", "20132")
                        .param("parts", "neshto"));

        Assert.assertEquals(1, this.carRepository.count());
    }
}
