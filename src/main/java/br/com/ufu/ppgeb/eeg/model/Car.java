package br.com.ufu.ppgeb.eeg.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;


/**
 * Created by joaol on 15/04/18.
 */
@Entity
public class Car {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;


    @Column(length = 40, nullable = false)
    private String brand;

    @Column(length = 50, nullable = false)
    private String model;

    @Column(length = 30, nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Float price;

    private String description;

    @Column(nullable = false, name = "IS_NEW", columnDefinition = "TINYINT", length = 1)
    private boolean isNew;

    @Column(nullable = false, name = "CREATED_DATE")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date updatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return isNew == car.isNew &&
                Objects.equals(id, car.id) &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model) &&
                Objects.equals(color, car.color) &&
                Objects.equals(year, car.year) &&
                Objects.equals(price, car.price) &&
                Objects.equals(description, car.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, brand, model, color, year, price, description, isNew);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", isNew=" + isNew +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
