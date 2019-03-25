package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SuppliersControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    @WithMockUser("spring")
    public void suppliers_addSupplierCorrectly() throws Exception {
        this.mvc
                .perform(post("/suppliers/add")
                        .param("name", "pesho")
                        .param("isImporter", "off"));

        this.mvc
                .perform(post("/suppliers/add")
                        .param("name", "stamat")
                        .param("isImporter", "true"));

        Supplier actual = this.supplierRepository.findAll().get(1);
        Assert.assertEquals(2, this.supplierRepository.count());
        Assert.assertEquals("stamat", actual.getName());
        Assert.assertTrue(actual.getIsImporter());
    }

    @Test
    @WithMockUser("spring")
    public void suppliers_editSupplierCorrectly() throws Exception {

        Supplier supplier1 = new Supplier();
        supplier1.setName("gosho");
        supplier1.setIsImporter(true);

        Supplier supplier2 = new Supplier();
        supplier2.setName("pesho");
        supplier2.setIsImporter(false);

        supplier1 = this.supplierRepository.saveAndFlush(supplier1);
        supplier2 = this.supplierRepository.saveAndFlush(supplier2);

        this.mvc
                .perform(
                        post("/suppliers/edit/" + supplier1.getId())
                        .param("name", "Gosho")
                        .param("isImporter", "false")
                );

        this.mvc
                .perform(
                        post("/suppliers/edit/" + supplier2.getId())
                                .param("name", "Pesho")
                                .param("isImporter", "true")
                );

        Supplier supplier1Actual = this.supplierRepository.findById(supplier1.getId()).orElse(null);
        Supplier supplier2Actual = this.supplierRepository.findById(supplier2.getId()).orElse(null);

        Assert.assertEquals("Gosho", supplier1Actual.getName());
        Assert.assertFalse(supplier1Actual.getIsImporter());

        Assert.assertEquals("Pesho", supplier2Actual.getName());
        Assert.assertTrue(supplier2Actual.getIsImporter());
    }

    @Test
    @WithMockUser("spring")
    public void suppliers_deleteSupplierCorrectly() throws Exception {

        Supplier supplier1 = new Supplier();
        supplier1.setName("gosho");
        supplier1.setIsImporter(true);

        Supplier supplier2 = new Supplier();
        supplier2.setName("pesho");
        supplier2.setIsImporter(false);

        supplier1 = this.supplierRepository.saveAndFlush(supplier1);
        supplier2 = this.supplierRepository.saveAndFlush(supplier2);

        this.mvc
                .perform(
                        post("/suppliers/delete/" + supplier1.getId())
                );

        Assert.assertEquals(1, this.supplierRepository.count());
    }

    @Test
    @WithMockUser("spring")
    public void suppliers_ReturnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/suppliers/all"))
                .andExpect(view().name("all-suppliers"));
    }
}
