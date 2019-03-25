package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartServiceTests {

    @Autowired
    private PartRepository partRepository;
    private ModelMapper modelMapper;

    public void init(){
        this.modelMapper = new ModelMapper();
    }

    @Test
    public void partService_savePartWithCorrectValues_ReturnsCorrect() {

        PartService partService =
                new PartServiceImpl(this.partRepository, this.modelMapper);

        PartServiceModel toBeSaved = new PartServiceModel();
        toBeSaved.setName("guma");
        toBeSaved.setPrice(BigDecimal.TEN);

        PartServiceModel actual = partService.savePart(toBeSaved);

        PartServiceModel expected = this.modelMapper
                .map(this.partRepository.findAll().get(0), PartServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test(expected = Exception.class)
    public void partService_savePartWithNullValues_ThrowsException() {

        PartService partService =
                new PartServiceImpl(this.partRepository, this.modelMapper);

        PartServiceModel toBeSaved = new PartServiceModel();
        toBeSaved.setName(null);
        toBeSaved.setPrice(BigDecimal.TEN);

        partService.savePart(toBeSaved);
    }

    @Test
    public void partService_editPartWithCorrectValues_ReturnsCorrect() {

        PartService partService =
                new PartServiceImpl(this.partRepository, this.modelMapper);

        Part part = new Part();
        part.setName("guma");
        part.setPrice(BigDecimal.TEN);

        this.partRepository.saveAndFlush(part);

        PartServiceModel toBeEdited = new PartServiceModel();
        toBeEdited.setId(part.getId());
        toBeEdited.setName("lllll");
        toBeEdited.setPrice(BigDecimal.ONE);

        PartServiceModel actual = partService.editPart(toBeEdited);
        PartServiceModel expected = this.modelMapper
                .map(this.partRepository.findAll().get(0), PartServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
    }

    @Test(expected = Exception.class)
    public void partService_editPartWithNullValues_ThrowsException() {

        PartService partService =
                new PartServiceImpl(this.partRepository, this.modelMapper);

        PartServiceModel toBeSaved = new PartServiceModel();
        toBeSaved.setName("guma");
        toBeSaved.setPrice(BigDecimal.TEN);

        partService.savePart(toBeSaved);

        PartServiceModel toBeEdited = this.modelMapper
                .map(this.partRepository.findAll().get(0), PartServiceModel.class);

        toBeEdited.setPrice(BigDecimal.ONE);
        toBeEdited.setName(null);

       partService.editPart(toBeEdited);
    }

    @Test
    public void partService_deletePartWithCorrectValues_ReturnsCorrect() {

        PartService partService =
                new PartServiceImpl(this.partRepository, this.modelMapper);

        PartServiceModel toBeSaved = new PartServiceModel();
        toBeSaved.setName("guma");
        toBeSaved.setPrice(BigDecimal.TEN);

        partService.savePart(toBeSaved);

        partService.deletePart(toBeSaved.getId());


    }

    @Test(expected = Exception.class)
    public void partService_deletePartWithNullValues_ThrowsException() {

        PartService partService =
                new PartServiceImpl(this.partRepository, this.modelMapper);

        PartServiceModel toBeSaved = new PartServiceModel();
        toBeSaved.setName("guma");
        toBeSaved.setPrice(BigDecimal.TEN);

        partService.savePart(toBeSaved);

        PartServiceModel toBeDeleted = this.modelMapper
                .map(this.partRepository.findAll().get(0), PartServiceModel.class);

        partService.deletePart(toBeDeleted.getId());
    }

    @Test(expected = Exception.class)
    public void partService_findByPartIdWithCNullValues_ThrowsExcepion() {

        PartService partService =
                new PartServiceImpl(this.partRepository, this.modelMapper);

        PartServiceModel toBeSaved = new PartServiceModel();
        toBeSaved.setName("guma");
        toBeSaved.setPrice(BigDecimal.TEN);

        partService.savePart(toBeSaved);

        partService.findPartById(null);
    }
}
