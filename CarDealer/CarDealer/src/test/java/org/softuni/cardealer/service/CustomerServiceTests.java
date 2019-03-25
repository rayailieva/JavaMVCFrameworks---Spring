package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Customer;
import org.softuni.cardealer.domain.models.service.CustomerServiceModel;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerServiceTests {

    @Autowired
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
    }

    @Test
    public void customerService_saveCustomerWithValidValues_ReturnsCorrect() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        CustomerServiceModel toBeSaved = new CustomerServiceModel();
        toBeSaved.setName("gosho");
        toBeSaved.setBirthDate(LocalDate.now());
        toBeSaved.setYoungDriver(true);

        CustomerServiceModel actual = customerService.saveCustomer(toBeSaved);
        CustomerServiceModel expected = this.modelMapper
                .map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_saveCustomerWithInValidValues_ThrowsException() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        CustomerServiceModel toBeSaved = new CustomerServiceModel();
        toBeSaved.setName(null);
        toBeSaved.setBirthDate(LocalDate.now());
        toBeSaved.setYoungDriver(true);

        customerService.saveCustomer(toBeSaved);    }

    @Test
    public void customerService_editCustomerWithValidValues_ReturnsCorrect() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        Customer customer = new Customer();
        customer.setName("gosho");
        customer.setBirthDate(LocalDate.now());
        customer.setYoungDriver(true);

        customer = this.customerRepository.saveAndFlush(customer);

        CustomerServiceModel toBeEdited = new CustomerServiceModel();

        toBeEdited.setId(customer.getId());
        toBeEdited.setName("pesho");
        toBeEdited.setBirthDate(LocalDate.MIN);
        toBeEdited.setYoungDriver(false);

        CustomerServiceModel actual = customerService.editCustomer(toBeEdited);
        CustomerServiceModel expected = this.modelMapper
                .map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_editCustomerWithInvalidValues_ThrowsException() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        CustomerServiceModel customer = new CustomerServiceModel();
        customer.setName("gosho");
        customer.setBirthDate(LocalDate.now());
        customer.setYoungDriver(true);

        customerService.saveCustomer(customer);

        CustomerServiceModel toBeEdited = new CustomerServiceModel();

        toBeEdited.setId(customer.getId());
        toBeEdited.setName(null);
        toBeEdited.setBirthDate(LocalDate.MIN);
        toBeEdited.setYoungDriver(false);

        customerService.editCustomer(toBeEdited);
    }

    @Test
    public void customerService_deleteCustomerWithValidId_ReturnsCorrect() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        CustomerServiceModel customer = new CustomerServiceModel();
        customer.setName("gosho");
        customer.setBirthDate(LocalDate.now());
        customer.setYoungDriver(true);

       customerService.saveCustomer(customer);

        customerService.deleteCustomer(customer.getId());

        long expectedCount = 0;
        long actualCount = this.customerRepository.count();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void customerService_deleteCustomerWithInvalidId_ThrowsException() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        CustomerServiceModel customer = new CustomerServiceModel();
        customer.setName("gosho");
        customer.setBirthDate(LocalDate.now());
        customer.setYoungDriver(true);

        customerService.saveCustomer(customer);

        customerService.deleteCustomer("invalidddd");
    }


    @Test
    public void customerService_findCustomerWithValidId_ReturnsCorrect() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        CustomerServiceModel toBeSaved = new CustomerServiceModel();
        toBeSaved.setName("gosho");
        toBeSaved.setBirthDate(LocalDate.now());
        toBeSaved.setYoungDriver(true);

       customerService.saveCustomer(toBeSaved);

        CustomerServiceModel actual =
                this.modelMapper.map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

        CustomerServiceModel expected =
                customerService.findCustomerById(actual.getId());

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_findCustomerWithInvalidId_ThrowsException() {
        CustomerService customerService =
                new CustomerServiceImpl(this.customerRepository, this.modelMapper);

        CustomerServiceModel customer = new CustomerServiceModel();
        customer.setName("gosho");
        customer.setBirthDate(LocalDate.now());
        customer.setYoungDriver(true);

       customerService.saveCustomer(customer);

        customerService.findCustomerById("invalid");
    }
}
