package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PartRepository partRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    @WithMockUser("spring")
    public void parts_addPartCorrectly() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);

        this.supplierRepository.saveAndFlush(supplier);

        this.mvc
                .perform(post("/parts/add")
                        .param("name", "pesho")
                        .param("price", "12.33")
                        .param("supplier", "pesho"));

      Assert.assertEquals(1, this.partRepository.count());
    }

    @Test
    @WithMockUser("spring")
    public void parts_editPartCorrectly() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);

        this.supplierRepository.saveAndFlush(supplier);

        Part part = new Part();
        part.setName("neshto");
        part.setPrice(BigDecimal.ONE);
        part.setSupplier(supplier);

        part = this.partRepository.saveAndFlush(part);

        this.mvc
                .perform(post("/parts/edit/" + part.getId())
                        .param("name", "nishto")
                        .param("price", "12.33"));

        Part partActual = this.partRepository.findAll().get(0);

        Assert.assertEquals("nishto", partActual.getName());
        Assert.assertEquals(BigDecimal.valueOf(12.33) , partActual.getPrice());
    }

    @Test
    @WithMockUser("spring")
    public void parts_deletePartCorrectly() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(true);

        this.supplierRepository.saveAndFlush(supplier);

        Part part = new Part();
        part.setName("neshto");
        part.setPrice(BigDecimal.ONE);
        part.setSupplier(supplier);

        part = this.partRepository.saveAndFlush(part);

        this.mvc
                .perform(post("/parts/delete/" + part.getId()));

        Assert.assertEquals(0, this.partRepository.count());
    }

    @Test
    @WithMockUser("spring")
    public void parts_ReturnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/parts/all"))
                .andExpect(view().name("all-parts"));
    }
}
